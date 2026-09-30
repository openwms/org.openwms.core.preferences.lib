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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for extensible PropertyScope support.
 *
 * @author Heiko Scherrer
 */
@SpringBootTest(classes = {
        PreferencesTestStarter.class,
        ExtensiblePropertyScopeIT.ScopeTestConfiguration.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.jpa.show-sql=false",
        "spring.main.banner-mode=OFF",
        "spring.jackson.serialization.INDENT_OUTPUT=true"
})
class ExtensiblePropertyScopeIT {

    @Autowired
    private PropertyScopes propertyScopes;

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private WebApplicationContext context;

    @TestConfiguration
    public static class ScopeTestConfiguration {

        /**
         * Test registrar taking full control of the scope set: the built-in scopes plus the custom TENANT scope. With an own
         * registrar bean present the library's default registrar backs off.
         */
        @Bean
        PropertyScopeRegistrar tenantScopeRegistrar() {
            return () -> List.of("APPLICATION", "MODULE", "ROLE", "USER", "TENANT");
        }
    }

    /**
     * Test that the four built-in scopes are always available.
     */
    @Test
    void testBuiltinScopesAreRegistered() {
        assertThat(propertyScopes.isRegistered("APPLICATION")).isTrue();
        assertThat(propertyScopes.isRegistered("MODULE")).isTrue();
        assertThat(propertyScopes.isRegistered("ROLE")).isTrue();
        assertThat(propertyScopes.isRegistered("USER")).isTrue();
    }

    /**
     * Test that custom scopes registered via PropertyScopeRegistrar are available.
     */
    @Test
    void testCustomScopeIsRegistered() {
        assertThat(propertyScopes.isRegistered("TENANT")).isTrue();
    }

    /**
     * Test that resolve() returns the correct PropertyScope for built-in scopes.
     */
    @Test
    void testResolveBuiltinScope() {
        PropertyScope app = propertyScopes.resolve("APPLICATION");
        assertThat(app).isEqualTo(PropertyScope.APPLICATION);
        assertThat(app.name()).isEqualTo("APPLICATION");
    }

    /**
     * Test that resolve() returns a new PropertyScope for custom scopes.
     */
    @Test
    void testResolveCustomScope() {
        PropertyScope tenant = propertyScopes.resolve("TENANT");
        assertThat(tenant).isNotNull();
        assertThat(tenant.name()).isEqualTo("TENANT");
    }

    /**
     * Test that resolve() throws for unregistered scopes.
     */
    @Test
    void testResolveUnregisteredScopeThrows() {
        assertThatThrownBy(() -> propertyScopes.resolve("UNKNOWN_SCOPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown scope");
    }

    /**
     * Test that all() returns all scopes including custom ones.
     */
    @Test
    void testAllReturnsAllScopes() {
        Collection<PropertyScope> all = propertyScopes.all();
        assertThat(all).hasSize(5); // 4 built-ins + 1 custom TENANT
        assertThat(all.stream().map(PropertyScope::name))
                .containsExactlyInAnyOrder("APPLICATION", "MODULE", "ROLE", "USER", "TENANT");
    }

    /**
     * Test that invalid scope names are rejected.
     */
    @Test
    void testInvalidScopeNameThrows() {
        assertThatThrownBy(() -> PropertyScope.of("invalid-scope"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must start with an uppercase letter");
    }

    /**
     * Test that PropertyScope.equals() works correctly.
     */
    @Test
    void testPropertyScopeEquality() {
        PropertyScope app1 = PropertyScope.APPLICATION;
        PropertyScope app2 = PropertyScope.of("APPLICATION");
        assertThat(app1).isEqualTo(app2);

        PropertyScope tenant1 = propertyScopes.resolve("TENANT");
        PropertyScope tenant2 = propertyScopes.resolve("TENANT");
        assertThat(tenant1).isEqualTo(tenant2);
    }

    /**
     * Test that PropertyScope.hashCode() is consistent with equality.
     */
    @Test
    void testPropertyScopeHashCode() {
        PropertyScope app1 = PropertyScope.APPLICATION;
        PropertyScope app2 = PropertyScope.of("APPLICATION");
        assertThat(app1.hashCode()).isEqualTo(app2.hashCode());
    }

    /**
     * Test creating a preference with a custom scope.
     */
    @Test
    void testCreatePreferenceWithCustomScope() {
        PropertyScope tenant = propertyScopes.resolve("TENANT");
        Preference pref = Preference.newBuilder()
                .key("CUSTOM_PREF")
                .owner("tenant123")
                .scope(tenant)
                .val("value123")
                .type(PreferenceType.STRING)
                .build();

        Preference saved = preferencesService.create(pref);
        assertThat(saved).isNotNull();
        assertThat(saved.getScope()).isEqualTo(tenant);
        assertThat(saved.getScope().name()).isEqualTo("TENANT");
    }

    /**
     * Test finding a preference with a custom scope.
     */
    @Test
    void testFindPreferenceWithCustomScope() {
        PropertyScope tenant = propertyScopes.resolve("TENANT");
        Preference pref = Preference.newBuilder()
                .key("FIND_CUSTOM_PREF")
                .owner("tenant456")
                .scope(tenant)
                .val("findable")
                .type(PreferenceType.STRING)
                .build();

        preferencesService.create(pref);

        Optional<Preference> found = preferencesService.findForOwnerAndScopeAndKey("tenant456", tenant, "FIND_CUSTOM_PREF");
        assertThat(found).isPresent();
        assertThat(found.get().getScope()).isEqualTo(tenant);
        assertThat(found.get().getVal()).isEqualTo("findable");
    }

    /**
     * Acceptance: full REST round-trip for a custom scope through the generic endpoints. Creates a TENANT preference as a base
     * PreferenceVO carrying the scope name, reads it back filtered by scope, and deletes it.
     */
    @Test
    void testRestRoundTripWithCustomScope() throws Exception {
        var mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        var body = """
                {
                  "@class": "org.openwms.core.preferences.api.PreferenceVO",
                  "key": "REST_TENANT_PREF",
                  "owner": "tenant789",
                  "scope": "TENANT",
                  "val": "rest-value",
                  "type": "STRING"
                }
                """;

        // Create
        var createResult = mockMvc.perform(post(org.openwms.core.preferences.api.PreferencesApi.API_PREFERENCES)
                        .contentType("application/json")
                        .content(body))
                .andReturn();
        assertThat(createResult.getResponse().getStatus())
                .as("create response: %s", createResult.getResponse().getContentAsString())
                .isEqualTo(201);
        var location = createResult.getResponse().getHeader("Location");
        assertThat(location).isNotBlank();

        // Read back via generic owner/scope/key filter
        mockMvc.perform(get(org.openwms.core.preferences.api.PreferencesApi.API_PREFERENCES)
                        .queryParam("owner", "tenant789")
                        .queryParam("scope", "TENANT")
                        .queryParam("key", "REST_TENANT_PREF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("REST_TENANT_PREF"))
                .andExpect(jsonPath("$.scope").value("TENANT"));

        // Unregistered scope still rejected
        mockMvc.perform(get(org.openwms.core.preferences.api.PreferencesApi.API_PREFERENCES)
                        .queryParam("owner", "tenant789")
                        .queryParam("scope", "UNREGISTERED"))
                .andExpect(status().is4xxClientError());

        // Delete
        var pKey = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(delete(org.openwms.core.preferences.api.PreferencesApi.API_PREFERENCES + "/" + pKey))
                .andExpect(status().isNoContent());
        assertThat(preferencesService.findForOwnerAndScopeAndKey("tenant789", propertyScopes.resolve("TENANT"), "REST_TENANT_PREF")).isEmpty();
    }
}
