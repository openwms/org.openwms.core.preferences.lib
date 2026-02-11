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
package org.openwms.core.preferences.impl.file;

import jakarta.xml.bind.annotation.XmlRegistry;

/**
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content. The Java
 * representation of XML content can consist of schema derived interfaces and classes representing the binding of schema type definitions,
 * element declarations and model groups. Factory methods for each of these are provided in this class.
 * 
 * @author Heiko Scherrer
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * org.openwms.core.domain.preferences.
     */
    public ObjectFactory() {
        super();
    }

    public static Class[] allTypes() {
        return new Class[]{ApplicationPreference.class, ModulePreference.class, RolePreference.class, UserPreference.class};
    }

    /**
     * Create an instance of {@link ModulePreference}.
     * 
     * @return an instance of {@link ModulePreference}
     */
    public ModulePreference createModulePreference() {
        return new ModulePreference();
    }

    /**
     * Create an instance of {@link Preferences}.
     * 
     * @return an instance of {@link Preferences}
     */
    public Preferences createPreferences() {
        return new Preferences();
    }

    /**
     * Create an instance of {@link ApplicationPreference}.
     * 
     * @return an instance of {@link ApplicationPreference}
     */
    public ApplicationPreference createApplicationPreference() {
        return new ApplicationPreference();
    }

    /**
     * Create an instance of {@link UserPreference}.
     *
     * @return an instance of {@link UserPreference}
     */
    public UserPreference createUserPreference() {
        return new UserPreference();
    }

    /**
     * Create an instance of {@link UserPreference}.
     *
     * @param username
     *            The name of the User
     * @param key
     *            The key of the preference
     * @param description
     *            The description text
     * @return an instance of {@link UserPreference}
     */
    public static UserPreference createUserPreference(String username, String key, String description) {
        UserPreference result = new UserPreference(username, key);
        result.setDescription(description);
        return result;
    }

    /**
     * Create an instance of {@link RolePreference}.
     *
     * @return an instance of {@link RolePreference}
     */
    public RolePreference createRolePreference() {
        return new RolePreference();
    }
}