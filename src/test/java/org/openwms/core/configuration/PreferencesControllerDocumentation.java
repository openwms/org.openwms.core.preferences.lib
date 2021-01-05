/*
 * Copyright 2005-2020 the original author or authors.
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
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.openwms.core.configuration.CoreConstants.API_PREFERENCES;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
                                        fieldWithPath("[].key").description("The preference key"),
                                        fieldWithPath("[].value").description("The preference value"),
                                        fieldWithPath("[].type").description("The preference value type"),
                                        fieldWithPath("[].description").description("The descriptive text of the preference"),
                                        fieldWithPath("[].owner").optional().description("The exclusive preference owner"),
                                        fieldWithPath("[].@class").description("Metadata used internally to clarify the preference type")
                                )
                        )
                )
                .jsonPath("$[0].key").exists()
                .jsonPath("$[0].value").exists()
                .jsonPath("$[0].description").exists()
                .jsonPath("$[0].@class").exists()
                .jsonPath("$[0].type").exists()
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
                .jsonPath("$.@class").isEqualTo("org.openwms.core.configuration.api.PreferenceVO")
                .jsonPath("$.type").isEqualTo("STRING")
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
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(
                        document("prefs-update",
                                preprocessResponse(prettyPrint())
                        )
                )
        ;
    }
}
