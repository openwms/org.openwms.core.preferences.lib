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
package org.openwms.core.configuration;

import org.openwms.core.configuration.file.AbstractPreference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.openwms.core.CoreConstants.API_CONFIGURATIONS;

/**
 * A ConfigurationController.
 *
 * @author Heiko Scherrer
 */
@RestController
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping(value= API_CONFIGURATIONS/*, produces= MediaType.APPLICATION_OCTET_STREAM_VALUE*/)
    public Flux<AbstractPreference> findAll() {
        return Flux.fromIterable(configurationService.findAll()).log();
    }

    @GetMapping(value= API_CONFIGURATIONS, params = {"owner"})
    public Flux<AbstractPreference> findBy(
            @RequestParam("owner") String owner) {
        return Flux.fromIterable(configurationService.findByType(AbstractPreference.class, owner)).log();
    }

    @GetMapping(value= API_CONFIGURATIONS, params = {"owner", "type"})
    public Flux<AbstractPreference> findBy(
            @RequestParam("owner") String owner,
            @RequestParam("type") PropertyScope type) {
        return Flux.fromIterable(configurationService.findByType()).log();
    }

    @GetMapping(value= API_CONFIGURATIONS, params = {"owner", "type", "key"})
    public Mono<AbstractPreference> findBy(
            @RequestParam("owner") String owner,
            @RequestParam("type") PropertyScope type,
            @RequestParam("key") String key) {
        return Flux.fromIterable(configurationService.findByType()).log();
    }
}
