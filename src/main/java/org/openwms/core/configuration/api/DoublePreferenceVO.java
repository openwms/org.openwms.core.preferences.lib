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
package org.openwms.core.configuration.api;

import java.util.Objects;

/**
 * A DoublePreferenceVO.
 *
 * @author Heiko Scherrer
 */
public class DoublePreferenceVO extends AbstractPreferenceVO<DoublePreferenceVO> {

    private double value;

    private double minimum;

    private double maximum;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DoublePreferenceVO that = (DoublePreferenceVO) o;
        return Double.compare(that.value, value) == 0 &&
                Double.compare(that.minimum, minimum) == 0 &&
                Double.compare(that.maximum, maximum) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, minimum, maximum);
    }
}
