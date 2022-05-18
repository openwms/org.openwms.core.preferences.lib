/*
 * Copyright 2005-2022 the original author or authors.
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
package org.openwms.core.configuration;

import java.util.Arrays;

/**
 * A PreferenceType.
 *
 * @author Heiko Scherrer
 */
public enum PreferenceType {

    /** Float presentation. */
    FLOAT(Float.class.getName()),

    /** String representation. */
    STRING(String.class.getName()),

    /** Integer representation. */
    INT(Integer.class.getName()),

    /** Any Object. */
    OBJECT(Object.class.getName()),

    /** Boolean type. */
    BOOL(Boolean.class.getName()),

    /** Boolean type. */
    JSON("JSON");

    private final String clazz;

    PreferenceType(String clazz) {
        this.clazz = clazz;
    }

    public String getClazz() {
        return clazz;
    }

    public static PreferenceType getForClazz(String clazz) {
        return Arrays.stream(PreferenceType.values()).filter(p -> p.getClazz().equals(clazz)).findFirst().orElse(null);
    }
}