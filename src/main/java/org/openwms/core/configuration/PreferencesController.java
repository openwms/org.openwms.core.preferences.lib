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

import org.ameba.exception.ResourceExistsException;
import org.ameba.http.MeasuredRestController;
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.configuration.api.ApplicationPreferenceVO;
import org.openwms.core.configuration.api.ModulePreferenceVO;
import org.openwms.core.configuration.api.PreferenceVO;
import org.openwms.core.configuration.api.RolePreferenceVO;
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.openwms.core.configuration.impl.PreferenceVOConverter;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.http.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.openwms.core.configuration.api.PreferenceVO.MEDIA_TYPE;
import static org.openwms.core.configuration.api.PreferencesApi.API_PREFERENCES;
import static org.openwms.core.configuration.api.PreferencesConstants.ALREADY_EXISTS_WITH_OWNER_AND_SCOPE_AND_KEY;
import static org.openwms.core.configuration.api.PreferencesConstants.NOT_ALLOWED_PKEY;
import static org.openwms.core.configuration.api.PreferencesConstants.PROPERTY_SCOPE_NOT_DEFINED;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A PreferencesController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class PreferencesController extends AbstractWebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesController.class);
    private final PreferencesService preferencesService;
    private final Translator translator;
    private final BeanMapper mapper;

    public PreferencesController(PreferencesService preferencesService, Translator translator, BeanMapper mapper) {
        this.preferencesService = preferencesService;
        this.translator = translator;
        this.mapper = mapper;
    }

    @GetMapping(API_PREFERENCES + "/index")
    public ResponseEntity<Index> index() {
        return ResponseEntity.ok(
                new Index(
                        linkTo(methodOn(PreferencesController.class).findAll()).withRel("preferences-findall"),
                        linkTo(methodOn(PreferencesController.class).findByPKey("pKey")).withRel("preferences-findbypkey"),
                        linkTo(methodOn(PreferencesController.class).findAllOfScope("{scope}")).withRel("preferences-findallofscope"),
                        linkTo(methodOn(PreferencesController.class).create(new PreferenceVO(), false)).withRel("preferences-create"),
                        linkTo(methodOn(PreferencesController.class).update("pKey", new PreferenceVO())).withRel("preferences-update"),
                        linkTo(methodOn(PreferencesController.class).delete("pKey")).withRel("preferences-delete"),
                        linkTo(methodOn(UserPreferencesController.class).findByUser("user")).withRel("user-preferences-findbyuser"),
                        linkTo(methodOn(UserPreferencesController.class).findByUserAndKey("user", "key")).withRel("user-preferences-findbyuserandkey")
                )
        );
    }

    @GetMapping(value = API_PREFERENCES, produces = MEDIA_TYPE)
    public ResponseEntity<List<PreferenceVO>> findAll() {
        return ResponseEntity.ok(
                mapper.map(new ArrayList<>(preferencesService.findAll()), PreferenceVO.class)
        );
    }

    @GetMapping(value = API_PREFERENCES, params = "scope")
    public ResponseEntity<List<PreferenceVO>> findAllOfScope(
            @RequestParam("scope") String scope
    ) {
        PropertyScope propertyScope;
        try {
            propertyScope = PropertyScope.valueOf(scope);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(translator.translate(PROPERTY_SCOPE_NOT_DEFINED, new String[]{scope}, scope));
        }
        List<PreferenceVO> result = mapper.map(
                new ArrayList<>(preferencesService.findForOwnerAndScope(null, propertyScope)),
                PreferenceVO.class
        );
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = API_PREFERENCES + "/{pKey}")
    public ResponseEntity<PreferenceVO> findByPKey(
            @PathVariable("pKey") String pKey
    ) {
        var result = mapper.map(preferencesService.findByPKey(pKey), PreferenceVO.class);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, result.getContentType()).body(result);
    }

    @Transactional
    @PostMapping(value = API_PREFERENCES)
    public ResponseEntity<PreferenceVO> create(
            @RequestBody PreferenceVO preference,
            @RequestParam(value = "strict", required = false) Boolean strict
    ) {
        PreferenceEO result;
        if (!strict) {
            var existingPrefOpt = preferencesService.findForOwnerAndScopeAndKey(
                    preference.getOwner(), PreferenceVOConverter.resolveScope(preference), preference.getKey()
            );
            if (existingPrefOpt.isPresent()) {
                if (!existingPrefOpt.get().getPersistentKey().equals(preference.getpKey())) {
                    LOGGER.warn("The preference to create already exists, strict-mode allows updates but the persistent keys are not the same");
                }
                result = preferencesService.update(
                        existingPrefOpt.get().getPersistentKey(),
                        mapper.map(preference, PreferenceEO.class)
                );
                var vo = mapper.map(result, PreferenceVO.class);
                return ResponseEntity
                        .created(linkTo(methodOn(PreferencesController.class).findByPKey(result.getPersistentKey())).toUri())
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(vo);
            }
        }
        if (preference.hasPKey()) {
            throw new IllegalArgumentException(translator.translate(NOT_ALLOWED_PKEY, preference.getpKey()));
        }
        ensurePreferenceNotExists(preference);
        result = preferencesService.create(mapper.map(preference, PreferenceEO.class));
        var vo = mapper.map(result, PreferenceVO.class);
        return ResponseEntity
                .created(linkTo(methodOn(PreferencesController.class).findByPKey(result.getPersistentKey())).toUri())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(vo);
    }

    private void ensurePreferenceNotExists(PreferenceVO preference) {
        var scope = switch (preference) {
            case UserPreferenceVO ignored -> PropertyScope.USER;
            case RolePreferenceVO ignored -> PropertyScope.ROLE;
            case ModulePreferenceVO ignored -> PropertyScope.MODULE;
            case ApplicationPreferenceVO ignored -> PropertyScope.APPLICATION;
            case null, default -> throw new IllegalArgumentException("Not implemented Preference type");
        };
        if (preferencesService.existsForOwnerAndScopeAndKey(preference.getOwner(), scope, preference.getKey())) {
            throw new ResourceExistsException(
                    translator,
                    ALREADY_EXISTS_WITH_OWNER_AND_SCOPE_AND_KEY,
                    new Serializable[]{preference.getKey(), preference.getOwner(), scope},
                    preference.getKey(), preference.getOwner(), scope
            );
        }
    }

    @PutMapping(API_PREFERENCES + "/{pKey}")
    public ResponseEntity<PreferenceVO> update(
            @PathVariable("pKey") String pKey,
            @RequestBody PreferenceVO preference
    ) {
        var vo = mapper.map(preferencesService.update(pKey, mapper.map(preference, PreferenceEO.class)), PreferenceVO.class);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, vo.getContentType())
                .body(vo);
    }

    @DeleteMapping(API_PREFERENCES + "/{pKey}")
    ResponseEntity<Void> delete(
            @PathVariable("pKey") String pKey
    ) {
        preferencesService.delete(pKey);
        return ResponseEntity.noContent().build();
    }
}
