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
package org.openwms.core.configuration.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.openwms.core.configuration.api.PreferencesApi.API_PREFERENCES;

/**
 * A UserPreferencesApi defines the public REST API to manage UserPreferences.
 *
 * @author Heiko Scherrer
 */
@FeignClient(name = "preferences-service", qualifier = "userPreferencesApi")
public interface UserPreferencesApi {

    /**
     * Find and return all existing UserPreferences that belong to an {@code user}.
     *
     * @param user The owning user of the preferences
     * @return An infinite filtered stream of PreferenceVO
     */
    @GetMapping(value = API_PREFERENCES, params = {"user"})
    List<PreferenceVO> findByUser(
            @RequestParam("user") String user
    );

    /**
     * Find and return one UserPreference that belongs to an {@code user} and has the given {@code key}.
     *
     * @param user The owning user of the preference
     * @param key The key of the preference
     * @return One single instance
     */
    @GetMapping(value = API_PREFERENCES, params = {"user", "key"})
    PreferenceVO findByUserAndKey(
            @RequestParam("user") String user,
            @RequestParam("key") String key
    );

    /**
     * Create or update an UserPreference.
     *
     * @param userPreference The user preference
     * @return The saved instance
     */
    @PostMapping(value = API_PREFERENCES)
    UserPreferenceVO createOrUpdate(
            @RequestBody UserPreferenceVO userPreference
    );
}
