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
package org.openwms.core.preferences.impl.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.ameba.integration.jpa.ApplicationEntity;
import org.openwms.core.preferences.PreferenceType;
import org.openwms.core.preferences.PropertyScope;
import org.openwms.core.preferences.impl.file.PreferenceKey;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

import static org.openwms.core.preferences.api.PreferencesConstants.LENGTH_DESCRIPTION;
import static org.openwms.core.preferences.api.PreferencesConstants.LENGTH_GROUP;
import static org.openwms.core.preferences.api.PreferencesConstants.LENGTH_KEY;
import static org.openwms.core.preferences.api.PreferencesConstants.LENGTH_OWNER;
import static org.openwms.core.preferences.api.PreferencesConstants.LENGTH_TYPE;
import static org.openwms.core.preferences.api.PreferencesConstants.LENGTH_VALUE;

/**
 * An PreferenceEO is the persistent entity class that represents preferences in the database.
 *
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "COR_PREF_PREFERENCE", uniqueConstraints = @UniqueConstraint(name = "UC_PREFERENCE", columnNames = {"C_KEY", "C_OWNER"}))
public class PreferenceEO extends ApplicationEntity implements Serializable {

    /** Suffix for the FIND_ALL named query. Default {@value} */
    public static final String FIND_ALL = ".findAll";

    /** Key field of the {@link PreferenceEO}. */
    @Column(name = "C_KEY", length = LENGTH_KEY)
    @NotBlank
    @Size(min = 1, max = LENGTH_KEY)
    private String key;

    @Column(name = "C_OWNER", length = LENGTH_OWNER)
    @Size(max = LENGTH_OWNER)
    protected String owner;

    /** Description text of the {@link PreferenceEO}. */
    @Column(name = "C_DESCRIPTION", length = LENGTH_DESCRIPTION)
    @Size(max = LENGTH_DESCRIPTION)
    protected String description;

    /** Flag to remember if the preference was originally imported from a file. */
    @Column(name = "C_FROM_FILE")
    private boolean fromFile;

    /** Scope of this preference. */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "C_SCOPE", nullable = false)
    protected PropertyScope scope;

    /** A current value of the {@link PreferenceEO}. */
    @Column(name = "C_CURRENT_VALUE", length = LENGTH_VALUE)
    @Size(max = LENGTH_VALUE)
    private String val;

    /** The default value of the {@link PreferenceEO}. */
    @Column(name = "C_DEF_VALUE", length = LENGTH_VALUE)
    @Size(max = LENGTH_VALUE)
    private String defValue;

    /** The minimum value of the {@link PreferenceEO}. */
    @Column(name = "C_MIN_VALUE", length = LENGTH_VALUE)
    @Size(max = LENGTH_VALUE)
    private String minValue;

    /** The maximum value of the {@link PreferenceEO}. */
    @Column(name = "C_MAX_VALUE", length = LENGTH_VALUE)
    @Size(max = LENGTH_VALUE)
    private String maxValue;

    /** The name of the group the {@link PreferenceEO} is assigned to. */
    @Column(name = "C_GROUP_NAME", length = LENGTH_GROUP)
    @Size(max = LENGTH_GROUP)
    private String groupName = "GLOBAL";

    /** Type of this preference. */
    @Convert(converter = PreferenceTypeConverter.class)
    @Column(name = "C_TYPE", nullable = false, length = LENGTH_TYPE)
    @NotNull
    protected PreferenceType type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public boolean isFromFile() {
        return fromFile;
    }

    public void setFromFile(boolean fromFile) {
        this.fromFile = fromFile;
    }

    public PropertyScope getScope() {
        return scope;
    }

    public void setScope(PropertyScope scope) {
        this.scope = scope;
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public PreferenceType getType() {
        return type;
    }

    public void setType(PreferenceType type) {
        this.type = type;
    }

    @Override
    public void setPersistentKey(String pKey) {
        super.setPersistentKey(pKey);
    }

    /**
     * Return a {@link PreferenceKey} of this preference.
     *
     * @return A {@link PreferenceKey}
     */
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(this.owner == null ? ":APPLICATION:" : this.owner, this.key, this.scope.name());
    }

    public PreferenceEO() {
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreferenceEO that)) return false;
        if (!super.equals(o)) return false;
        return fromFile == that.fromFile && Objects.equals(key, that.key) && Objects.equals(owner, that.owner) && Objects.equals(description, that.description) && scope == that.scope && Objects.equals(val, that.val) && Objects.equals(defValue, that.defValue) && Objects.equals(minValue, that.minValue) && Objects.equals(maxValue, that.maxValue) && Objects.equals(groupName, that.groupName) && type == that.type;
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, owner, description, fromFile, scope, val, defValue, minValue, maxValue, groupName, type);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", PreferenceEO.class.getSimpleName() + "[", "]")
                .add("key='" + key + "'")
                .add("pKey='" + getPersistentKey() + "'")
                .add("owner='" + owner + "'")
                .add("description='" + description + "'")
                .add("fromFile=" + fromFile)
                .add("scope=" + scope)
                .add("val='" + val + "'")
                .add("defValue='" + defValue + "'")
                .add("minValue='" + minValue + "'")
                .add("maxValue='" + maxValue + "'")
                .add("groupName='" + groupName + "'")
                .add("type=" + type)
                .toString();
    }

    private PreferenceEO(Builder builder) {
        key = builder.key;
        if (builder.hasPKey()) {
            setPersistentKey(builder.pKey);
        }
        owner = builder.owner;
        description = builder.description;
        fromFile = builder.fromFile;
        scope = builder.scope;
        val = builder.val;
        defValue = builder.defValue;
        minValue = builder.minValue;
        maxValue = builder.maxValue;
        groupName = builder.groupName;
        type = builder.type;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String key;
        private String pKey;
        private String owner;
        private String description;
        private boolean fromFile;
        private @NotNull PropertyScope scope;
        private String val;
        private String defValue;
        private String minValue;
        private String maxValue;
        private String groupName;
        private @NotNull PreferenceType type;

        private Builder() {
        }

        public Builder pKey(String val) {
            pKey = val;
            return this;
        }

        public boolean hasPKey() {
            return pKey != null && !pKey.isEmpty();
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public Builder owner(String val) {
            owner = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder fromFile(boolean val) {
            fromFile = val;
            return this;
        }

        public Builder scope(@NotNull PropertyScope val) {
            scope = val;
            return this;
        }

        public Builder val(String val) {
            this.val = val;
            return this;
        }

        public Builder defValue(String val) {
            defValue = val;
            return this;
        }

        public Builder minValue(String val) {
            minValue = val;
            return this;
        }

        public Builder maxValue(String val) {
            maxValue = val;
            return this;
        }

        public Builder groupName(String val) {
            groupName = val;
            return this;
        }

        public Builder type(@NotNull PreferenceType val) {
            type = val;
            return this;
        }

        public PreferenceEO build() {
            return new PreferenceEO(this);
        }
    }
}