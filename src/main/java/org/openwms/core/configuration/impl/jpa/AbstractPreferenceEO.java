/*
 * Copyright 2005-2020 the original author or authors.
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
package org.openwms.core.configuration.impl.jpa;

import org.ameba.integration.jpa.ApplicationEntity;
import org.openwms.core.configuration.PreferenceType;
import org.openwms.core.configuration.PropertyScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * An AbstractPreferenceEO is a superclass for all other preference classes within the application. It encapsulates some common behavior
 * of preference types.
 *
 * @author Heiko Scherrer
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "COR_PREF_PREFERENCE")
public abstract class AbstractPreferenceEO extends ApplicationEntity implements Serializable {

    /** Suffix for the FIND_ALL named query. Default {@value} */
    public static final String FIND_ALL = ".findAll";

    /** Suffix for the FIND_BY_OWNER named query. Default {@value} */
    public static final String FIND_BY_OWNER = ".findByOwner";

    /** Parameter name for the owner. Default {@value} */
    public static final String NQ_PARAM_OWNER = ":owner";

    /** Description text of the {@link AbstractPreferenceEO}. */
    @Column(name = "C_DESCRIPTION")
    protected String description;

    /** Flag to remember if the preference was originally imported from a file. */
    @Column(name = "C_FROM_FILE")
    private boolean fromFile = true;

    /** Scope of this preference. */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "C_SCOPE", nullable = false)
    protected PropertyScope scope;

    /** Type of this preference. */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "C_TYPE", nullable = false)
    protected PreferenceType type;

    @Column(name = "C_OWNER")
    protected String owner;

    /**
     * Return all fields as an array of objects.
     *
     * @return fields as array
    protected abstract Object[] getFields();
     */

    /**
     * Return the particular type of the preference.
     *
     * @return The type of the preference
    public abstract PropertyScope getType();
     */

    /**
     * Return a {@link PreferenceKey} of this preference.
     *
     * @return A {@link PreferenceKey}
    public abstract PreferenceKey getPrefKey();
     */
}