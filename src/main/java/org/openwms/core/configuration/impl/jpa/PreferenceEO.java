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
import org.openwms.core.configuration.impl.file.PreferenceKey;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * An PreferenceEO is the persistent entity class that represents preferences in the database.
 *
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "COR_PREF_PREFERENCE")
public class PreferenceEO extends ApplicationEntity implements Serializable {

    /** Suffix for the FIND_ALL named query. Default {@value} */
    public static final String FIND_ALL = ".findAll";

    /** Suffix for the FIND_BY_OWNER named query. Default {@value} */
    public static final String FIND_BY_OWNER = ".findByOwner";

    /** Parameter name for the owner. Default {@value} */
    public static final String NQ_PARAM_OWNER = ":owner";

    /** Key field of the {@link PreferenceEO}. */
    @Column(name = "C_KEY")
    private String key;

    @Column(name = "C_OWNER")
    protected String owner;

    /** Description text of the {@link PreferenceEO}. */
    @Column(name = "C_DESCRIPTION")
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
    @Column(name = "C_CURRENT_VALUE")
    private String val;

    /** The default value of the {@link PreferenceEO}. */
    @Column(name = "C_DEF_VALUE")
    private String defValue;

    /** The minimum value of the {@link PreferenceEO}. */
    @Column(name = "C_MIN_VALUE")
    private String minValue;

    /** The maximum value of the {@link PreferenceEO}. */
    @Column(name = "C_MAX_VALUE")
    private String maxValue;

    /** Type of this preference. */
    @Convert(converter = PreferenceTypeConverter.class)
    @NotNull
    @Column(name = "C_TYPE", nullable = false)
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

    public PreferenceType getType() {
        return type;
    }

    public void setType(PreferenceType type) {
        this.type = type;
    }

    /**
     * Return a {@link PreferenceKey} of this preference.
     *
     * @return A {@link PreferenceKey}
     */
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(this.owner, this.key, this.scope);
    }

    public PreferenceEO() {
    }

    private PreferenceEO(Builder builder) {
        key = builder.key;
        owner = builder.owner;
        description = builder.description;
        fromFile = builder.fromFile;
        scope = builder.scope;
        val = builder.val;
        defValue = builder.defValue;
        minValue = builder.minValue;
        maxValue = builder.maxValue;
        type = builder.type;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String key;
        private String owner;
        private String description;
        private boolean fromFile;
        private @NotNull PropertyScope scope;
        private String val;
        private String defValue;
        private String minValue;
        private String maxValue;
        private @NotNull PreferenceType type;

        private Builder() {
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

        public Builder type(@NotNull PreferenceType val) {
            type = val;
            return this;
        }

        public PreferenceEO build() {
            return new PreferenceEO(this);
        }
    }
}