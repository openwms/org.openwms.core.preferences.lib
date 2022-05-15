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
import java.util.Collection;
import java.util.Collections;
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
    private final ApplicationContext ctx;

    PreferencesServiceImpl(PreferenceDao fileDao, PreferenceRepository preferenceRepository, BeanMapper mapper, ApplicationContext ctx) {
        this.fileDao = fileDao;
        this.preferenceRepository = preferenceRepository;
        this.mapper = mapper;
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Collection<PreferenceEO> findForOwnerAndScope() {
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
    public @NotNull PreferenceEO findBy(@NotBlank String pKey) {
        return preferenceRepository.findBypKey(pKey).orElseThrow(
                () -> new NotFoundException(format("Preference with key [%s] does not exist", pKey))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Optional<PreferenceEO> findBy(@NotBlank String owner, @NotNull PropertyScope scope, @NotBlank String key) {
        return preferenceRepository.findByOwnerAndScopeAndKey(owner, scope, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull PreferenceEO create(@NotNull PreferenceEO preference) {
        if (preference.getPersistentKey() != null) {
            throw new NotFoundException(format("Preference has a pKey [%s] and cannot be created", preference.getPersistentKey()));
        }
        var eoOpt = preferenceRepository.findByOwnerAndScopeAndKey(preference.getOwner(), preference.getScope(), preference.getKey());
        if (eoOpt.isPresent()) {
            throw new ResourceExistsException(format("Preference with key [%s] and owner [%s] of scope [%s] already exists and cannot be created", preference.getKey(), preference.getOwner(), preference.getScope()));
        }
        var saved = preferenceRepository.save(preference);
        ctx.publishEvent(new PreferencesEvent(saved, PreferencesEvent.Type.CREATED));
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull PreferenceEO update(@NotBlank String pKey, @NotNull PreferenceEO preference) {
        var eoOpt = preferenceRepository.findBypKey(pKey);
        if (eoOpt.isEmpty()) {
            throw new NotFoundException(format("Preference with key [%s] does not exist", pKey));
        }
        var eo = eoOpt.get();
        var saved = preferenceRepository.save(mapper.mapFromTo(preference, eo));
        ctx.publishEvent(new PreferencesEvent(saved, PreferencesEvent.Type.UPDATED));
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull PreferenceEO save(@NotNull PreferenceEO preference) {
        var saved = preferenceRepository.save(preference);
        ctx.publishEvent(new PreferencesEvent(saved, PreferencesEvent.Type.UPDATED));
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotBlank String pKey) {
        var eoOpt = preferenceRepository.findBypKey(pKey);
        if (eoOpt.isEmpty()) {
            throw new NotFoundException(format("Preference with key [%s] does not exist", pKey));
        }
        var eo = eoOpt.get();
        preferenceRepository.delete(eo);
        ctx.publishEvent(new PreferencesEvent(eo, PreferencesEvent.Type.DELETED));
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
        var fromFile = fileDao.findAll();
        var persistedPrefs = preferenceRepository.findAll().stream()
                .collect(Collectors.toMap(PreferenceEO::getPrefKey, p -> p));
        for (var pref : fromFile) {
            if (!persistedPrefs.containsKey(pref.getPrefKey())) {
                var saved = preferenceRepository.save(mapper.map(pref, PreferenceEO.class));
                ctx.publishEvent(new PreferencesEvent(saved, PreferencesEvent.Type.CREATED));
            }
        }
    }
}