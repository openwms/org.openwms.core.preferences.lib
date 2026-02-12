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
package org.openwms.core.preferences.impl.jpa;

import org.openwms.core.preferences.Preference;
import org.openwms.core.preferences.PropertyScope;
import org.openwms.core.preferences.impl.PreferencePersistencePort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * A JpaPreferencePersistenceAdapter implements {@link PreferencePersistencePort} using JPA.
 *
 * @author Heiko Scherrer
 */
@Profile("!MONGODB")
@Component
class JpaPreferencePersistenceAdapter implements PreferencePersistencePort {

    private final PreferenceRepository preferenceRepository;
    private final PreferenceEOMapper mapper;

    JpaPreferencePersistenceAdapter(PreferenceRepository preferenceRepository, PreferenceEOMapper mapper) {
        this.preferenceRepository = preferenceRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Preference> findAll() {
        return mapper.toDomainList(preferenceRepository.findAll());
    }

    @Override
    public Optional<Preference> findBypKey(String pKey) {
        return preferenceRepository.findBypKey(pKey).map(mapper::toDomain);
    }

    @Override
    public List<Preference> findByOwnerAndScope(String owner, PropertyScope scope) {
        return mapper.toDomainList(preferenceRepository.findByOwnerAndScope(owner, scope));
    }

    @Override
    public Optional<Preference> findByOwnerAndScopeAndKey(String owner, PropertyScope scope, String key) {
        return preferenceRepository.findByOwnerAndScopeAndKey(owner, scope, key).map(mapper::toDomain);
    }

    @Override
    public List<Preference> findByOwnerAndScopeAndGroupName(String owner, PropertyScope scope, String groupName) {
        return mapper.toDomainList(preferenceRepository.findByOwnerAndScopeAndGroupName(owner, scope, groupName));
    }

    @Override
    public Preference save(Preference preference) {
        PreferenceEO eo;
        if (preference.hasPersistentKey()) {
            var existingOpt = preferenceRepository.findBypKey(preference.getPersistentKey());
            if (existingOpt.isPresent()) {
                eo = mapper.updateEntity(preference, existingOpt.get());
            } else {
                eo = mapper.toEntity(preference);
            }
        } else {
            eo = mapper.toEntity(preference);
        }
        return mapper.toDomain(preferenceRepository.save(eo));
    }

    @Override
    public void delete(Preference preference) {
        if (preference.hasPersistentKey()) {
            preferenceRepository.findBypKey(preference.getPersistentKey())
                    .ifPresent(preferenceRepository::delete);
        }
    }
}
