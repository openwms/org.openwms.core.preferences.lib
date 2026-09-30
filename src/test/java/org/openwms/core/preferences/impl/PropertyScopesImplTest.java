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
package org.openwms.core.preferences.impl;

import org.junit.jupiter.api.Test;
import org.openwms.core.preferences.PropertyScope;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link PropertyScopesImpl} registry semantics: the registered set is exactly the union of the registrar
 * contributions, the default registrar contributes the built-ins, and an empty set is rejected at startup.
 *
 * @author Heiko Scherrer
 */
class PropertyScopesImplTest {

    @Test
    void shouldRegisterExactlyTheDefaultRegistrarScopes() {
        var scopes = new PropertyScopesImpl(List.of(new DefaultPropertyScopeRegistrar()));

        assertThat(scopes.isRegistered("APPLICATION")).isTrue();
        assertThat(scopes.isRegistered("MODULE")).isTrue();
        assertThat(scopes.isRegistered("ROLE")).isTrue();
        assertThat(scopes.isRegistered("USER")).isTrue();
        assertThat(scopes.all()).hasSize(4);
    }

    @Test
    void shouldGiveConsumerFullControl_withoutBuiltins() {
        var scopes = new PropertyScopesImpl(List.of(() -> List.of("WAREHOUSE")));

        assertThat(scopes.isRegistered("WAREHOUSE")).isTrue();
        assertThat(scopes.isRegistered("APPLICATION")).isFalse();
        assertThat(scopes.isRegistered("USER")).isFalse();
        assertThat(scopes.all()).hasSize(1);
        assertThatThrownBy(() -> scopes.resolve(PropertyScope.USER.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown scope");
    }

    @Test
    void shouldMergeMultipleRegistrars() {
        var scopes = new PropertyScopesImpl(List.of(
                new DefaultPropertyScopeRegistrar(),
                () -> List.of("WAREHOUSE")
        ));

        assertThat(scopes.all()).hasSize(5);
        assertThat(scopes.isRegistered("WAREHOUSE")).isTrue();
        assertThat(scopes.isRegistered("USER")).isTrue();
    }

    @Test
    void shouldFailAtStartup_whenNoScopesRegistered() {
        assertThatThrownBy(() -> new PropertyScopesImpl(List.of()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No property scopes registered");
        assertThatThrownBy(() -> new PropertyScopesImpl(List.of(List::of)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No property scopes registered");
    }
}
