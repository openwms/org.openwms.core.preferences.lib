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

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.preferences.api.ApplicationPreferenceVO;
import org.openwms.core.preferences.api.ModulePreferenceVO;
import org.openwms.core.preferences.api.PreferenceVO;
import org.openwms.core.preferences.api.PreferencesApi;
import org.openwms.core.preferences.api.RolePreferenceVO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
                .andExpect(jsonPath("$._links.role-preferences-findbyrole").exists())
                .andExpect(jsonPath("$._links.role-preferences-findbyroleandkey").exists())
                .andExpect(jsonPath("$._links.module-preferences-findbymodule").exists())
                .andExpect(jsonPath("$._links.module-preferences-findbymoduleandkey").exists())
                .andExpect(jsonPath("$._links.length()", is(13)))
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
                .andDo(document("prefs-findbykey",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("@class").description("The full-qualified name (FQN) of the type"),
                                fieldWithPath("pKey").description("The persistent technical key of the Preference"),
                                fieldWithPath("key").description("An identifying key property (uniqueness depends on scope)"),
                                fieldWithPath("owner").description("The owner of the preference (nullable - depends on scope if the Preference has an owner or not)"),
                                fieldWithPath("description").description("Some arbitrary description text to describe the usage of the Preference"),
                                fieldWithPath("value").description("The current value of the Preference"),
                                fieldWithPath("groupName").description("Some arbitrary name to group, or keep, Preferences together by any logical name"),
                                fieldWithPath("type").description("The type can either be one of the following: [FLOAT|STRING|INT|OBJECT|BOOL|JSON]")
                        )))
                .andExpect(content().contentType(UserPreferenceVO.MEDIA_TYPE))
                .andExpect(jsonPath("$.key", is("key1")))
                .andExpect(jsonPath("$.value", is("current val")))
                .andExpect(jsonPath("$.description", is("String description")))
                .andExpect(jsonPath("$.@class", is("org.openwms.core.preferences.api.UserPreferenceVO")))
        ;
    }

    @Test
    void shall_return_role_preference_by_key() throws Exception {
        mockMvc.perform(
                        get(PreferencesApi.API_PREFERENCES + "/1003")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(RolePreferenceVO.MEDIA_TYPE))
                .andExpect(jsonPath("$.key", is("key4")))
                .andExpect(jsonPath("$.owner", is("role1")))
                .andExpect(jsonPath("$.@class", is("org.openwms.core.preferences.api.RolePreferenceVO")))
        ;
    }

    @Test
    void shall_return_module_preference_by_key() throws Exception {
        mockMvc.perform(
                        get(PreferencesApi.API_PREFERENCES + "/1004")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(ModulePreferenceVO.MEDIA_TYPE))
                .andExpect(jsonPath("$.key", is("key5")))
                .andExpect(jsonPath("$.owner", is("module1")))
                .andExpect(jsonPath("$.@class", is("org.openwms.core.preferences.api.ModulePreferenceVO")))
        ;
    }

    @Test
    void shall_return_application_preference_by_key() throws Exception {
        mockMvc.perform(
                        get(PreferencesApi.API_PREFERENCES + "/1002")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(ApplicationPreferenceVO.MEDIA_TYPE))
                .andExpect(jsonPath("$.key", is("key3")))
                .andExpect(jsonPath("$.@class", is("org.openwms.core.preferences.api.ApplicationPreferenceVO")))
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
        var om = JsonMapper.builder().build();
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
        var om = JsonMapper.builder().build();
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
    void shall_create_user_preference() throws Exception {
        var om = JsonMapper.builder().build();
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
                .andExpect(status().isCreated())
                .andDo(document("prefs-create", preprocessResponse(prettyPrint())));

        var result = om.readValue(resultActions.andReturn().getResponse().getContentAsString(), PreferenceVO.class);
        assertThat(result).isInstanceOf(UserPreferenceVO.class);
        assertThat(result.getpKey()).isNotBlank();
    }

    @Test
    void shall_create_role_preference() throws Exception {
        var om = JsonMapper.builder().build();
        var vo = new RolePreferenceVO();
        vo.setKey("keyY");
        vo.setOwner("role2");
        vo.setDescription("A Boolean");
        vo.setType("BOOL");
        vo.setVal(true);
        var resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(PreferencesApi.API_PREFERENCES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isCreated())
                .andDo(document("prefs-create-role", preprocessResponse(prettyPrint())));

        var result = om.readValue(resultActions.andReturn().getResponse().getContentAsString(), PreferenceVO.class);
        assertThat(result).isInstanceOf(RolePreferenceVO.class);
        assertThat(result.getpKey()).isNotBlank();
    }

    @Test
    void shall_create_module_preference() throws Exception {
        var om = JsonMapper.builder().build();
        var vo = new ModulePreferenceVO();
        vo.setKey("keyY");
        vo.setOwner("module2");
        vo.setDescription("A Boolean");
        vo.setType("BOOL");
        vo.setVal(true);
        var resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(PreferencesApi.API_PREFERENCES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isCreated())
                .andDo(document("prefs-create-module", preprocessResponse(prettyPrint())));

        var result = om.readValue(resultActions.andReturn().getResponse().getContentAsString(), PreferenceVO.class);
        assertThat(result).isInstanceOf(ModulePreferenceVO.class);
        assertThat(result.getpKey()).isNotBlank();
    }

    @Test
    void shall_create_application_preference() throws Exception {
        var om = JsonMapper.builder().build();
        var vo = new ApplicationPreferenceVO();
        vo.setKey("keyY");
        vo.setDescription("A Boolean");
        vo.setType("BOOL");
        vo.setVal(true);
        var resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(PreferencesApi.API_PREFERENCES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(vo))
                )
                .andExpect(status().isCreated())
                .andDo(document("prefs-create-application", preprocessResponse(prettyPrint())));

        var result = om.readValue(resultActions.andReturn().getResponse().getContentAsString(), PreferenceVO.class);
        assertThat(result).isInstanceOf(ApplicationPreferenceVO.class);
        assertThat(result.getpKey()).isNotBlank();
    }

    @Test
    void shall_fail_to_create_existing_preference() throws Exception {
        var om = JsonMapper.builder().build();
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
