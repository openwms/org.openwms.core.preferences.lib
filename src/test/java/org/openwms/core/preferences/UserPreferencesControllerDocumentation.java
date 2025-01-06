/*
 * Copyright 2005-2025 the original author or authors.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.preferences.api.PreferencesApi;
import org.openwms.core.preferences.api.UserPreferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A UserPreferencesControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@CoreApplicationTest
@SqlGroup({
        @Sql(scripts = "classpath:test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserPreferencesControllerDocumentation extends DefaultTestProfile {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)).build();
    }

    @Test
    void shall_return_all_for_user() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES)
                                .queryParam("user", "owner1")
                )
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("prefs-findforuser", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_return_all_for_user_and_key() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES)
                                .queryParam("user", "owner1")
                                .queryParam("key", "key1")
                )
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("prefs-findforuserkey", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_fail_with_unknown_key() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES)
                                .queryParam("user", "owner1")
                                .queryParam("key", "UNKNOWN")
                )
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("prefs-findforuserkey404", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_create_userpreference() throws Exception {
        var om = new ObjectMapper();
        var up = new UserPreferenceVO();
        up.setOwner("owner1");
        up.setDescription("test desc");
        up.setKey("o1-u1");
        up.setType("STRING");
        up.setVal("TEST VAL");
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PreferencesApi.API_PREFERENCES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(up))

                )
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentation.document("prefs-createup", preprocessResponse(prettyPrint())))
        ;
    }
}
