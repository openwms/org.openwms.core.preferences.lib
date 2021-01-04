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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.openwms.core.configuration.CoreConstants.API_PREFERENCES;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

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
    void shall_return_all_for_user() {
        this.client
                .get()
                .uri(u -> u.path(API_PREFERENCES)
                        .queryParam("user", "openwms")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("prefs-findforuser", preprocessResponse(prettyPrint()))
                )
        ;
    }

    @Test
    void shall_return_all_for_user_and_key() {
        this.client
                .get()
                .uri(u -> u.path(API_PREFERENCES)
                        .queryParam("user", "owner1")
                        .queryParam("key", "key1")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("prefs-findforuserkey", preprocessResponse(prettyPrint()))
                )
        ;
    }
}
