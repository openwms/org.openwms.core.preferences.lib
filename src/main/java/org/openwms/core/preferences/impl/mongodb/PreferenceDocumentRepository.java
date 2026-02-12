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

import org.openwms.core.preferences.PropertyScope;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * A PreferenceDocumentRepository is a Spring Data MongoDB repository that deals with {@link PreferenceDocument}s.
 *
 * @author Heiko Scherrer
 */
@Profile("MONGODB")
interface PreferenceDocumentRepository extends MongoRepository<PreferenceDocument, String> {

    @Query("{ '$or': [ { '$and': [ { 'owner': { '$exists': false } }, { '$expr': { '$eq': [ ?0, null ] } } ] }, { 'owner': ?0 } ], 'scope': ?1 }")
    List<PreferenceDocument> findByOwnerAndScope(String owner, PropertyScope scope);

    @Query("{ '$or': [ { '$and': [ { 'owner': { '$exists': false } }, { '$expr': { '$eq': [ ?0, null ] } } ] }, { 'owner': ?0 } ], 'scope': ?1, 'key': ?2 }")
    Optional<PreferenceDocument> findByOwnerAndScopeAndKey(String owner, PropertyScope scope, String key);

    @Query("{ '$or': [ { '$and': [ { 'owner': { '$exists': false } }, { '$expr': { '$eq': [ ?0, null ] } } ] }, { 'owner': ?0 } ], 'scope': ?1, 'groupName': ?2 }")
    List<PreferenceDocument> findByOwnerAndScopeAndGroupName(String owner, PropertyScope scope, String groupName);
}
