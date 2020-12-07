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
package org.openwms.core.configuration.file;

import org.openwms.core.configuration.PropertyScope;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * An AbstractPreference is a superclass for all other preference classes within the application. It encapsulates some common behavior
 * of preference types.
 *
 * @author Heiko Scherrer
 */
@XmlTransient
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "COR_PREFERENCE")
public abstract class AbstractPreference implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    /** Suffix for the FIND_BY_OWNER named query. Default {@value} */
    public static final String FIND_BY_OWNER = ".findByOwner";

    /**
     * Return all fields as an array of objects.
     *
     * @return fields as array
     */
    protected abstract Object[] getFields();

    /**
     * Return the particular type of the preference.
     *
     * @return The type of the preference
     */
    public abstract PropertyScope getType();

    /**
     * Return a {@link PreferenceKey} of this preference.
     *
     * @return A {@link PreferenceKey}
     */
    public abstract PreferenceKey getPrefKey();
}