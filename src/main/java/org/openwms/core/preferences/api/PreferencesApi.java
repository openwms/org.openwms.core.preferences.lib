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
package org.openwms.core.preferences.api;

import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * A PreferencesApi defines the public REST API to manage preferences.
 *
 * @author Heiko Scherrer
 */
@FeignClient(name = "preferences-service", qualifiers = "preferencesApi")
public interface PreferencesApi {

    /** API version. */
    String API_VERSION = "v1";
    /** API root to hit Preferences (plural). */
    String API_PREFERENCES = "/" + API_VERSION + "/preferences";

    /**
     * Find and return all existing preferences.
     *
     * @return All existing Preferences
     */
    @Cacheable("preferences")
    @GetMapping(API_PREFERENCES)
    List<PreferenceVO> findAll();

    /**
     * Find and return a Preference identified by its persistent key.
     *
     * @param pKey The persistent identifier
     * @return The instance
     */
    @Cacheable("preferences")
    @GetMapping(value = API_PREFERENCES + "/{pKey}")
    PreferenceVO findByPKey(
            @PathVariable("pKey") String pKey
    );

    /**
     * Find and return all existing preferences with the given {@code scope}.
     *
     * @param scope The scope to search for
     * @return All existing Preferences
     */
    @Cacheable("preferences")
    @GetMapping(value = API_PREFERENCES, params = "scope")
    List<PreferenceVO> findAllOfScope(
            @RequestParam("scope") String scope
    );

    /**
     * Find and return a Preference with the given identifying attributes.
     *
     * @param owner The owner of the Preference (nullable)
     * @param scope The scope of the Preference (APPLICATION, MODULE, ROLE or USER)
     * @param key The key of the Preference
     * @return The existing Preference
     * @throws org.ameba.exception.NotFoundException If no Preference exists
     */
    @Cacheable("preferences")
    @GetMapping(value = API_PREFERENCES, params = {"scope", "key"})
    PreferenceVO findByOwnerScopeKey(
            @RequestParam(value = "owner", required = false) String owner,
            @RequestParam("scope") @NotBlank String scope,
            @RequestParam("key") @NotBlank String key
    );

    /**
     * Find and return all {@code Preference}s that belong to a group with the same {@code groupName}.
     *
     * @param owner The owner of the Preference
     * @param scope What kind of Preference it is
     * @param groupName The name of the group
     * @return All instances, never {@literal null}
     */
    @Cacheable("preferences")
    @GetMapping(value = "/preferences/groups", params = {"scope", "name"}, produces = MediaType.APPLICATION_JSON_VALUE)
    List<PreferenceVO> findForOwnerAndScopeAndGroupName(
            @RequestParam(value = "owner", required = false) String owner,
            @RequestParam("scope") @NotBlank String scope,
            @RequestParam("name") String groupName);

    /**
     * Create a new not-existing preference.
     *
     * @param preference The content to update the preference with
     */
    @PostMapping(value = API_PREFERENCES)
    void create(
            @RequestBody PreferenceVO preference
    );

    /**
     * Update the content of an existing preference identified by its persistent key.
     *
     * @param pKey The persistent key of the preference to update
     * @param preference The content to update the preference with
     */
    @PutMapping(value = API_PREFERENCES + "/{pKey}")
    PreferenceVO update(
            @PathVariable("pKey") String pKey,
            @RequestBody PreferenceVO preference
    );

    /**
     * Delete an existing preference identified by its persistent key.
     *
     * @param pKey The persistent key of the preference to update
     */
    @DeleteMapping(value = API_PREFERENCES + "/{pKey}")
    void delete(
            @PathVariable("pKey") String pKey
    );
}
