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


import org.openwms.core.configuration.PreferenceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

import static org.openwms.core.configuration.impl.jpa.AbstractPreferenceEO.NQ_PARAM_OWNER;

@Entity
@Table(name = "COR_PREF_BOOL")
@NamedQueries({
        @NamedQuery(name = BooleanPreferenceEO.NQ_FIND_ALL, query = "select bp from BooleanPreferenceEO bp"),
        @NamedQuery(name = BooleanPreferenceEO.NQ_FIND_BY_OWNER, query = "select bp from BooleanPreferenceEO bp where bp.owner = " + NQ_PARAM_OWNER)
})
public class BooleanPreferenceEO extends AbstractPreferenceEO implements Serializable {

    /** Query to find all {@code BooleanPreferenceEO}s. Name is {@value}. */
    public static final String NQ_FIND_ALL = "BooleanPreferenceEO" + FIND_BY_OWNER;

    /** Query to find all {@code BooleanPreferenceEO}s. Name is {@value}. */
    public static final String NQ_FIND_BY_OWNER = "BooleanPreferenceEO" + FIND_BY_OWNER;

    /** A current value of the {@link BooleanPreferenceEO}. */
    @Column(name = "C_CURRENT_VALUE")
    private boolean currentValue;

    /** A default value of the {@link BooleanPreferenceEO}. */
    @Column(name = "C_DEFAULT_VALUE")
    private boolean defaultValue;

    public BooleanPreferenceEO() {
        super();
        this.type = PreferenceType.BOOL;
    }

    public boolean isCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(boolean currentValue) {
        this.currentValue = currentValue;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return String.valueOf(currentValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BooleanPreferenceEO that = (BooleanPreferenceEO) o;
        return currentValue == that.currentValue &&
                defaultValue == that.defaultValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), currentValue, defaultValue);
    }
}
