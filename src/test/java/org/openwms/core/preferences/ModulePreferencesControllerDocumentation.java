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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.preferences.api.PreferencesApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A ModulePreferencesControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@CoreApplicationTest
@Sql(scripts = "classpath:test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ModulePreferencesControllerDocumentation extends DefaultTestProfile {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)).build();
    }

    @Test
    void shall_return_all_for_module() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES)
                                .queryParam("module", "module1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", is("key5")))
                .andExpect(jsonPath("$[0].owner", is("module1")))
                .andExpect(jsonPath("$[0].@class", is("org.openwms.core.preferences.api.ModulePreferenceVO")))
                .andDo(MockMvcRestDocumentation.document("prefs-findformodule", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_return_all_for_module_and_key() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES)
                                .queryParam("module", "module1")
                                .queryParam("key", "key5")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("key5")))
                .andExpect(jsonPath("$.owner", is("module1")))
                .andExpect(jsonPath("$.@class", is("org.openwms.core.preferences.api.ModulePreferenceVO")))
                .andDo(MockMvcRestDocumentation.document("prefs-findformodulekey", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_fail_with_unknown_key() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES)
                                .queryParam("module", "module1")
                                .queryParam("key", "UNKNOWN")
                )
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("prefs-findformodulekey404", preprocessResponse(prettyPrint())))
        ;
    }
}
