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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import org.openwms.core.preferences.PropertyScope;
import org.springframework.util.Assert;

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
public class UserPreference extends GenericPreference implements Serializable {

    /** Owner of the {@link UserPreference}. */
    private String owner;

    /** Key value of the {@link UserPreference}. */
    private String key;

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
    @XmlAttribute(name = "owner", required = true)
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Get the key.
     *
     * @return the key.
     */
    @XmlAttribute(name = "key", required = true)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     *
     * @see GenericPreference#getFields()
     */
    @Override
    protected Object[] getFields() {
        return new Object[]{getOwner(), getKey(), getType()};
    }

    /**
     * {@inheritDoc}
     *
     * @see GenericPreference#getPrefKey()
     */
    @Override
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(getOwner(), getKey(), PropertyScope.USER.name());
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
        return type.equals(other.type);
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