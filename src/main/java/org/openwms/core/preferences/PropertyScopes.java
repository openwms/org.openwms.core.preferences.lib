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

import java.util.Collection;

/**
 * A PropertyScopes registry manages all available property scopes. The built-in scopes APPLICATION, MODULE, ROLE, and USER
 * are always registered. Downstream consumers can register additional domain-specific scopes via {@link PropertyScopeRegistrar}
 * beans.
 *
 * @author Heiko Scherrer
 */
public interface PropertyScopes {

    /**
     * Checks whether a scope with the given name is registered.
     *
     * @param name the scope name to check
     * @return true if the scope is registered, false otherwise
     */
    boolean isRegistered(String name);

    /**
     * Resolves a scope by name. Only registered scopes are resolved successfully.
     *
     * @param name the scope name
     * @return the PropertyScope instance
     * @throws IllegalArgumentException if the scope name is not registered or invalid
     */
    PropertyScope resolve(String name);

    /**
     * Returns a collection of all registered scopes.
     *
     * @return all registered scopes
     */
    Collection<PropertyScope> all();
}
