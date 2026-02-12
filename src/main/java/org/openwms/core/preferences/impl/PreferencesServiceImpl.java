/*
 * Copyright 2005-2026 the original author or authors.
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
package org.openwms.core.preferences.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.ameba.annotation.Measured;
import org.ameba.annotation.TxService;
import org.ameba.exception.NotFoundException;
import org.ameba.exception.ResourceExistsException;
import org.ameba.i18n.Translator;
import org.openwms.core.preferences.NotAuthorizedException;
import org.openwms.core.preferences.Preference;
import org.openwms.core.preferences.PreferencesEvent;
import org.openwms.core.preferences.PreferencesService;
import org.openwms.core.preferences.PropertyScope;
import org.openwms.core.preferences.impl.file.FilePreferenceMapper;
import org.openwms.core.preferences.impl.file.PreferenceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.openwms.core.preferences.api.PreferencesConstants.ALREADY_EXISTS;
import static org.openwms.core.preferences.api.PreferencesConstants.ALREADY_EXISTS_WITH_OWNER_AND_SCOPE_AND_KEY;
import static org.openwms.core.preferences.api.PreferencesConstants.NOT_ALLOWED_FETCH_USER_PREFS;
import static org.openwms.core.preferences.api.PreferencesConstants.NOT_FOUND_BY_PKEY;

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
    private final PreferencePersistencePort persistencePort;
    private final FilePreferenceMapper filePreferenceMapper;
    private final Translator translator;
    private final ApplicationContext ctx;

    PreferencesServiceImpl(PreferenceDao fileDao, PreferencePersistencePort persistencePort, FilePreferenceMapper filePreferenceMapper, Translator translator, ApplicationContext ctx) {
        this.fileDao = fileDao;
        this.persistencePort = persistencePort;
        this.filePreferenceMapper = filePreferenceMapper;
        this.translator = translator;
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Preference findByPKey(@NotBlank String pKey) {
        return findByPKeyInternal(pKey);
    }

    private Preference findByPKeyInternal(String pKey) {
        return persistencePort.findBypKey(pKey).orElseThrow(
                () -> new NotFoundException(translator, NOT_FOUND_BY_PKEY, new String[]{pKey}, pKey)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Collection<Preference> findAll() {
        return persistencePort.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Collection<Preference> findForOwnerAndScope(String owner, @NotNull PropertyScope scope) {
        ensureUserPreferenceAccess(owner, scope);
        var result = persistencePort.findByOwnerAndScope(owner, scope);
        return result == null ? Collections.emptyList() : result;
    }

    private void ensureUserPreferenceAccess(String owner, PropertyScope scope) {
        if ((owner == null || owner.isEmpty()) && scope == PropertyScope.USER) {
            throw new NotAuthorizedException(translator, NOT_ALLOWED_FETCH_USER_PREFS, new String[0]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Optional<Preference> findForOwnerAndScopeAndKey(String owner, @NotNull PropertyScope scope, @NotBlank String key) {
        ensureUserPreferenceAccess(owner, scope);
        return persistencePort.findByOwnerAndScopeAndKey(owner, scope, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public List<Preference> findForScopeOwnerGroupName(String owner, @NotNull PropertyScope scope, @NotBlank String groupName) {
        ensureUserPreferenceAccess(owner, scope);
        var result = persistencePort.findByOwnerAndScopeAndGroupName(owner, scope, groupName);
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public boolean existsForOwnerAndScopeAndKey(String owner, @NotNull PropertyScope scope, @NotBlank String key) {
        return persistencePort.findByOwnerAndScopeAndKey(owner, scope, key).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Preference create(@NotNull Preference preference) {
        if (preference.hasPersistentKey()) {
            throw new ResourceExistsException(translator, ALREADY_EXISTS,
                    new String[]{preference.getPersistentKey()},
                    preference.getPersistentKey());
        }
        verifyDoesNotExist(preference.getOwner(), preference.getScope(), preference.getKey());
        return saveInternal(preference, PreferencesEvent.Type.CREATED);
    }

    private Preference saveInternal(Preference preference, PreferencesEvent.Type type) {
        var saved = persistencePort.save(preference);
        ctx.publishEvent(new PreferencesEvent(saved, type));
        return saved;
    }

    private void verifyDoesNotExist(String owner, PropertyScope scope, String key) {
        var opt = persistencePort.findByOwnerAndScopeAndKey(owner, scope, key);
        if (opt.isPresent()) {
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
    public @NotNull Preference update(@NotBlank String pKey, @NotNull Preference preference) {
        var existing = findByPKeyInternal(pKey);
        LOGGER.debug("Overriding existing Preference [{}] with [{}]", existing, preference);
        preference.setPKey(existing.getPersistentKey());
        return saveInternal(preference, PreferencesEvent.Type.UPDATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotBlank String pKey) {
        var existing = findByPKeyInternal(pKey);
        persistencePort.delete(existing);
        ctx.publishEvent(new PreferencesEvent(existing, PreferencesEvent.Type.DELETED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void reloadInitialPreferences() {
        var fromFile = fileDao.findAll();
        var persistedPrefs = persistencePort.findAll().stream()
                .collect(Collectors.toMap(Preference::getPrefKey, p -> p));
        for (var pref : fromFile) {
            if (!persistedPrefs.containsKey(pref.getPrefKey())) {
                saveInternal(filePreferenceMapper.toDomain(pref), PreferencesEvent.Type.CREATED);
            }
        }
    }
}
