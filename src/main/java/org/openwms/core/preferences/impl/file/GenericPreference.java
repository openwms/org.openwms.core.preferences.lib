/*
 * Copyright 2005-2023 the original author or authors.
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;
import java.util.Objects;

/**
 * An GenericPreference is a superclass for all other preference classes within the application. It encapsulates some common behavior
 * of preference types.
 *
 * @author Heiko Scherrer
 */
@XmlType(name = "abstractPreference", propOrder = {"description"}, namespace = "http://www.openwms.org/schema/preferences")
public abstract class GenericPreference extends AbstractPreference implements Serializable {

    /** The String value of the {@code AbstractPreference}. */
    protected String value;

    /** A data type of the value. */
    protected String type;

    /** Description text of the {@link GenericPreference}. */
    protected String description;

    /** Minimum value. */
    protected String minimum;

    /** Maximum value. */
    protected String maximum;

    /** Flag to remember if the preference was originally imported from a file. */
    @XmlTransient
    private static final boolean fromFile = true;

    /* ----------------------------- methods ------------------- */

    /**
     * Return the <code>value</code> of the {@link GenericPreference}.
     *
     * @return The value of the {@link GenericPreference}
     */
    @XmlAttribute(name = "val")
    public String getValue() {
        return value;
    }

    /**
     * Set the <code>value</code> of the {@link GenericPreference}.
     *
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the property value type.
     *
     * @return Type as String
     */
    @XmlAttribute(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return the <code>description</code> of the {@link GenericPreference}.
     *
     * @return The description as String
     */
    @XmlValue
    public String getDescription() {
        return description;
    }

    /**
     * Set a <code>description</code> for the {@link GenericPreference}.
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the possible minimum value of the {@link GenericPreference}.
     *
     * @return The possible minimum value
     */
    @XmlAttribute(name = "minimum")
    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    /**
     * Return the possible maximum value of the {@link GenericPreference}.
     *
     * @return The possible maximum value
     */
    @XmlAttribute(name = "maximum")
    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    @Override
    public String toString() {
        return "GenericPreference{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", minimum=" + minimum +
                ", maximum=" + maximum +
                ", fromFile=" + fromFile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericPreference that = (GenericPreference) o;
        return Objects.equals(value, that.value)
                && Objects.equals(type, that.type)
                && Objects.equals(description, that.description)
                && Objects.equals(minimum, that.minimum)
                && Objects.equals(maximum, that.maximum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type, description, minimum, maximum);
    }
}