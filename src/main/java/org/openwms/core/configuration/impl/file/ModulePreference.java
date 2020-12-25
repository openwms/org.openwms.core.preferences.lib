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
package org.openwms.core.configuration.impl.file;

import org.openwms.core.configuration.PropertyScope;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * A ModulePreference is used to store configuration settings in Module scope. <p> The table model of an ModulePreference spans an unique
 * key over the columns C_TYPE, C_OWNER and C_KEY. </p> <p> It's counterpart in the context of JAXB is the modulePreference element. </p>
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@XmlType(name = "modulePreference", namespace = "http://www.openwms.org/schema/preferences")
@Entity
@Table(name = "COR_MODULE_PREFERENCE", uniqueConstraints = @UniqueConstraint(columnNames = {"C_TYPE", "C_OWNER",
        "C_KEY"}))
@NamedQueries({
        @NamedQuery(name = ModulePreference.NQ_FIND_BY_OWNER, query = "select mp from ModulePreference mp where mp.owner = :owner")})
public class ModulePreference extends GenericPreference implements Serializable {

    /**
     * Query to find <strong>all</strong> {@code ModulePreference}s of a {@code Module}. <li>Query parameter name <strong>owner</strong> :
     * The modulename of the {@code Module} to search for.</li><br /> Name is {@value} .
     */
    public static final String NQ_FIND_BY_OWNER = "ModulePreference" + FIND_BY_OWNER;

    /**
     * Type of this preference.
     */
    @XmlTransient
    @Enumerated(EnumType.STRING)
    @Column(name = "C_TYPE")
    private PropertyScope type = PropertyScope.MODULE;

    /**
     * Owner of the {@code ModulePreference} (not nullable).
     */
    @XmlAttribute(name = "owner", required = true)
    @Column(name = "C_OWNER", nullable = false)
    private String owner;

    /**
     * Key of the {@code ModulePreference} (not nullable).
     */
    @XmlAttribute(name = "key", required = true)
    @Column(name = "C_KEY", nullable = false)
    private String key;

    /**
     * Create a new {@code ModulePreference}. Only defined by the JAXB implementation.
     */
    public ModulePreference() {
        super();
    }

    /**
     * Create a new {@code ModulePreference}.
     *
     * @param owner The name of the owning module
     * @param key the key
     * @throws IllegalArgumentException when key or owner is {@literal null} or empty
     */
    public ModulePreference(String owner, String key) {
        // Called from the client-side only.
        super();
        Assert.hasText(owner, "Not allowed to create an ModulePreference with an empty owner");
        Assert.hasText(key, "Not allowed to create an ModulePreference with an empty key");
        this.owner = owner;
        this.key = key;
    }

    /**
     * Get the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyScope getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object[] getFields() {
        return new Object[]{getType(), getOwner(), getKey()};
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses the type, owner and the key to create a {@link PreferenceKey} instance.
     */
    @Override
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(getType(), getOwner(), getKey());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses the type, owner and the key for the hashCode calculation.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Comparison done with the type, owner and the key fields. Not delegated to super class.
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ModulePreference other = (ModulePreference) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        return type == other.type;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Use all fields.
     */
    @Override
    public String toString() {
        return "ModulePreference{" +
                "type=" + type +
                ", owner='" + owner + '\'' +
                ", key='" + key + '\'' +
                "} " + super.toString();
    }
}