/*
 * Copyright 2005-2025 the original author or authors.
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
 * A RolePreference is used to provide settings specific to an {@code Role} . These kind of {@link Preferences} is valid for the assigned
 * Role only. {@code User}s assigned to a {@code Role} inherit these RolePreferences but a RolePreference can be overruled by an {@link
 * UserPreference}. RolePreferences can be defined within a preferences file but also be created with the UI.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@XmlType(name = "rolePreference", namespace = "http://www.openwms.org/schema/usermanagement")
public class RolePreference extends GenericPreference implements Serializable {

    /**
     * Owner of the {@code RolePreference}.
     */
    private String owner;
    /**
     * Key value of the {@link RolePreference}.
     */
    private String key;

    /**
     * Create a new RolePreference. Defined for the JAXB implementation.
     */
    public RolePreference() {
        super();
    }

    /**
     * Create a new RolePreference.
     *
     * @param rolename The name of the Role that owns this preference
     * @param key the key
     * @throws IllegalArgumentException when rolename or key is {@literal null} or empty
     */
    public RolePreference(String rolename, String key) {
        // Called from the client.
        super();
        Assert.hasText(owner, "Not allowed to create a RolePreference with an empty rolename");
        Assert.hasText(key, "Not allowed to create a RolePreference with an empty key");
        owner = rolename;
        this.key = key;
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
     * Get the name of the {@code Role} as String.
     *
     * @return the rolename.
     */
    @XmlAttribute(name = "owner", required = true)
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
     * <p>
     * Uses the type, owner and the key to create a {@link PreferenceKey} instance.
     *
     * @see GenericPreference#getPrefKey()
     */
    @Override
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(getOwner(), getKey(), PropertyScope.ROLE.name());
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
        RolePreference other = (RolePreference) obj;
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
        return "RolePreference{" +
                "type=" + type +
                ", owner='" + owner + '\'' +
                ", key='" + key + '\'' +
                "} " + super.toString();
    }
}
