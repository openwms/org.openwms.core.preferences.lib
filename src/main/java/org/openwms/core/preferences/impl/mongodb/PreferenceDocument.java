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
package org.openwms.core.preferences.impl.mongodb;

import org.openwms.core.preferences.PreferenceType;
import org.openwms.core.preferences.PropertyScope;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A PreferenceDocument is the MongoDB document representation of a Preference.
 *
 * @author Heiko Scherrer
 */
@Document(collection = "cor_pref_preference")
@CompoundIndex(name = "uc_preference", def = "{'key': 1, 'owner': 1}", unique = true)
public class PreferenceDocument implements Serializable {

    @Id
    private String pKey;

    @Field("key")
    private String key;

    @Field("owner")
    private String owner;

    @Field("description")
    private String description;

    @Field("currentValue")
    private String val;

    @Field("defaultValue")
    private String defValue;

    @Field("minValue")
    private String minValue;

    @Field("maxValue")
    private String maxValue;

    @Field("groupName")
    private String groupName = "GLOBAL";

    @Field("scope")
    private PropertyScope scope;

    @Field("type")
    private PreferenceType type;

    @Field("fromFile")
    private boolean fromFile;

    @Field("lastModifiedDt")
    private LocalDateTime lastModifiedDt;

    public PreferenceDocument() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreferenceDocument that)) return false;
        return Objects.equals(key, that.key) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, owner);
    }
}
