/*
 * Copyright 2005-2019 the original author or authors.
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
import org.openwms.core.configuration.file.AbstractPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.openwms.core.CoreConstants.API_CONFIGURATIONS;

/**
 * A ConfigurationApi.
 *
 * @author Heiko Scherrer
 */
@FeignClient(name = "preferences-service", qualifier = "configurationApi")
public interface ConfigurationApi {

    /**
     * Find and return all existing preferences.
     *
     * @return An infinite stream of all AbstractPreferences
     */
    @GetMapping(value= API_CONFIGURATIONS)
    Flux<AbstractPreference> findAll();

    /**
     * Find and return all existing preferences that belong to one {@code owner}.
     *
     * @param owner The owner of the preferences
     * @return An infinite filtered stream of AbstractPreferences
     */
    @GetMapping(value= API_CONFIGURATIONS, params = {"owner"})
    Flux<AbstractPreference> findBy(
            @RequestParam("owner") String owner
    );

    /**
     * Find and return all existing preferences that belong to one {@code owner} and are of one particular {@code type}.
     *
     * @param owner The owner of the preferences
     * @param type The type of the preferences
     * @return An infinite filtered stream of AbstractPreferences
     */
    @GetMapping(value= API_CONFIGURATIONS, params = {"owner", "type"})
    Flux<AbstractPreference> findBy(
            @RequestParam("owner") String owner,
            @RequestParam("type") PropertyScope type
    );

    /**
     * Find and return all existing preferences that
     * @param owner
     * @param type
     * @param key
     * @return
     */
    @GetMapping(value= API_CONFIGURATIONS, params = {"owner", "type", "key"})
    Mono<AbstractPreference> findBy(
            @RequestParam("owner") String owner,
            @RequestParam("type") PropertyScope type,
            @RequestParam("key") String key
    );
}
