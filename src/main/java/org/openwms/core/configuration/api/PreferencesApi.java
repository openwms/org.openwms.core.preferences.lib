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
package org.openwms.core.configuration.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.openwms.core.configuration.CoreConstants.API_PREFERENCES;

/**
 * A PreferencesApi defines the public REST API to manage preferences.
 *
 * @author Heiko Scherrer
 */
@FeignClient(name = "preferences-service", qualifier = "preferencesApi")
public interface PreferencesApi {

    /**
     * Find and return all existing preferences.
     *
     * @return An infinite stream of all PreferenceVO
     */
    @GetMapping(value = API_PREFERENCES)
    Flux<PreferenceVO> findAll();

    /**
     * Find and return all existing UserPreferences that belong to an {@code user}.
     *
     * @param user The owning user of the preferences
     * @return An infinite filtered stream of AbstractPreferences
     */
    @GetMapping(value = API_PREFERENCES, params = {"user"})
    <T extends PreferenceVO<T>> Flux<T> findBy(
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
    <T extends PreferenceVO<T>> Mono<T> findBy(
            @RequestParam("user") String user,
            @RequestParam("key") String key
    );

    /**
     * Update the content of an existing preference identified by its persistent key.
     *
     * @param pKey The persistent key of the preference to update
     * @param preference The content to update the preference with
     */
    @PutMapping(value = API_PREFERENCES + "/{pKey}")
    <T extends PreferenceVO<T>> ResponseEntity<Void> update(
            @PathVariable("pKey") String pKey,
            @RequestBody T preference
    );
}
