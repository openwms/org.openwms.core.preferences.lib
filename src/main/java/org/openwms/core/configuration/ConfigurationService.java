/*
 * Copyright 2005-2019 the original author or authors.
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
package org.openwms.core.configuration;

import org.openwms.core.configuration.file.GenericPreference;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A ConfigurationService is responsible to deal with preferences. Whereby preferences have particular defined scopes, e.g. some preferences
 * are in a global scope which means they are visible and valid for the whole application. Others are only valid in a certain scope,
 * probably only visible for a particular {@code Module}, {@code Role} or{@code User}. Other subclasses of {@link GenericPreference}
 * may be implemented as well.
 *
 * @author Heiko Scherrer
 * @see GenericPreference
 * @see PropertyScope
 */
public interface ConfigurationService {

    /**
     * Find and return all preferences. The order of elements is not guaranteed and is specific to the implementation.
     *
     * @return A Collection of all preferences
     */
    Collection<GenericPreference> findAll();

    /**
     * Find and return all preferences in the scope of a specific type of Preference and of an owner.
     *
     * @param <T> Any subtype of {@link GenericPreference}
     * @param clazz The class of preference to search for
     * @return A Collection of preferences of type T
     */
    <T extends GenericPreference> Collection<T> findByType(Class<T> clazz, String owner);

    /**
     * Save the given {@link GenericPreference} or persist it when it is a transient instance.
     *
     * @param <T> Any subtype of {@link GenericPreference}
     * @param preference {@link GenericPreference} entity to save
     * @return Saved {@link GenericPreference} entity instance
     */
    <T extends GenericPreference> T save(@NotNull T preference);

    /**
     * Delete an {@link GenericPreference}.
     *
     * @param preference The {@link GenericPreference} to delete
     */
    void delete(@NotNull GenericPreference preference);
}
