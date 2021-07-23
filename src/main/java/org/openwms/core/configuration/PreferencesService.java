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
package org.openwms.core.configuration;

import org.openwms.core.configuration.impl.file.GenericPreference;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

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
     * Find and return all {@code Preferences}.
     */
    Collection<PreferenceEO> findAll();

    /**
     * Find and return all {@code Preferences} in the scope of a specific type of {@code Preference} and owner.
     *
     * @param owner The owner of the preference
     * @param scope What kind of preference it is
     * @return A Collection of preferences of type T, never {@literal null}
     */
    Collection<PreferenceEO> findAll(@NotEmpty String owner, @NotNull PropertyScope scope);

    /**
     * Find and return the {@code Preferences} identified by the {@code pKey}.
     *
     * @param pKey The persistent identifier
     * @return The instance, or {@literal null}
     */
    PreferenceEO findBy(@NotEmpty String pKey);

    /**
     * Find and return all {@code Preferences} in the scope of a specific type of {@code Preference} and owner.
     *
     * @param owner The owner of the preference
     * @param scope What kind of preference it is
     * @param key The preference key
     * @return A Collection of preferences of type T, never {@literal null}
     */
    Optional<PreferenceEO> findBy(@NotEmpty String owner, @NotNull PropertyScope scope, @NotEmpty String key);

    /**
     * Create a new non-existing {@code Preference}.
     *
     * @param preference The instance to create
     * @return The created instance
     */
    PreferenceEO create(@NotNull PreferenceEO preference);

    /**
     * Update the given and existing {@code Preference}.
     *
     * @param pKey The persistent identifier of the preference to save
     * @param preference {@link PreferenceEO} instance to save
     * @return Saved {@link PreferenceEO} instance
     */
    PreferenceEO update(@NotEmpty String pKey, @NotNull PreferenceEO preference);

    /**
     * Save the given {@code Preference}.
     *
     * @param preference {@link PreferenceEO} instance to save
     * @return Saved {@link PreferenceEO} instance
     */
    PreferenceEO save(@NotNull PreferenceEO preference);

    /**
     * Delete an existing {@code Preference}.
     *
     * @param pKey The persistent identifier of the preference to delete.
     */
    void delete(@NotEmpty String pKey);

    /**
     * Load properties from file and merge them with the ones in the database.
     */
    void reloadInitialPreferences();
}
