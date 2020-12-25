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
package org.openwms.core.configuration;

import org.openwms.core.configuration.impl.file.GenericPreference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.openwms.core.configuration.CoreConstants.API_CONFIGURATIONS;

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

    @GetMapping(value = API_CONFIGURATIONS)
    public Flux<GenericPreference> findAll() {
        return Flux.fromIterable(configurationService.findAll()).log();
    }

    @GetMapping(value = API_CONFIGURATIONS, params = {"owner"})
    public Flux<GenericPreference> findBy(
            @RequestParam("owner") String owner) {
        return Flux.fromIterable(configurationService.findByType(GenericPreference.class, owner)).log();
    }
}
