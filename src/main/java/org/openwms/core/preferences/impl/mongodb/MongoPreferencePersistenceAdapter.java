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
package org.openwms.core.preferences.impl.mongodb;

import org.openwms.core.preferences.Preference;
import org.openwms.core.preferences.PropertyScope;
import org.openwms.core.preferences.impl.PreferencePersistencePort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * A MongoPreferencePersistenceAdapter implements {@link PreferencePersistencePort} using MongoDB.
 *ASYNCHRONOUS,DISTRIBUTED,LOCAL,
 * @author Heiko Scherrer
 */
@Profile("MONGODB")
@Component
class MongoPreferencePersistenceAdapter implements PreferencePersistencePort {

    private final PreferenceDocumentRepository repository;
    private final PreferenceDocumentMapper mapper;

    MongoPreferencePersistenceAdapter(PreferenceDocumentRepository repository, PreferenceDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Preference> findAll() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public Optional<Preference> findBypKey(String pKey) {
        return repository.findById(pKey).map(mapper::toDomain);
    }

    @Override
    public List<Preference> findByOwnerAndScope(String owner, PropertyScope scope) {
        return mapper.toDomainList(repository.findByOwnerAndScope(owner, scope));
    }

    @Override
    public Optional<Preference> findByOwnerAndScopeAndKey(String owner, PropertyScope scope, String key) {
        return repository.findByOwnerAndScopeAndKey(owner, scope, key).map(mapper::toDomain);
    }

    @Override
    public List<Preference> findByOwnerAndScopeAndGroupName(String owner, PropertyScope scope, String groupName) {
        return mapper.toDomainList(repository.findByOwnerAndScopeAndGroupName(owner, scope, groupName));
    }

    @Override
    public Preference save(Preference preference) {
        PreferenceDocument doc;
        if (preference.hasPersistentKey()) {
            var existingOpt = repository.findById(preference.getPersistentKey());
            if (existingOpt.isPresent()) {
                doc = mapper.updateDocument(preference, existingOpt.get());
            } else {
                doc = mapper.toDocument(preference);
            }
        } else {
            doc = mapper.toDocument(preference);
        }
        return mapper.toDomain(repository.save(doc));
    }

    @Override
    public void delete(Preference preference) {
        if (preference.hasPersistentKey()) {
            repository.deleteById(preference.getPersistentKey());
        }
    }
}
