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
package org.openwms.core.preferences;

import org.openwms.core.preferences.impl.file.PreferenceKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A Preference is the persistence-agnostic domain model representing a configuration preference.
 *
 * @author Heiko Scherrer
 */
public class Preference implements Serializable {

    private String pKey;
    private String key;
    private String owner;
    private String description;
    private String val;
    private String defValue;
    private String minValue;
    private String maxValue;
    private String groupName = "GLOBAL";
    private PropertyScope scope;
    private PreferenceType type;
    private boolean fromFile;
    private LocalDateTime lastModifiedDt;

    public Preference() {
    }

    private Preference(Builder builder) {
        if (builder.hasPKey()) {
            this.pKey = builder.pKey;
        }
        this.key = builder.key;
        this.owner = builder.owner;
        this.description = builder.description;
        this.val = builder.val;
        this.defValue = builder.defValue;
        this.minValue = builder.minValue;
        this.maxValue = builder.maxValue;
        this.groupName = builder.groupName;
        this.scope = builder.scope;
        this.type = builder.type;
        this.fromFile = builder.fromFile;
        this.lastModifiedDt = builder.lastModifiedDt;
    }

    public String getPKey() {
        return pKey;
    }

    public void setPKey(String pKey) {
        this.pKey = pKey;
    }

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

    public PropertyScope getScope() {
        return scope;
    }

    public void setScope(PropertyScope scope) {
        this.scope = scope;
    }

    public PreferenceType getType() {
        return type;
    }

    public void setType(PreferenceType type) {
        this.type = type;
    }

    public boolean isFromFile() {
        return fromFile;
    }

    public void setFromFile(boolean fromFile) {
        this.fromFile = fromFile;
    }

    public LocalDateTime getLastModifiedDt() {
        return lastModifiedDt;
    }

    public void setLastModifiedDt(LocalDateTime lastModifiedDt) {
        this.lastModifiedDt = lastModifiedDt;
    }

    public PreferenceKey getPrefKey() {
        return new PreferenceKey(this.owner == null ? ":APPLICATION:" : this.owner, this.key, this.scope.name());
    }

    public String getPersistentKey() {
        return pKey;
    }

    public boolean hasPersistentKey() {
        return pKey != null && !pKey.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Preference that)) return false;
        return fromFile == that.fromFile && Objects.equals(key, that.key) && Objects.equals(owner, that.owner) && Objects.equals(description, that.description) && scope == that.scope && Objects.equals(val, that.val) && Objects.equals(defValue, that.defValue) && Objects.equals(minValue, that.minValue) && Objects.equals(maxValue, that.maxValue) && Objects.equals(groupName, that.groupName) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, owner, description, fromFile, scope, val, defValue, minValue, maxValue, groupName, type);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Preference.class.getSimpleName() + "[", "]")
                .add("key='" + key + "'")
                .add("pKey='" + pKey + "'")
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

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String pKey;
        private String key;
        private String owner;
        private String description;
        private String val;
        private String defValue;
        private String minValue;
        private String maxValue;
        private String groupName;
        private PropertyScope scope;
        private PreferenceType type;
        private boolean fromFile;
        private LocalDateTime lastModifiedDt;

        private Builder() {
        }

        public boolean hasPKey() {
            return pKey != null && !pKey.isEmpty();
        }

        public Builder pKey(String val) {
            pKey = val;
            return this;
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

        public Builder scope(PropertyScope val) {
            scope = val;
            return this;
        }

        public Builder type(PreferenceType val) {
            type = val;
            return this;
        }

        public Builder fromFile(boolean val) {
            fromFile = val;
            return this;
        }

        public Builder lastModifiedDt(LocalDateTime val) {
            lastModifiedDt = val;
            return this;
        }

        public Preference build() {
            return new Preference(this);
        }
    }
}
