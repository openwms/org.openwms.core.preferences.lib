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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.configuration.api.PreferenceVO;
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.openwms.core.configuration.api.PreferencesApi.API_PREFERENCES;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

/**
 * A PreferencesControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@CoreApplicationTest
@SqlGroup({
        @Sql(scripts = "classpath:test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PreferencesControllerDocumentation extends DefaultTestProfile {

    @Autowired
    private ApplicationContext context;
    private WebTestClient client;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.client = WebTestClient
                .bindToApplicationContext(this.context)
                .configureClient()
                .filter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void shall_return_index() {
        client
                .get()
                .uri(API_PREFERENCES + "/index")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("prefs-index",
                                preprocessResponse(prettyPrint())
                        )
                )
                .jsonPath("$._links.preferences-findall").exists()
                .jsonPath("$._links.preferences-findbypkey").exists()
                .jsonPath("$._links.preferences-update").exists()
                .jsonPath("$._links.preferences-delete").exists()
                .jsonPath("$._links.user-preferences-findbyuser").exists()
                .jsonPath("$._links.user-preferences-findbyuserandkey").exists()
                .jsonPath("$._links.length()", is(6))
        ;
    }

    @Test
    void shall_return_all_preferences() {
        this.client
                .get()
                .uri(API_PREFERENCES)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("prefs-findall",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[]").description("An array of all existing preferences"),
                                        fieldWithPath("[].pKey").description("The persistent unique key"),
                                        fieldWithPath("[].key").description("The business key, unique combined with the owner"),
                                        fieldWithPath("[].value").description("The preference value"),
                                        fieldWithPath("[].type").description("The preference type (FLOAT|STRING|INT|OBJECT|BOOL)"),
                                        fieldWithPath("[].description").description("The descriptive text of the preference"),
                                        fieldWithPath("[].owner").optional().description("The exclusive preference owner"),
                                        fieldWithPath("[].@class").description("Metadata used internally to clarify the preference type")
                                )
                        )
                )
                .jsonPath("$[0].pKey").exists()
                .jsonPath("$[0].key").exists()
                .jsonPath("$[0].value").exists()
                .jsonPath("$[0].description").exists()
                .jsonPath("$[0].@class").exists()
        ;
    }

    @Test
    void shall_return_preference_by_key() {
        this.client
                .get()
                .uri(u -> u.path(API_PREFERENCES + "/1000")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("prefs-findbykey",
                                preprocessResponse(prettyPrint())
                        )
                )
                .jsonPath("$.key").isEqualTo("key1")
                .jsonPath("$.value").isEqualTo("current val")
                .jsonPath("$.description").isEqualTo("String description")
                .jsonPath("$.@class").isEqualTo("org.openwms.core.configuration.api.UserPreferenceVO")
        ;
    }

    @Test
    void shall_return_preference_by_key_404() {
        this.client
                .get()
                .uri(u -> u.path(API_PREFERENCES + "/UNKNOWN")
                        .build()
                )
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(
                        document("prefs-findbykey404",
                                preprocessResponse(prettyPrint())
                        )
                )
        ;
    }

    @Test
    void shall_update_preference_by_key() throws Exception {
        ObjectMapper om = new ObjectMapper();
        UserPreferenceVO vo = new UserPreferenceVO();
        vo.setpKey("1000");
        vo.setKey("keyX");
        vo.setOwner("owner2");
        vo.setDescription("A Boolean");
        vo.setType("BOOL");
        vo.setVal(true);
        this.client
                .put()
                .uri(u -> u.path(API_PREFERENCES + "/1000")
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsString(vo))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("prefs-update",
                                preprocessRequest(prettyPrint())
                        )
                )
        ;
    }

    @Test
    void shall_update_preference_UNKNOWN() throws Exception {
        ObjectMapper om = new ObjectMapper();
        UserPreferenceVO vo = new UserPreferenceVO();
        vo.setpKey("1000");
        this.client
                .put()
                .uri(u -> u.path(API_PREFERENCES + "/UNKNOWN")
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsString(vo))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(
                        document("prefs-update-404",
                                preprocessRequest(prettyPrint())
                        )
                )
        ;
    }

    @Test
    void shall_delete_preference() throws Exception {
        this.client
                .delete()
                .uri(u -> u.path(API_PREFERENCES + "/1000")
                        .build()
                )
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(
                        document("prefs-delete",
                                preprocessRequest(prettyPrint())
                        )
                )
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
        this.client
                .post()
                .uri(u -> u.path(API_PREFERENCES)
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsString(vo))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(
                        document("prefs-create",
                                preprocessRequest(prettyPrint())
                ))
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
        WebTestClient.ResponseSpec spec = this.client
                .post()
                .uri(u -> u.path(API_PREFERENCES)
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsString(vo))
                .exchange()
                .expectStatus().isCreated();

        // Take the pKey of the former created Preference and try to create the same one again
        var result = spec.expectBody(PreferenceVO.class).returnResult().getResponseBody();
        assertThat(result.getpKey()).isNotBlank();

        this.client
                .post()
                .uri(u -> u.path(API_PREFERENCES)
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsString(vo))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .consumeWith(
                        document("prefs-create-fails",
                                preprocessResponse(prettyPrint())
                        ))
        ;
    }
}
