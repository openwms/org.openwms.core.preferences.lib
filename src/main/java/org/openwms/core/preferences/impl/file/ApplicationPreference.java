/*
 * Copyright 2005-2023 the original author or authors.
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openwms.core.preferences.PropertyScope;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Objects;

/**
 * An ApplicationPreference is used to store a configuration setting in application scope. <p> The table model of an ApplicationPreference
 * spans an unique key over the columns C_TYPE and C_KEY. </p> <p> It's counterpart in the context of JAXB is the applicationPreference
 * element. </p>
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@XmlType(name = "applicationPreference", namespace = "http://www.openwms.org/schema/preferences")
public class ApplicationPreference extends GenericPreference implements Serializable {

    /** Key of the preference (not nullable). */
    private String key;

    /** Create a new {@code ApplicationPreference}. Only defined by the JAXB implementation. */
    public ApplicationPreference() {
        super();
    }

    /**
     * Create a new {@code ApplicationPreference}.
     *
     * @param key the key
     * @throws IllegalArgumentException when key is {@literal null} or empty
     */
    public ApplicationPreference(String key) {
        // Called from the client-side only.
        super();
        Assert.hasText(key, "Not allowed to create an ApplicationPreference with an empty key");
        this.key = key;
    }

    private ApplicationPreference(Builder builder) {
        setValue(builder.value);
        type = builder.type;
        setDescription(builder.description);
        minimum = builder.minimum;
        maximum = builder.maximum;
        key = builder.key;
    }

    /**
     * Get the key.
     *
     * @return the key
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
     */
    @Override
    protected Object[] getFields() {
        return new Object[]{getKey(), getType()};
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses the type and key to create a {@link PreferenceKey} instance.
     *
     * @see GenericPreference#getPrefKey()
     */
    @Override
    @JsonIgnore
    public PreferenceKey getPrefKey() {
        return new PreferenceKey(":APPLICATION:", getKey(), PropertyScope.APPLICATION.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApplicationPreference that = (ApplicationPreference) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key);
    }

    @Override
    public String toString() {
        return "ApplicationPreference{" +
                "type=" + type +
                ", key='" + key + '\'' +
                "} " + super.toString();
    }

    public static final class Builder {
        private String value;
        private String type;
        private String description;
        private String minimum;
        private String maximum;
        private String key;

        private Builder() {
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder minimum(String val) {
            minimum = val;
            return this;
        }

        public Builder maximum(String val) {
            maximum = val;
            return this;
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public ApplicationPreference build() {
            return new ApplicationPreference(this);
        }
    }
}