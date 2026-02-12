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

import org.openwms.core.preferences.Preference;
import org.openwms.core.preferences.PropertyScope;

import java.util.List;
import java.util.Optional;

/**
 * A PreferencePersistencePort defines the persistence operations needed by the service layer, returning domain {@link Preference} objects.
 *
 * @author Heiko Scherrer
 */
public interface PreferencePersistencePort {

    List<Preference> findAll();

    Optional<Preference> findBypKey(String pKey);

    List<Preference> findByOwnerAndScope(String owner, PropertyScope scope);

    Optional<Preference> findByOwnerAndScopeAndKey(String owner, PropertyScope scope, String key);

    List<Preference> findByOwnerAndScopeAndGroupName(String owner, PropertyScope scope, String groupName);

    Preference save(Preference preference);

    void delete(Preference preference);
}
