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
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.openwms.core.configuration.CoreConstants.API_PREFERENCES;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

/**
 * A ConfigurationControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@CoreApplicationTest
@Sql(scripts = "classpath:test.sql")
class ConfigurationControllerDocumentation extends DefaultTestProfile {

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
                        document("prefs-findall", preprocessResponse(prettyPrint()))
                )
                .jsonPath("$[0].value").isEqualTo("en_US")
                .jsonPath("$[0].floatValue").isEqualTo(22.1f)
                .jsonPath("$[0].description").isEqualTo("description")
                .jsonPath("$[0].minimum").isEqualTo(10)
                .jsonPath("$[0].maximum").isEqualTo(100)
                .jsonPath("$[0].type").isEqualTo("APPLICATION")
                .jsonPath("$[0].key").isEqualTo("defaultLanguage")
        ;
    }

    @Test
    void shall_return_all_for_owner() {
        this.client
                .get()
                .uri(API_PREFERENCES)
                .attribute("owner", "SA")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("prefs-findforowner", preprocessResponse(prettyPrint()))
                )
        ;
    }
}
