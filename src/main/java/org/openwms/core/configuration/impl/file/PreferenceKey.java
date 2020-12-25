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
package org.openwms.core.configuration.impl.file;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A PreferenceKey can be used as a unique key object to group preference instances. Unfortunately this class cannot be implemented as a JPA
 * embeddable, because of JPA does not support inheritance of embeddables.
 * 
 * @author Heiko Scherrer
 */
class PreferenceKey implements Serializable {

    private Serializable[] fields;

    /**
     * Create a new {@code PreferenceKey} with a variable array of fields.
     * 
     * @param fields
     *            The array of fields to store as keys
     */
    public PreferenceKey(Serializable... fields) {
        this.fields = fields;
    }

    /**
     * {@inheritDoc}
     * 
     * Use of all fields for calculation of the hashCode.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(fields);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * Use all fields for comparison.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PreferenceKey other = (PreferenceKey) obj;
        return Arrays.equals(fields, other.fields);
    }
}