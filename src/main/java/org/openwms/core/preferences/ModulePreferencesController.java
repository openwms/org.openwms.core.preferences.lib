/*
 * Copyright 2005-2026 the original author or authors.
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
package org.openwms.core.preferences;

import jakarta.validation.constraints.NotBlank;
import org.ameba.exception.NotFoundException;
import org.ameba.http.MeasuredRestController;
import org.ameba.i18n.Translator;
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.preferences.api.ModulePreferenceVO;
import org.openwms.core.preferences.api.PreferencesApi;
import org.openwms.core.preferences.api.PreferencesConstants;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A ModulePreferencesController.
 *
 * @author Heiko Scherrer
 */
@Validated
@MeasuredRestController
public class ModulePreferencesController extends AbstractWebController {

    private final PreferencesService preferencesService;
    private final Translator translator;
    private final PreferenceVOMapper preferenceVOMapper;

    public ModulePreferencesController(MessageSource messageSource, PreferencesService preferencesService, Translator translator,
            PreferenceVOMapper preferenceVOMapper) {
        super(messageSource);
        this.preferencesService = preferencesService;
        this.translator = translator;
        this.preferenceVOMapper = preferenceVOMapper;
    }

    @GetMapping(value = PreferencesApi.API_PREFERENCES, params = "module")
    public ResponseEntity<List<ModulePreferenceVO>> findByModule(
            @RequestParam("module") @NotBlank String module
    ) {
        return ResponseEntity.ok(
                preferenceVOMapper.toModuleVOList(new ArrayList<>(preferencesService.findForOwnerAndScope(module, PropertyScope.MODULE)))
        );
    }

    @GetMapping(value = PreferencesApi.API_PREFERENCES, params = {"module", "key"})
    public ResponseEntity<ModulePreferenceVO> findByModuleAndKey(
            @RequestParam("module") @NotBlank String module,
            @RequestParam("key") @NotBlank String key
    ) {
        return ResponseEntity.ok(
                preferenceVOMapper.toModuleVO(
                        preferencesService.findForOwnerAndScopeAndKey(module, PropertyScope.MODULE, key)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                translator,
                                                PreferencesConstants.NOT_FOUND_BY_OWNER_AND_SCOPE_AND_KEY,
                                                new Serializable[]{key, module, PropertyScope.MODULE}, key, module, PropertyScope.MODULE
                                        )
                                )
                )
        );
    }
}
