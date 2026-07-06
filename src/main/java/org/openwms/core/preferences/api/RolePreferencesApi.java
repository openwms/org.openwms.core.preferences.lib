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

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.openwms.core.preferences.api.PreferencesApi.API_PREFERENCES;

/**
 * A RolePreferencesApi defines the public REST API to manage RolePreferences.
 *
 * @author Heiko Scherrer
 */
@FeignClient(name = "preferences-service", qualifiers = "rolePreferencesApi")
public interface RolePreferencesApi {

    /**
     * Find and return all existing RolePreferences that belong to a {@code role}.
     *
     * @param role The owning role of the preferences
     * @return A list of RolePreferenceVO
     */
    @GetMapping(value = API_PREFERENCES, params = {"role"})
    List<RolePreferenceVO> findByRole(
            @RequestParam("role") String role
    );

    /**
     * Find and return one RolePreference that belongs to a {@code role} and has the given {@code key}.
     *
     * @param role The owning role of the preference
     * @param key The key of the preference
     * @return One single instance
     */
    @GetMapping(value = API_PREFERENCES, params = {"role", "key"})
    RolePreferenceVO findByRoleAndKey(
            @RequestParam("role") String role,
            @RequestParam("key") String key
    );
}
