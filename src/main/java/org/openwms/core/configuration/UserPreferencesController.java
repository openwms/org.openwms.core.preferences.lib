/*
 * Copyright 2005-2022 the original author or authors.
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

import org.ameba.exception.NotFoundException;
import org.ameba.http.MeasuredRestController;
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.openwms.core.http.AbstractWebController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.ArrayList;

import static org.openwms.core.configuration.PreferencesConstants.NOT_FOUND_BY_OWNER_AND_SCOPE_AND_KEY;
import static org.openwms.core.configuration.api.PreferencesApi.API_PREFERENCES;

/**
 * A UserPreferencesController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class UserPreferencesController extends AbstractWebController {

    private final PreferencesService preferencesService;
    private final Translator translator;
    private final BeanMapper mapper;

    public UserPreferencesController(PreferencesService preferencesService, Translator translator, BeanMapper mapper) {
        this.preferencesService = preferencesService;
        this.translator = translator;
        this.mapper = mapper;
    }

    @GetMapping(value = API_PREFERENCES, params = "user")
    public Flux<UserPreferenceVO> findByUser(
            @RequestParam("user") String user
    ) {
        return Flux.fromIterable(
                mapper.map(
                        new ArrayList<>(preferencesService.findForOwnerAndScope(user, PropertyScope.USER)),
                        UserPreferenceVO.class)
                ).log();
    }

    @GetMapping(value = API_PREFERENCES, params = {"user", "key"})
    public ResponseEntity<Mono<UserPreferenceVO>> findByUserAndKey(
            @RequestParam("user") String user,
            @RequestParam("key") String key
    ) {
        return ResponseEntity.ok(Mono.just(
                mapper.map(
                        preferencesService.findForOwnerAndScopeAndKey(user, PropertyScope.USER, key)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                translator,
                                                NOT_FOUND_BY_OWNER_AND_SCOPE_AND_KEY,
                                                new Serializable[]{key, user, PropertyScope.USER}, key, user, PropertyScope.USER
                                        )
                                ),
                        UserPreferenceVO.class
                )
        ));
    }
}
