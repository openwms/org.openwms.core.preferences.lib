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
    private boolean fromFile = true;

    /** Scope of this preference. */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "C_SCOPE", nullable = false)
    protected PropertyScope scope;

    /** A current value of the {@link PreferenceEO}. */
    @Column(name = "C_CURRENT_VALUE")
    private String currentValue;

    /** The default value of the {@link PreferenceEO}. */
    @Column(name = "C_MIN_VALUE")
    private String defValue;

    /** The minimum value of the {@link PreferenceEO}. */
    @Column(name = "C_MIN_VALUE")
    private String minValue;

    /** The maximum value of the {@link PreferenceEO}. */
    @Column(name = "C_MAX_VALUE")
    private String maxValue;

    /** Type of this preference. */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "C_TYPE", nullable = false)
    protected PreferenceType type;

    private PreferenceEO(Builder builder) {
        key = builder.key;
        owner = builder.owner;
        description = builder.description;
        fromFile = builder.fromFile;
        scope = builder.scope;
        currentValue = builder.currentValue;
        defValue = builder.defValue;
        minValue = builder.minValue;
        maxValue = builder.maxValue;
        type = builder.type;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Return a {@link PreferenceKey} of this preference.
     *
     * @return A {@link PreferenceKey}
     */
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(this.owner, this.key, this.scope);
    }

    public String getKey() {
        return key;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFromFile() {
        return fromFile;
    }

    public PropertyScope getScope() {
        return scope;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public String getDefValue() {
        return defValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public PreferenceType getType() {
        return type;
    }

    public static final class Builder {
        private String key;
        private String owner;
        private String description;
        private boolean fromFile;
        private @NotNull PropertyScope scope;
        private String currentValue;
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

        public Builder currentValue(String val) {
            currentValue = val;
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