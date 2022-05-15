/*
 * Copyright 2005-2021 the original author or authors.
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
package org.openwms.core.configuration.impl.file;

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
    private List<GenericPreference> applicationOrRoleOrUserOrModule;
    @XmlTransient
    private List<ApplicationPreference> applications;
    @XmlTransient
    private List<ModulePreference> modules;
    @XmlTransient
    private List<UserPreference> users;
    @XmlTransient
    private List<RolePreference> roles;
    /** All concrete types of AbstractPreference. */
    protected static final Class<?>[] TYPES = {ApplicationPreference.class, ModulePreference.class, RolePreference.class,
            UserPreference.class};

    /**
     * Gets the value of the applicationOrRoleOrUserOrModule property. This method is called by the JAXB unmarshaller only.
     *
     * @return a list of all preferences
     */
    public List<GenericPreference> getApplicationOrRoleOrUserOrModule() {
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
    public List<GenericPreference> getAll() {
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
            for (GenericPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof ApplicationPreference p) {
                    applications.add(p);
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
            for (GenericPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof ModulePreference p) {
                    modules.add(p);
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
            for (GenericPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof UserPreference p) {
                    users.add(p);
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
            for (GenericPreference pref : applicationOrRoleOrUserOrModule) {
                if (pref instanceof RolePreference p) {
                    roles.add(p);
                }
            }
        }
        return roles;
    }
}