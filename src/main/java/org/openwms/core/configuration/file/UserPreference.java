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
 * An UserPreference is used to store settings specific to an {@code User}. It is always assigned to a particular {@code User} and
 * not accessible from, nor valid for, other {@code User}s. UserPreferences cannot be overruled by any other type of
 * {@link Preferences}.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@XmlType(name = "userPreference", namespace = "http://www.openwms.org/schema/usermanagement")
@Entity
@Table(name = "COR_USER_PREFERENCE", uniqueConstraints = @UniqueConstraint(columnNames = {"C_TYPE", "C_OWNER", "C_KEY"}))
@NamedQueries({
        @NamedQuery(name = UserPreference.NQ_FIND_BY_OWNER, query = "select up from UserPreference up where up.owner = :owner") })
public class UserPreference extends GenericPreference implements Serializable {

    /** Type of this preference. */
    @XmlTransient
    @Enumerated(EnumType.STRING)
    @Column(name = "C_TYPE")
    private PropertyScope type = PropertyScope.USER;

    /** Owner of the {@link GenericPreference}. */
    @XmlAttribute(name = "owner", required = true)
    @Column(name = "C_OWNER")
    private String owner;

    /** Key value of the {@link GenericPreference}. */
    @XmlAttribute(name = "key", required = true)
    @Column(name = "C_KEY")
    private String key;

    /**
     * Query to find <strong>all</strong> {@link UserPreference}s of an {@code User}. <li>Query parameter name
     * <strong>owner</strong> : The userName of the {@code User} to search for.</li><br />
     * Name is {@value} .
     */
    public static final String NQ_FIND_BY_OWNER = "UserPreference" + FIND_BY_OWNER;

    /** Create a new UserPreference. Defined for the JAXB implementation. */
    public UserPreference() {
        super();
    }

    /**
     * Create a new UserPreference.
     *
     * @param owner The User's username is set as owner of this preference
     * @param key The key of this preference
     * @throws IllegalArgumentException when owner or key is {@literal null} or empty
     */
    public UserPreference(String owner, String key) {
        // Called from the client-side only.
        super();
        Assert.hasText(owner, "Not allowed to create an UserPreference with an empty owner");
        Assert.hasText(key, "Not allowed to create an UserPreference with an empty key");
        this.owner = owner;
        this.key = key;
    }

    /**
     * Get the owner.
     *
     * @return the owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get the key.
     *
     * @return the key.
     */
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     *
     * @see GenericPreference#getType()
     */
    @Override
    public PropertyScope getType() {
        return PropertyScope.USER;
    }

    /**
     * {@inheritDoc}
     *
     * @see GenericPreference#getFields()
     */
    @Override
    protected Object[] getFields() {
        return new Object[]{getType(), getOwner(), getKey()};
    }

    /**
     * {@inheritDoc}
     *
     * @see GenericPreference#getPrefKey()
     */
    @Override
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(getType(), getOwner(), getKey());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses key, owner and type for hashCode calculation.
     *
     * @see java.lang.Object#hashCode()
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
     * Comparison done with key, owner and type fields. Not delegated to super class.
     *
     * @see java.lang.Object#equals(java.lang.Object)
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
        UserPreference other = (UserPreference) obj;
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
        return "UserPreference{" +
                "type=" + type +
                ", owner='" + owner + '\'' +
                ", key='" + key + '\'' +
                "} " + super.toString();
    }
}