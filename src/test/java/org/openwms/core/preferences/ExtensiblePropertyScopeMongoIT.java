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

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for extensible PropertyScope support against a real MongoDB, covering the MONGODB persistence adapter and the
 * custom read/write converters.
 *
 * @author Heiko Scherrer
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(classes = {
        PreferencesTestStarter.class,
        ExtensiblePropertyScopeMongoIT.ScopeTestConfiguration.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"TEST", "MONGODB"})
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=" +
                "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
                "org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration," +
                "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration," +
                "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration",
        "spring.jpa.show-sql=false",
        "spring.main.banner-mode=OFF"
})
class ExtensiblePropertyScopeMongoIT {

    @Container
    @ServiceConnection
    static final MongoDBContainer MONGO = new MongoDBContainer("mongo:7");

    @Autowired
    private PropertyScopes propertyScopes;

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @TestConfiguration
    public static class ScopeTestConfiguration {

        /**
         * Test registrar taking full control of the scope set: the built-in scopes plus the custom TENANT scope.
         */
        @Bean
        PropertyScopeRegistrar mongoTenantScopeRegistrar() {
            return () -> List.of("APPLICATION", "MODULE", "ROLE", "USER", "TENANT");
        }
    }

    /**
     * Acceptance: a preference with a custom scope survives the full Mongo round-trip through the persistence adapter and the
     * PropertyScope read/write converters, and the raw document stores the scope as the plain scope name.
     */
    @Test
    void testMongoRoundTripWithCustomScope() {
        PropertyScope tenant = propertyScopes.resolve("TENANT");
        Preference pref = Preference.newBuilder()
                .key("MONGO_TENANT_PREF")
                .owner("tenant321")
                .scope(tenant)
                .val("mongo-value")
                .type(PreferenceType.STRING)
                .build();

        Preference saved = preferencesService.create(pref);
        assertThat(saved.getPersistentKey()).isNotBlank();
        assertThat(saved.getScope()).isEqualTo(tenant);

        Optional<Preference> found = preferencesService.findForOwnerAndScopeAndKey("tenant321", tenant, "MONGO_TENANT_PREF");
        assertThat(found).isPresent();
        assertThat(found.get().getScope().name()).isEqualTo("TENANT");
        assertThat(found.get().getVal()).isEqualTo("mongo-value");

        // The raw document must carry the scope as the plain string name (write converter)
        Document raw = mongoTemplate.getCollection(mongoTemplate.getCollectionNames().stream()
                        .filter(n -> !n.startsWith("system.")).findFirst().orElseThrow())
                .find(new Document("key", "MONGO_TENANT_PREF")).first();
        assertThat(raw).isNotNull();
        assertThat(raw.get("scope")).isEqualTo("TENANT");
    }

    /**
     * Acceptance: built-in scopes keep working unchanged against MongoDB.
     */
    @Test
    void testMongoRoundTripWithBuiltinScope() {
        Preference pref = Preference.newBuilder()
                .key("MONGO_USER_PREF")
                .owner("user42")
                .scope(PropertyScope.USER)
                .val("user-value")
                .type(PreferenceType.STRING)
                .build();

        preferencesService.create(pref);

        Optional<Preference> found = preferencesService.findForOwnerAndScopeAndKey("user42", PropertyScope.USER, "MONGO_USER_PREF");
        assertThat(found).isPresent();
        assertThat(found.get().getScope()).isEqualTo(PropertyScope.USER);
    }

    /**
     * Acceptance: updating and deleting a custom-scope preference works against MongoDB.
     */
    @Test
    void testMongoUpdateAndDeleteWithCustomScope() {
        PropertyScope tenant = propertyScopes.resolve("TENANT");
        Preference created = preferencesService.create(Preference.newBuilder()
                .key("MONGO_TENANT_UPD")
                .owner("tenant654")
                .scope(tenant)
                .val("before")
                .type(PreferenceType.STRING)
                .build());

        created.setVal("after");
        Preference updated = preferencesService.update(created.getPersistentKey(), created);
        assertThat(updated.getVal()).isEqualTo("after");
        assertThat(updated.getScope()).isEqualTo(tenant);

        preferencesService.delete(updated.getPersistentKey());
        assertThat(preferencesService.findForOwnerAndScopeAndKey("tenant654", tenant, "MONGO_TENANT_UPD")).isEmpty();
    }
}
