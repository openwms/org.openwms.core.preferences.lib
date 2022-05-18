/*
 * Copyright 2005-2022 the original author or authors.
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
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.configuration.PreferencesEvent;
import org.openwms.core.configuration.PreferencesService;
import org.openwms.core.configuration.PropertyScope;
import org.openwms.core.configuration.impl.file.PreferenceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.openwms.core.configuration.PreferencesConstants.ALREADY_EXISTS;
import static org.openwms.core.configuration.PreferencesConstants.ALREADY_EXISTS_WITH_OWNER_AND_SCOPE_AND_KEY;
import static org.openwms.core.configuration.PreferencesConstants.NOT_FOUND_BY_PKEY;

/**
 * A PreferencesServiceImpl is a transactional Spring managed service implementation to manage {@code Preferences}.
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
    private final Translator translator;
    private final ApplicationContext ctx;

    PreferencesServiceImpl(PreferenceDao fileDao, PreferenceRepository preferenceRepository, BeanMapper mapper, Translator translator, ApplicationContext ctx) {
        this.fileDao = fileDao;
        this.preferenceRepository = preferenceRepository;
        this.mapper = mapper;
        this.translator = translator;
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull PreferenceEO findByPKey(@NotBlank String pKey) {
        return findByPKeyInternal(pKey);
    }

    private PreferenceEO findByPKeyInternal(String pKey) {
        return preferenceRepository.findBypKey(pKey).orElseThrow(
                () -> new NotFoundException(translator, NOT_FOUND_BY_PKEY, new String[]{pKey}, pKey)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Collection<PreferenceEO> findAll() {
        return preferenceRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Collection<PreferenceEO> findForOwnerAndScope(@NotBlank String owner, @NotNull PropertyScope scope) {
        var result = preferenceRepository.findByOwnerAndScope(owner, scope);
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Optional<PreferenceEO> findForOwnerAndScopeAndKey(String owner, @NotNull PropertyScope scope, @NotBlank String key) {
        return preferenceRepository.findByOwnerAndScopeAndKey(owner, scope, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull PreferenceEO create(@NotNull PreferenceEO preference) {
        if (preference.hasPersistentKey()) {
            throw new ResourceExistsException(translator, ALREADY_EXISTS,
                    new String[]{preference.getPersistentKey()},
                    preference.getPersistentKey());
        }
        verifyDoesNotExist(preference.getOwner(), preference.getScope(), preference.getKey());
        return saveInternal(preference, PreferencesEvent.Type.CREATED);
    }

    private PreferenceEO saveInternal(PreferenceEO preference, PreferencesEvent.Type type) {
        var saved = preferenceRepository.save(preference);
        ctx.publishEvent(new PreferencesEvent(saved, type));
        return saved;
    }

    private void verifyDoesNotExist(String owner, PropertyScope scope, String key) {
        var eoOpt = preferenceRepository.findByOwnerAndScopeAndKey(owner, scope, key);
        if (eoOpt.isPresent()) {
            throw new ResourceExistsException(translator, ALREADY_EXISTS_WITH_OWNER_AND_SCOPE_AND_KEY,
                    new Serializable[]{key, owner, scope},
                    key, owner, scope);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull PreferenceEO update(@NotBlank String pKey, @NotNull PreferenceEO preference) {
        var eo = findByPKeyInternal(pKey);
        LOGGER.debug("Overriding existing Preference [{}] with [{}]", eo, preference);
        return saveInternal(mapper.mapFromTo(preference, eo), PreferencesEvent.Type.UPDATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotBlank String pKey) {
        var eo = findByPKeyInternal(pKey);
        preferenceRepository.delete(eo);
        ctx.publishEvent(new PreferencesEvent(eo, PreferencesEvent.Type.DELETED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void reloadInitialPreferences() {
        var fromFile = fileDao.findAll();
        var persistedPrefs = preferenceRepository.findAll().stream()
                .collect(Collectors.toMap(PreferenceEO::getPrefKey, p -> p));
        for (var pref : fromFile) {
            if (!persistedPrefs.containsKey(pref.getPrefKey())) {
                saveInternal(mapper.map(pref, PreferenceEO.class), PreferencesEvent.Type.CREATED);
            }
        }
    }
}