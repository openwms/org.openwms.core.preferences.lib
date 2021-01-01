/*
 * Copyright 2005-2020 the original author or authors.
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

import org.openwms.core.configuration.impl.file.GenericPreference;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A PreferencesService is responsible to manage {@code Preferences}. Whereby {@code Preferences} have particular defined scopes, e.g. some
 * are defined in a global scope which means they are visible and valid for the whole application. Others are only valid in a certain
 * restricted scope, probably only visible for a particular {@code Module}, {@code Role} or{@code User}. Other subclasses of
 * {@link GenericPreference} may be implemented as well.
 *
 * @author Heiko Scherrer
 * @see GenericPreference
 * @see PropertyScope
 */
public interface PreferencesService {

    /**
     * Find and return all {@code Preferences} in the scope of a specific type of {@code Preference} and owner.
     *
     * @param owner
     * @param scope
     * @return A Collection of preferences of type T, never {@literal null}
     */
    Collection<PreferenceEO> findAll(@NotEmpty String owner, @NotNull PropertyScope scope);

    /**
     * Find and return all {@code Preferences} in the scope of a specific type of {@code Preference} and owner.
     *
     * @param <T> Any subtype of {@link GenericPreference}
     * @param clazz The class of preference to search for
     * @return A Collection of preferences of type T, never {@literal null}
     */
    PreferenceEO findBy(@NotEmpty String owner, @NotNull PropertyScope scope, @NotEmpty String key);

    /**
     * Create or update the given {@code Preference}.
     *
     * @param <T> Any subtype of {@link GenericPreference}
     * @param preference {@link GenericPreference} entity to save
     * @return Saved {@link GenericPreference} entity instance
    <T extends AbstractPreferenceEO> T save(@NotNull T preference);
     */

    /**
     * Delete an existing {@code Preference}.
     *
     * @param preference The {@link GenericPreference} to delete
    void delete(@NotNull AbstractPreferenceEO preference);
     */

    /**
     *
     */
    void reloadInitialPreferences();
}
