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

import org.openwms.core.configuration.PropertyScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * A PreferenceRepository is a Spring Data JPA repository that deals with {@link PreferenceEO}s.
 *
 * @author Heiko Scherrer
 */
interface PreferenceRepository extends JpaRepository<PreferenceEO, Long>, PreferenceRepositoryCustom {

    Optional<PreferenceEO> findBypKey(String pKey);

    List<PreferenceEO> findAllByOwnerAndAndScope(String owner, PropertyScope scope);

    Optional<PreferenceEO> findAllByOwnerAndAndScopeAndKey(String owner, PropertyScope scope, String key);
}