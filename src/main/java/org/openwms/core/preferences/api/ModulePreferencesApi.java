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
 * A ModulePreferencesApi defines the public REST API to manage ModulePreferences.
 *
 * @author Heiko Scherrer
 */
@FeignClient(name = "preferences-service", qualifiers = "modulePreferencesApi")
public interface ModulePreferencesApi {

    /**
     * Find and return all existing ModulePreferences that belong to a {@code module}.
     *
     * @param module The owning module of the preferences
     * @return A list of ModulePreferenceVO
     */
    @GetMapping(value = API_PREFERENCES, params = {"module"})
    List<ModulePreferenceVO> findByModule(
            @RequestParam("module") String module
    );

    /**
     * Find and return one ModulePreference that belongs to a {@code module} and has the given {@code key}.
     *
     * @param module The owning module of the preference
     * @param key The key of the preference
     * @return One single instance
     */
    @GetMapping(value = API_PREFERENCES, params = {"module", "key"})
    ModulePreferenceVO findByModuleAndKey(
            @RequestParam("module") String module,
            @RequestParam("key") String key
    );
}
