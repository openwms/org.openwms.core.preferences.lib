/*
 * Copyright 2005-2021 the original author or authors.
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
import org.openwms.core.configuration.impl.jpa.PreferenceEO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Flux<PreferenceVO> findAll() {
        return Flux.fromIterable(mapper.map(new ArrayList(preferencesService.findAll()), PreferenceVO.class)).log();
    }

    @GetMapping(value = API_PREFERENCES + "/{pKey}")
    public ResponseEntity<Mono<PreferenceVO>> findByPKey(
            @PathVariable("pKey") String pKey
    ) {
        return ResponseEntity.ok(Mono.just(mapper.map(preferencesService.findBy(pKey), PreferenceVO.class)));
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
