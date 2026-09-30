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

import java.util.Collection;
import java.util.List;

/**
 * A DefaultPropertyScopeRegistrar registers the four generic built-in scopes APPLICATION, MODULE, ROLE, and USER. It is
 * contributed by the auto-configuration only when the consumer does not define any {@link PropertyScopeRegistrar} bean itself.
 * As soon as a consumer defines at least one registrar bean, this default backs off entirely and the consumer controls the
 * complete set of registered scopes, including whether the built-ins remain available.
 *
 * @author Heiko Scherrer
 */
public class DefaultPropertyScopeRegistrar implements PropertyScopeRegistrar {

    /**
     * {@inheritDoc}
     *
     * Returns the four built-in scope names.
     */
    @Override
    public Collection<String> register() {
        return List.of(
                PropertyScope.APPLICATION.name(),
                PropertyScope.MODULE.name(),
                PropertyScope.ROLE.name(),
                PropertyScope.USER.name()
        );
    }
}
