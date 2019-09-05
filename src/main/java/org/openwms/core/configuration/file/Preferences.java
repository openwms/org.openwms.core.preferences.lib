/*
 * Copyright 2005-2019 the original author or authors.
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
package org.openwms.core.configuration.file;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An instance of a {@code Preferences} represents the root of a preferences XML file and aggregates all other types of preference.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"applicationOrRoleOrUserOrModule"})
@XmlRootElement(name = "preferences", namespace = "http://www.openwms.org/schema/preferences")
public class Preferences implements Serializable {

    @XmlElements({@XmlElement(name = "module", type = ModulePreference.class),
            @XmlElement(name = "application", type = ApplicationPreference.class),
            @XmlElement(name = "role", type = RolePreference.class),
            @XmlElement(name = "user", type = UserPreference.class)})
    private List<AbstractPreference> applicationOrRoleOrUserOrModule;
    @XmlTransient
    private List<ApplicationPreference> applications;
    @XmlTransient
    private List<ModulePreference> modules;
    @XmlTransient
    private List<UserPreference> users;
    @XmlTransient
    private List<RolePreference> roles;
    /** All concrete types of AbstractPreference. */
    public static final Class<?>[] TYPES = {ApplicationPreference.class, ModulePreference.class, RolePreference.class,
            UserPreference.class};

    /**
     * Gets the value of the applicationOrRoleOrUserOrModule property. This method is called by the JAXB unmarshaller only.
     *
     * @return a list of all preferences
     */
    public List<AbstractPreference> getApplicationOrRoleOrUserOrModule() {
        if (applicationOrRoleOrUserOrModule == null) {
            applicationOrRoleOrUserOrModule = new ArrayList<>();
        }
        return applicationOrRoleOrUserOrModule;
    }

    /**
     * Return a list of all preferences. Simple call to {@link #getApplicationOrRoleOrUserOrModule()}. Is only added due to naming purpose.
     *
     * @return a list of all preferences
     */
    public List<AbstractPreference> getAll() {
        return getApplicationOrRoleOrUserOrModule();
    }

    /**
     * Return a list of all {@link ApplicationPreference}s or an empty ArrayList when no {@link ApplicationPreference}s exist.
     *
     * @return a list of all {@link ApplicationPreference}s
     */
    public List<ApplicationPreference> getApplications() {
        if (applications == null) {
            applications = new ArrayList<>();
            for (AbstractPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof ApplicationPreference) {
                    applications.add((ApplicationPreference) pref);
                }
            }
        }
        return applications;
    }

    /**
     * Return a list of all {@link ModulePreference}s or an empty ArrayList when no {@link ModulePreference}s exist.
     *
     * @return a list of all {@link ModulePreference}s
     */
    public List<ModulePreference> getModules() {
        if (modules == null) {
            modules = new ArrayList<>();
            for (AbstractPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof ModulePreference) {
                    modules.add((ModulePreference) pref);
                }
            }
        }
        return modules;
    }

    /**
     * Return a list of all {@link UserPreference}s or an empty ArrayList when no {@link UserPreference}s exist.
     *
     * @return a list of all {@link UserPreference}s
     */
    public List<UserPreference> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
            for (AbstractPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof UserPreference) {
                    users.add((UserPreference) pref);
                }
            }
        }
        return users;
    }

    /**
     * Return a list of all {@link RolePreference}s or an empty ArrayList when no {@link RolePreference}s exist.
     *
     * @return a list of all {@link RolePreference}s
     */
    public List<RolePreference> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
            for (AbstractPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof RolePreference) {
                    roles.add((RolePreference) pref);
                }
            }
        }
        return roles;
    }

    /**
     * Return a list of preferences filtered by a specific type, defined by the parameter clazz.
     *
     * @param <T> Expected types are {@code ApplicationPreference}, {@code ModulePreference}, {@code RolePreference}
     * {@code UserPreference}
     * @param clazz The class type of the preference to filter for
     * @return a list of T.

     @SuppressWarnings("unchecked") public <T extends AbstractPreference> List<T> getOfType(Class<T> clazz) {
     if (ApplicationPreference.class.equals(clazz)) {
     return (List<T>) getApplications();
     }
     if (ModulePreference.class.equals(clazz)) {
     return (List<T>) getModules();
     }
     if (UserPreference.class.equals(clazz)) {
     return (List<T>) getUsers();
     }
     if (RolePreference.class.equals(clazz)) {
     return (List<T>) getRoles();
     }
     return Collections.<T>emptyList();
     }
     */
}