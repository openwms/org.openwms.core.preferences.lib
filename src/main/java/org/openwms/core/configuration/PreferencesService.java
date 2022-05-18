/*
 * Copyright 2005-2022 the original author or authors.
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

import org.openwms.core.configuration.impl.jpa.PreferenceEO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

/**
 * A PreferencesService is responsible to manage {@code Preferences}. Whereby {@code Preferences} have particular defined scopes, e.g. some
 * are defined in global scope which means they are visible and valid for the whole application. Others are only valid in a certain
 * restricted scope, probably only visible for a particular {@code Module}, {@code Role} or {@code User}.
 *
 * @author Heiko Scherrer
 * @see PropertyScope
 */
public interface PreferencesService {

    /**
     * Find and return the {@code Preferences} identified by the {@code pKey}.
     *
     * @param pKey The persistent identifier
     * @return The instance, never {@literal null}
     * @throws org.ameba.exception.NotFoundException If the instance does not exist
     */
    @NotNull PreferenceEO findByPKey(@NotBlank String pKey);

    /**
     * Find and return all {@code Preferences}.
     *
     * @return A Collection of Preferences, never {@literal null}
     */
    @NotNull Collection<PreferenceEO> findAll();

    /**
     * Find and return all {@code Preferences} of a specific {@code scope} that belong to the given {@code owner}.
     *
     * @param owner The owner of the Preference
     * @param scope What kind of Preference it is
     * @return A Collection of Preferences, never {@literal null}
     */
    @NotNull Collection<PreferenceEO> findForOwnerAndScope(@NotBlank String owner, @NotNull PropertyScope scope);

    /**
     * Find and return the {@code Preference} of a specific {@code scope} that belongs to the given {@code owner} and has the given
     * {@code key}.
     *
     * @param owner The owner of the Preference
     * @param scope What kind of Preference it is
     * @param key The Preference key
     * @return A Collection of Preferences, never {@literal null}
     */
    Optional<PreferenceEO> findForOwnerAndScopeAndKey(String owner, @NotNull PropertyScope scope, @NotBlank String key);

    /**
     * Create a new non-existing {@code Preference}.
     *
     * @param preference The instance to create
     * @return The created instance
     * @throws org.ameba.exception.ResourceExistsException in case the passed Preference already exists
     */
    @NotNull PreferenceEO create(@NotNull PreferenceEO preference);

    /**
     * Update the given and existing {@code Preference}.
     *
     * @param pKey The persistent identifier of the Preference to save
     * @param preference The Preference instance to save
     * @return Saved instance
     * @throws org.ameba.exception.NotFoundException In case the Preferences does not exist
     */
    @NotNull PreferenceEO update(@NotBlank String pKey, @NotNull PreferenceEO preference);

    /**
     * Delete an existing {@code Preference}.
     *
     * @param pKey The persistent identifier of the Preference to delete.
     */
    void delete(@NotBlank String pKey);

    /**
     * Load {@code Preferences} from file and merge them with the ones in the persistent store.
     */
    void reloadInitialPreferences();
}
