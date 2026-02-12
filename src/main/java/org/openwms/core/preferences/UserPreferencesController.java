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
import org.openwms.core.preferences.api.PreferencesApi;
import org.openwms.core.preferences.api.PreferencesConstants;
import org.openwms.core.preferences.api.UserPreferenceVO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A UserPreferencesController.
 *
 * @author Heiko Scherrer
 */
@Validated
@MeasuredRestController
public class UserPreferencesController extends AbstractWebController {

    private final PreferencesService preferencesService;
    private final Translator translator;
    private final PreferenceVOMapper preferenceVOMapper;

    public UserPreferencesController(PreferencesService preferencesService, Translator translator, PreferenceVOMapper preferenceVOMapper) {
        this.preferencesService = preferencesService;
        this.translator = translator;
        this.preferenceVOMapper = preferenceVOMapper;
    }

    @GetMapping(value = PreferencesApi.API_PREFERENCES, params = "user")
    public ResponseEntity<List<UserPreferenceVO>> findByUser(
            @RequestParam("user") @NotBlank String user
    ) {
        return ResponseEntity.ok(
                preferenceVOMapper.toUserVOList(new ArrayList<>(preferencesService.findForOwnerAndScope(user, PropertyScope.USER)))
        );
    }

    @GetMapping(value = PreferencesApi.API_PREFERENCES, params = {"user", "key"})
    public ResponseEntity<UserPreferenceVO> findByUserAndKey(
            @RequestParam("user") @NotBlank String user,
            @RequestParam("key") @NotBlank String key
    ) {
        return ResponseEntity.ok(
                preferenceVOMapper.toUserVO(
                        preferencesService.findForOwnerAndScopeAndKey(user, PropertyScope.USER, key)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                translator,
                                                PreferencesConstants.NOT_FOUND_BY_OWNER_AND_SCOPE_AND_KEY,
                                                new Serializable[]{key, user, PropertyScope.USER}, key, user, PropertyScope.USER
                                        )
                                )
                )
        );
    }
}
