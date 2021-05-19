/*
 * Copyright 2005-2021 the original author or authors.
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
package org.openwms.core.configuration.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.ameba.http.AbstractBase;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * A PreferenceVO.
 *
 * @author Heiko Scherrer
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class PreferenceVO extends AbstractBase<PreferenceVO> {

    @JsonProperty("pKey")
    private String pKey;

    @JsonProperty("key")
    private String key;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("description")
    private String description;

    @JsonProperty("value")
    private Serializable val;

    @JsonProperty("type")
    private String type;

    public String getpKey() {
        return pKey;
    }

    public void setpKey(String pKey) {
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

    public Serializable getVal() {
        return val;
    }

    public Optional<Serializable> getOptionalVal() {
        return Optional.ofNullable(val);
    }

    public void setVal(Serializable val) {
        this.val = val;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PreferenceVO that = (PreferenceVO) o;
        return Objects.equals(pKey, that.pKey) &&
                Objects.equals(key, that.key) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(description, that.description) &&
                Objects.equals(val, that.val) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pKey, key, owner, description, val, type);
    }
}
