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

import org.junit.jupiter.api.Test;
import org.openwms.core.configuration.impl.file.UserPreference;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A UserPreferenceTest.
 *
 * @author Heiko Scherrer
 */
class UserPreferenceTest {

    @Test void testCreationNegative1() {
        assertThatThrownBy(
                () -> new UserPreference(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testCreationNegative2() {
        assertThatThrownBy(
                () -> new UserPreference("test", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testCreationNegative3() {
        assertThatThrownBy(
                () -> new UserPreference(null, "test"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testCreationNegative4() {
        assertThatThrownBy(
                () -> new UserPreference("", "test"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
