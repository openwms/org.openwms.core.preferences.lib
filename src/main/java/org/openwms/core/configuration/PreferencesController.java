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
import org.openwms.core.configuration.api.PreferenceVO;
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping(value = API_PREFERENCES)
    public Flux<UserPreferenceVO> findAll() {
        return Flux.fromIterable(mapper.map(new ArrayList(preferencesService.findAll()), UserPreferenceVO.class)).log();
    }

    @GetMapping(value = API_PREFERENCES, params = "user")
    public Flux<UserPreferenceVO> findByUser(
            @RequestParam("user") String user
    ) {
        return Flux.fromIterable(mapper.map(new ArrayList(preferencesService.findAll(user, PropertyScope.USER)), UserPreferenceVO.class)).log();
    }

    @GetMapping(value = API_PREFERENCES, params = {"user", "key"})
    public ResponseEntity<Mono<UserPreferenceVO>> findBy(
            @RequestParam("user") String user,
            @RequestParam("key") String key
    ) {
        PreferenceEO eo = preferencesService.findBy(user, PropertyScope.USER, key);
        if (eo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Mono.empty());
        }
        UserPreferenceVO p = mapper.map(eo, UserPreferenceVO.class);
        return ResponseEntity.ok(Mono.just(p));
    }

    @GetMapping(value = API_PREFERENCES + "/{pKey}")
    public ResponseEntity<Mono<UserPreferenceVO>> findByPKey(
            @PathVariable("pKey") String pKey
    ) {
        PreferenceEO eo = preferencesService.findBy(pKey);
        if (eo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Mono.empty());
        }
        UserPreferenceVO p = mapper.map(eo, UserPreferenceVO.class);
        return ResponseEntity.ok(Mono.just(p));
    }

    @PutMapping(value = API_PREFERENCES + "/{pKey}")
    public ResponseEntity<Void> update(
            @PathVariable("pKey") String pKey,
            @RequestBody PreferenceVO preference
    ) {
        preferencesService.save(pKey, mapper.map(preference, PreferenceEO.class));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = API_PREFERENCES + "/{pKey}")
    ResponseEntity<Void> delete(
            @PathVariable("pKey") String pKey
    ) {
        preferencesService.delete(pKey);
        return ResponseEntity.noContent().build();
    }
}
