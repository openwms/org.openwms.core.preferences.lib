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

import java.util.List;

/**
 * A PreferenceRepositoryCustom defines additional custom methods to search preferences by class type.
 *
 * @author Heiko Scherrer
 */
interface PreferenceRepositoryCustom {

    /**
     * Find and return all preferences that are of the given {@code clazz} type.
     *
     * @param clazz A subclass of {@link PreferenceEO} to search for
     * @param <T> Any type of {@link PreferenceEO}
     * @return A list of all preferences or an empty list, never {@literal null}
     */
    <T extends PreferenceEO> List<T> findByType(Class<T> clazz);
}
