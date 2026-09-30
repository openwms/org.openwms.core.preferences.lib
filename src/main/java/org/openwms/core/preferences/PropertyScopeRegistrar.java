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
 * A PropertyScopeRegistrar is a functional interface for registering additional property scopes. Implementations are
 * discovered via Spring's component scanning and automatically registered with the {@link PropertyScopes} bean during
 * auto-configuration. This allows downstream consumers to extend the library with domain-specific scopes without
 * modifying the library code.
 *
 * @author Heiko Scherrer
 */
@FunctionalInterface
public interface PropertyScopeRegistrar {

    /**
     * Registers additional property scopes. The returned collection must contain valid scope names matching the pattern
     * [A-Z][A-Z0-9_]*.
     *
     * @return a collection of additional scope names to register (may be empty, but not null)
     */
    Collection<String> register();
}
