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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * Find and return an Preference identified by its persistent key.
     *
     * @param pKey The persistent identifier
     * @return The instance
     */
    @GetMapping(value = API_PREFERENCES + "/{pKey}")
    Mono<PreferenceVO> findByPKey(
            @PathVariable("pKey") String pKey
    );

    /**
     * Update the content of an existing preference identified by its persistent key.
     *
     * @param pKey The persistent key of the preference to update
     * @param preference The content to update the preference with
     */
    @PutMapping(value = API_PREFERENCES + "/{pKey}")
    ResponseEntity<Void> update(
            @PathVariable("pKey") String pKey,
            @RequestBody PreferenceVO preference
    );

    /**
     * Delete an existing preference identified by its persistent key.
     *
     * @param pKey The persistent key of the preference to update
     */
    @DeleteMapping(value = API_PREFERENCES + "/{pKey}")
    ResponseEntity<Void> delete(
            @PathVariable("pKey") String pKey
    );
}
