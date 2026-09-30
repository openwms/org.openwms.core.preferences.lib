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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PropertyScope defines the different scopes for preferences. The built-in scopes APPLICATION, MODULE, ROLE, and USER are generic
 * and always available. Downstream consumers can register additional domain-specific scopes (e.g. WAREHOUSE, TENANT) via the
 * {@link PropertyScopes} registry without modifying this library. This enables a generic mechanism for scope extensibility while
 * maintaining type safety and validation: registered scope names follow the pattern [A-Z][A-Z0-9_]*.
 *
 * @author Heiko Scherrer
 */
public final class PropertyScope implements Serializable {

    /** This kind of preference belongs to the main application. */
    public static final PropertyScope APPLICATION = new PropertyScope("APPLICATION");

    /** This kind of preference is specific to a {@code Module}. */
    public static final PropertyScope MODULE = new PropertyScope("MODULE");

    /** This kind of preference belongs to a particular {@code Role}. */
    public static final PropertyScope ROLE = new PropertyScope("ROLE");

    /** This kind of preference belongs to a certain {@code User}. */
    public static final PropertyScope USER = new PropertyScope("USER");

    private final String name;

    /**
     * Creates a PropertyScope with the given name. The name must match the pattern [A-Z][A-Z0-9_]*.
     *
     * @param name the scope name, must not be blank and must match the validation pattern
     * @throws IllegalArgumentException if the name is invalid
     */
    PropertyScope(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Property scope name must not be blank");
        }
        if (!name.matches("^[A-Z][A-Z0-9_]*$")) {
            throw new IllegalArgumentException(
                    "Property scope name must start with an uppercase letter and contain only uppercase letters, digits, or underscores, got: " + name
            );
        }
        this.name = name;
    }

    /**
     * Factory method to create or retrieve a PropertyScope. For the built-in scopes APPLICATION, MODULE, ROLE, and USER, returns
     * the static constants. For custom scopes registered via {@link PropertyScopes}, returns a new instance if the name matches
     * the validation pattern.
     *
     * @param name the scope name, must match [A-Z][A-Z0-9_]*
     * @return a PropertyScope instance
     * @throws IllegalArgumentException if the name is invalid
     */
    @JsonCreator
    public static PropertyScope of(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Property scope name must not be blank");
        }
        // Return built-in constants to ensure identity equality
        return switch (name) {
            case "APPLICATION" -> APPLICATION;
            case "MODULE" -> MODULE;
            case "ROLE" -> ROLE;
            case "USER" -> USER;
            default -> new PropertyScope(name);
        };
    }

    /**
     * Returns the name of this scope.
     *
     * @return the scope name
     */
    @JsonValue
    public String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyScope that)) return false;
        return Objects.equals(name, that.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }
}
