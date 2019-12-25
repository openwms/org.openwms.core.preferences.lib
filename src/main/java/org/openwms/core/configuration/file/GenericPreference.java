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

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
@Entity
public abstract class GenericPreference extends AbstractPreference implements Serializable {

    /** The String value of the {@code AbstractPreference}. */
    @Column(name = "C_VALUE")
    protected String value;

    /** A binary value for this {@link GenericPreference}. */
    @XmlTransient
    @Lob
    @Column(name = "C_BINVALUE")
    protected Serializable binValue;

    /** A float value of the {@link GenericPreference}. */
    @XmlAttribute(name = "floatValue")
    @Column(name = "C_FLOAT_VALUE")
    protected Float floatValue;

    /** Description text of the {@link GenericPreference}. */
    @Column(name = "C_DESCRIPTION")
    protected String description;

    /** Minimum value. */
    @XmlAttribute(name = "minimum")
    @Column(name = "C_MINIMUM")
    protected int minimum = 0;

    /** Maximum value. */
    @XmlAttribute(name = "maximum")
    @Column(name = "C_MAXIMUM")
    protected int maximum = 0;

    /** Flag to remember if the preference was originally imported from a file. */
    @XmlTransient
    @Column(name = "C_FROM_FILE")
    private boolean fromFile = true;

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
     * Get the binValue.
     *
     * @return the binValue.
     */
    @XmlTransient
    @JsonIgnore
    public Serializable getBinValue() {
        return binValue;
    }

    /**
     * Get the <code>floatValue</code> of the {@link GenericPreference}.
     *
     * @return The floatValue of the preference
     */
    public Float getFloatValue() {
        return floatValue;
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
    public int getMinimum() {
        return minimum;
    }

    /**
     * Return the possible maximum value of the {@link GenericPreference}.
     *
     * @return The possible maximum value
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Return all fields as concatenated String.
     *
     * @return fields as String
     */
    @JsonIgnore
    public String getPropertiesAsString() {
        // TODO [openwms]: 17/05/16
        return null;
    }

    @Override
    public String toString() {
        return "AbstractPreference{" +
                "value='" + value + '\'' +
                ", binValue=" + binValue +
                ", floatValue=" + floatValue +
                ", description='" + description + '\'' +
                ", minimum=" + minimum +
                ", maximum=" + maximum +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericPreference that = (GenericPreference) o;
        return minimum == that.minimum &&
                maximum == that.maximum &&
                Objects.equals(value, that.value) &&
                Objects.equals(binValue, that.binValue) &&
                Objects.equals(floatValue, that.floatValue) &&
                Objects.equals(description, that.description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(value, binValue, floatValue, description, minimum, maximum);
    }
}