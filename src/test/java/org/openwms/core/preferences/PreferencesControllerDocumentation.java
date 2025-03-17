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
import org.openwms.core.preferences.api.PreferenceVO;
import org.openwms.core.preferences.api.PreferencesApi;
import org.openwms.core.preferences.api.UserPreferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A PreferencesControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@CoreApplicationTest
@Sql(scripts = "classpath:test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PreferencesControllerDocumentation extends DefaultTestProfile {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    void shall_return_index() throws Exception {
        mockMvc.perform(
                    get(PreferencesApi.API_PREFERENCES + "/index")
                )
                .andExpect(status().isOk())
                .andDo(document("prefs-index", preprocessResponse(prettyPrint())))
                .andExpect(jsonPath("$._links.preferences-findall").exists())
                .andExpect(jsonPath("$._links.preferences-findbypkey").exists())
                .andExpect(jsonPath("$._links.preferences-findallofscope").exists())
                .andExpect(jsonPath("$._links.preferences-create").exists())
                .andExpect(jsonPath("$._links.preferences-update").exists())
                .andExpect(jsonPath("$._links.preferences-delete").exists())
                .andExpect(jsonPath("$._links.user-preferences-findbyuser").exists())
                .andExpect(jsonPath("$._links.user-preferences-findbyuserandkey").exists())
                .andExpect(jsonPath("$._links.length()", is(9)))
        ;
    }

    @Test
    void shall_return_all_preferences() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES)
                )
                .andExpect(status().isOk())
                .andDo(document("prefs-findall",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[]").description("An array of all existing preferences"),
                                fieldWithPath("[].pKey").description("The persistent unique key"),
                                fieldWithPath("[].key").description("The business key, unique combined with the owner"),
                                fieldWithPath("[].value").description("The preference value"),
                                fieldWithPath("[].groupName").optional().description("A group the preference is assigned to"),
                                fieldWithPath("[].type").description("The preference type (FLOAT|STRING|INT|OBJECT|BOOL)"),
                                fieldWithPath("[].description").description("The descriptive text of the preference"),
                                fieldWithPath("[].owner").optional().description("The exclusive preference owner"),
                                fieldWithPath("[].@class").description("Metadata used internally to clarify the preference type")
                        )
                        ))
                .andExpect(jsonPath("$[0].pKey").exists())
                .andExpect(jsonPath("$[0].key").exists())
                .andExpect(jsonPath("$[0].value").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].@class").exists())
        ;
    }

    @Test
    void shall_return_all_of_scope() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES).queryParam("scope", "APPLICATION")
                )
                .andExpect(status().isOk())
                .andDo(document("prefs-findallofscope", preprocessResponse(prettyPrint())))
                .andExpect(jsonPath("$[0].pKey").exists())
                .andExpect(jsonPath("$[0].key").exists())
                .andExpect(jsonPath("$[0].value").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].@class").exists())
        ;
    }

    @Test
    void shall_return_all_of_scope_403() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PreferencesApi.API_PREFERENCES).queryParam("scope", "USER")
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("prefs-findallofscope-403", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_return_preference_by_key() throws Exception {
        mockMvc.perform(
                        get(PreferencesApi.API_PREFERENCES + "/1000")
                )
                .andExpect(status().isOk())
                .andDo(document("prefs-findbykey", preprocessResponse(prettyPrint())))
                .andExpect(jsonPath("$.key", is("key1")))
                .andExpect(jsonPath("$.value", is("current val")))
                .andExpect(jsonPath("$.description", is("String description")))
                .andExpect(jsonPath("$.@class", is("org.openwms.core.preferences.api.UserPreferenceVO")))
        ;
    }

    @Test
    void shall_return_preference_by_key_404() throws Exception {
        mockMvc.perform(
                        get(PreferencesApi.API_PREFERENCES + "/UNKNOWN")
                )
                .andExpect(status().isNotFound())
                .andDo(document("prefs-findbykey404", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_update_preference_by_key() throws Exception {
        var om = new ObjectMapper();
        var vo = new UserPreferenceVO();
        vo.setpKey("1000");
        vo.setKey("keyX");
        vo.setOwner("owner2");
        vo.setDescription("A Boolean");
        vo.setType("BOOL");
        vo.setVal(true);
        mockMvc.perform(
                        put(PreferencesApi.API_PREFERENCES + "/1000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isOk())
                .andDo(document("prefs-update", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_update_preference_UNKNOWN() throws Exception {
        var om = new ObjectMapper();
        var vo = new UserPreferenceVO();
        vo.setpKey("1000");
        mockMvc.perform(
                        put(PreferencesApi.API_PREFERENCES + "/UNKNOWN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isNotFound())
                .andDo(document("prefs-update-404", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_delete_preference() throws Exception {
        mockMvc.perform(
                        delete(PreferencesApi.API_PREFERENCES + "/1000")
                )
                .andExpect(status().isNoContent())
                .andDo(document("prefs-delete", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_create_preference() throws Exception {
        var om = new ObjectMapper();
        var vo = new UserPreferenceVO();
        vo.setKey("keyY");
        vo.setOwner("owner2");
        vo.setDescription("A Boolean");
        vo.setType("BOOL");
        vo.setVal(true);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PreferencesApi.API_PREFERENCES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isCreated())
                .andDo(document("prefs-create", preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    void shall_fail_to_create_existing_preference() throws Exception {
        var om = new ObjectMapper();
        var vo = new UserPreferenceVO();
        vo.setKey("keyY");
        vo.setOwner("owner2");
        vo.setDescription("A Boolean");
        vo.setType("BOOL");
        vo.setVal(true);
        var resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(PreferencesApi.API_PREFERENCES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isCreated());

        // Take the pKey of the former created Preference and try to create the same one again
        var result = om.readValue(resultActions.andReturn().getResponse().getContentAsString(), PreferenceVO.class);
        assertThat(result.getpKey()).isNotBlank();

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PreferencesApi.API_PREFERENCES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isConflict())
                .andDo(document("prefs-create-fails", preprocessResponse(prettyPrint())))
        ;
    }
}
