/*
 * Copyright 2005-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.configuration.impl.jpa;

import org.ameba.annotation.Measured;
import org.ameba.annotation.TxService;
import org.ameba.exception.NotFoundException;
import org.ameba.exception.ResourceExistsException;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.configuration.PreferencesService;
import org.openwms.core.configuration.PropertyScope;
import org.openwms.core.configuration.impl.file.GenericPreference;
import org.openwms.core.configuration.impl.file.PreferenceDao;
import org.openwms.core.configuration.impl.file.PreferenceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * A PreferencesServiceImpl is a transactional Spring powered service implementation to manage {@code Preferences}.
 *
 * @author Heiko Scherrer
 */
@Validated
@TxService
class PreferencesServiceImpl implements PreferencesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesServiceImpl.class);
    private final PreferenceDao fileDao;
    private final PreferenceRepository preferenceRepository;
    private final BeanMapper mapper;

    PreferencesServiceImpl(PreferenceDao fileDao, PreferenceRepository preferenceRepository, BeanMapper mapper) {
        this.fileDao = fileDao;
        this.preferenceRepository = preferenceRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Collection<PreferenceEO> findAll() {
        return preferenceRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Collection<PreferenceEO> findAll(@NotEmpty String owner, @NotNull PropertyScope scope) {
        Collection<PreferenceEO> result = preferenceRepository.findByOwnerAndScope(owner, scope);
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public PreferenceEO findBy(@NotEmpty String pKey) {
        return preferenceRepository.findBypKey(pKey).orElseThrow(() -> new NotFoundException(format("Preference with key [%s] does not exist", pKey)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Optional<PreferenceEO> findBy(@NotEmpty String owner, @NotNull PropertyScope scope, @NotEmpty String key) {
        return preferenceRepository.findByOwnerAndScopeAndKey(owner, scope, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public PreferenceEO create(@NotNull PreferenceEO preference) {
        if (preference.getPersistentKey() != null) {
            throw new NotFoundException(format("Preference has a pKey [%s] and cannot be created", preference.getPersistentKey()));
        }
        Optional<PreferenceEO> eoOpt = preferenceRepository.findByOwnerAndScopeAndKey(preference.getOwner(), preference.getScope(), preference.getKey());
        if (eoOpt.isPresent()) {
            throw new ResourceExistsException(format("Preference with key [%s] and owner [%s] of scope [%s] already exists and cannot be created", preference.getKey(), preference.getOwner(), preference.getScope()));
        }
        PreferenceEO saved = preferenceRepository.save(preference);
        LOGGER.debug("Created Preference [{}]", saved);
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public PreferenceEO update(@NotEmpty String pKey, @NotNull PreferenceEO preference) {
        Optional<PreferenceEO> eoOpt = preferenceRepository.findBypKey(pKey);
        if (eoOpt.isEmpty()) {
            throw new NotFoundException(format("Preference with key [%s] does not exist", pKey));
        }
        PreferenceEO eo = eoOpt.get();
        PreferenceEO saved = preferenceRepository.save(mapper.mapFromTo(preference, eo));
        LOGGER.debug("Updated Preference [{}]", saved);
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public PreferenceEO save(@NotNull PreferenceEO preference) {
        PreferenceEO saved = preferenceRepository.save(preference);
        LOGGER.debug("Updated Preference [{}]", saved);
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotEmpty String pKey) {
        Optional<PreferenceEO> eoOpt = preferenceRepository.findBypKey(pKey);
        if (eoOpt.isEmpty()) {
            throw new NotFoundException(format("Preference with key [%s] does not exist", pKey));
        }
        PreferenceEO eo = eoOpt.get();
        preferenceRepository.delete(eo);
        LOGGER.debug("Deleted Preference [{}]", eo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void reloadInitialPreferences() {
        mergeApplicationProperties();
    }

    private void mergeApplicationProperties() {
        List<GenericPreference> fromFile = fileDao.findAll();
        Map<PreferenceKey, PreferenceEO> persistedPrefs = preferenceRepository.findAll().stream()
                .collect(Collectors.toMap(PreferenceEO::getPrefKey, p -> p));
        for (GenericPreference pref : fromFile) {
            if (!persistedPrefs.containsKey(pref.getPrefKey())) {
                PreferenceEO saved = preferenceRepository.save(mapper.map(pref, PreferenceEO.class));
                LOGGER.debug("Merged and saved Preference [{}]", saved);
            }
        }
    }
}