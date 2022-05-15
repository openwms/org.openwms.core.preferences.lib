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
import org.openwms.core.configuration.api.ApplicationPreferenceVO;
import org.openwms.core.configuration.api.ModulePreferenceVO;
import org.openwms.core.configuration.api.PreferenceVO;
import org.openwms.core.configuration.api.RolePreferenceVO;
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.http.Index;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

import static org.openwms.core.configuration.api.PreferencesApi.API_PREFERENCES;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A PreferencesController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class PreferencesController extends AbstractWebController {

    private final PreferencesService preferencesService;
    private final BeanMapper mapper;

    public PreferencesController(PreferencesService preferencesService, BeanMapper mapper) {
        this.preferencesService = preferencesService;
        this.mapper = mapper;
    }

    @GetMapping(API_PREFERENCES + "/index")
    public ResponseEntity<Index> index() {
        return ResponseEntity.ok(
                new Index(
                        linkTo(methodOn(PreferencesController.class).findAll()).withRel("preferences-findall"),
                        linkTo(methodOn(PreferencesController.class).findByPKey("pKey")).withRel("preferences-findbypkey"),
                        linkTo(methodOn(PreferencesController.class).create(new PreferenceVO())).withRel("preferences-create"),
                        linkTo(methodOn(PreferencesController.class).update("pKey", new PreferenceVO())).withRel("preferences-update"),
                        linkTo(methodOn(PreferencesController.class).delete("pKey")).withRel("preferences-delete"),
                        linkTo(methodOn(UserPreferencesController.class).findByUser("user")).withRel("user-preferences-findbyuser"),
                        linkTo(methodOn(UserPreferencesController.class).findByUserAndKey("user", "key")).withRel("user-preferences-findbyuserandkey")
                )
        );
    }

    @GetMapping(API_PREFERENCES)
    public Flux<PreferenceVO> findAll() {
        return Flux.fromIterable(mapper.map(new ArrayList(preferencesService.findAll()), PreferenceVO.class)).log();
    }

    @GetMapping(value = API_PREFERENCES + "/{pKey}")
    public ResponseEntity<Mono<PreferenceVO>> findByPKey(
            @PathVariable("pKey") String pKey
    ) {
        return ResponseEntity.ok(Mono.just(mapper.map(preferencesService.findBy(pKey), PreferenceVO.class)));
    }

    @Transactional
    @PostMapping(API_PREFERENCES)
    public ResponseEntity<PreferenceVO> create(
            @RequestBody PreferenceVO preference
    ) {
        PreferenceEO result;
        if (preference.getpKey() != null && !preference.getpKey().isEmpty()) {
            result = preferencesService.update(preference.getpKey(), mapper.map(preference, PreferenceEO.class));
            return ResponseEntity.ok(mapper.map(result, PreferenceVO.class));
        } else {
            Optional<PreferenceEO> existingOpt = findByKey(preference);
            if (existingOpt.isPresent()) {
                var preferenceEO = existingOpt.get();
                var pKey = preferenceEO.getPersistentKey();
                mapper.mapFromTo(mapper.map(preference, PreferenceEO.class), preferenceEO);
                preferenceEO.setPersistentKey(pKey);
                result = preferencesService.save(preferenceEO);
            } else {
                result = preferencesService.create(mapper.map(preference, PreferenceEO.class));
            }
            return ResponseEntity.created(linkTo(methodOn(PreferencesController.class).findByPKey(result.getPersistentKey())).toUri())
                    .body(mapper.map(result, UserPreferenceVO.class));
        }
    }

    private Optional<PreferenceEO> findByKey(PreferenceVO preference) {
        PropertyScope scope;
        if (preference instanceof UserPreferenceVO) {
            scope = PropertyScope.USER;
        } else if (preference instanceof RolePreferenceVO) {
            scope = PropertyScope.ROLE;
        } else if (preference instanceof ModulePreferenceVO) {
            scope = PropertyScope.MODULE;
        } else if (preference instanceof ApplicationPreferenceVO) {
            scope = PropertyScope.APPLICATION;
        } else {
            throw new IllegalArgumentException("Not implemented Preference type");
        }
        return preferencesService.findBy(preference.getOwner(), scope, preference.getKey());
    }

    @PutMapping(API_PREFERENCES + "/{pKey}")
    public ResponseEntity<PreferenceVO> update(
            @PathVariable("pKey") String pKey,
            @RequestBody PreferenceVO preference
    ) {
        var saved = preferencesService.update(pKey, mapper.map(preference, PreferenceEO.class));
        return ResponseEntity.ok(mapper.map(saved, PreferenceVO.class));
    }

    @DeleteMapping(API_PREFERENCES + "/{pKey}")
    ResponseEntity<Void> delete(
            @PathVariable("pKey") String pKey
    ) {
        preferencesService.delete(pKey);
        return ResponseEntity.noContent().build();
    }
}
