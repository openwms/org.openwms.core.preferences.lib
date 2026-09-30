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

import org.openwms.core.preferences.PropertyScope;
import org.openwms.core.preferences.PropertyScopeRegistrar;
import org.openwms.core.preferences.PropertyScopes;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link PropertyScopes} registry. Collects all scopes from the {@link PropertyScopeRegistrar} beans
 * discovered at startup. The built-in scopes are contributed by the {@link DefaultPropertyScopeRegistrar} unless the consumer
 * defines an own registrar bean, in which case the consumer controls the complete scope set. An empty resulting scope set is
 * a misconfiguration and rejected at startup.
 *
 * @author Heiko Scherrer
 */
@Component
class PropertyScopesImpl implements PropertyScopes {

    private final Map<String, PropertyScope> scopes;

    PropertyScopesImpl(List<PropertyScopeRegistrar> registrars) {
        this.scopes = new ConcurrentHashMap<>();
        if (registrars != null) {
            for (PropertyScopeRegistrar registrar : registrars) {
                Collection<String> registeredScopes = registrar.register();
                if (registeredScopes != null) {
                    for (String scopeName : registeredScopes) {
                        scopes.put(scopeName, PropertyScope.of(scopeName));
                    }
                }
            }
        }
        if (scopes.isEmpty()) {
            throw new IllegalStateException(
                    "No property scopes registered. At least one PropertyScopeRegistrar bean must register at least one scope"
            );
        }
    }

    @Override
    public boolean isRegistered(String name) {
        return scopes.containsKey(name);
    }

    @Override
    public PropertyScope resolve(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Scope name must not be blank");
        }
        PropertyScope scope = scopes.get(name);
        if (scope == null) {
            throw new IllegalArgumentException("Unknown scope: " + name);
        }
        return scope;
    }

    @Override
    public Collection<PropertyScope> all() {
        return scopes.values();
    }
}
