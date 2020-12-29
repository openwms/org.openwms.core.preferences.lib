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

import org.ameba.http.MeasuredRestController;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

import static org.openwms.core.configuration.CoreConstants.API_PREFERENCES;

/**
 * A PreferencesController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class PreferencesController {

    private final PreferencesService preferencesService;
    private final BeanMapper mapper;

    public PreferencesController(PreferencesService preferencesService, BeanMapper mapper) {
        this.preferencesService = preferencesService;
        this.mapper = mapper;
    }

    @GetMapping(value = API_PREFERENCES, params = "user")
    public Flux<UserPreferenceVO> findBy(
            @RequestParam("user") String user
    ) {
        return Flux.fromIterable(mapper.map(new ArrayList(preferencesService.findAll(user)), UserPreferenceVO.class)).log();
    }
/*
    @GetMapping(value = API_PREFERENCES, headers = HEADER_VALUE_X_TENANT, params = "key")
    public <T extends AbstractPreferenceVO<T>> Flux<T> findBy(
            @RequestHeader(HEADER_VALUE_X_TENANT) String tenant,
            @RequestParam("key") String key
    ) {
        return Flux.fromIterable(mapper.eosToVos(new ArrayList(preferencesService.findBy(AbstractPreferenceEO.class, tenant, key)))).log();
    }
*/}
