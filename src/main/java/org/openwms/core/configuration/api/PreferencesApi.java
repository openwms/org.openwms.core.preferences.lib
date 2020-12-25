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

import org.openwms.core.configuration.PropertyScope;
import org.openwms.core.configuration.impl.file.GenericPreference;
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
     * @return An infinite stream of all AbstractPreferences
     */
    @GetMapping(value = API_PREFERENCES)
    Flux<GenericPreference> findAll();

    /**
     * Find and return all existing preferences that belong to one {@code owner}.
     *
     * @param owner The owner of the preferences
     * @return An infinite filtered stream of AbstractPreferences
     */
    @GetMapping(value = API_PREFERENCES, params = {"owner"})
    <T extends AbstractPreferenceVO<T>> Flux<T> findBy(
            @RequestParam("owner") String owner
    );

    /**
     * Find and return all existing preferences that belong to one {@code owner} and are of one particular {@code type}.
     *
     * @param owner The owner of the preferences
     * @param type The type of the preferences
     * @return An infinite filtered stream of AbstractPreferences
     */
    @GetMapping(value = API_PREFERENCES, params = {"owner", "type"})
    <T extends AbstractPreferenceVO<T>> Flux<T> findBy(
            @RequestParam("owner") String owner,
            @RequestParam("type") PropertyScope type
    );

    /**
     * Find and return one preference that belongs to one {@code owner} and is of one particular {@code type} and has the given {@code key}.
     *
     * @param owner The owner of the preference
     * @param type The type of the preference
     * @param key The key of the preference
     * @return One single instance
     */
    @GetMapping(value = API_PREFERENCES, params = {"owner", "type", "key"})
    <T extends AbstractPreferenceVO<T>> Mono<T> findBy(
            @RequestParam("owner") String owner,
            @RequestParam("type") PropertyScope type,
            @RequestParam("key") String key
    );

    /**
     * Update the content of an existing preference identified by its persistent key.
     *
     * @param pKey The persistent key of the preference to update
     * @param preference The content to update the preference with
     */
    @PutMapping(value = API_PREFERENCES + "/{pKey}")
    <T extends AbstractPreferenceVO<T>> ResponseEntity<Void> update(
            @PathVariable("pKey") String pKey,
            @RequestBody T preference
    );
}
