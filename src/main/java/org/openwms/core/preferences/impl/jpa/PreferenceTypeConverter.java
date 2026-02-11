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
package org.openwms.core.preferences.impl.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.openwms.core.preferences.PreferenceType;

/**
 * A PreferenceTypeConverter.
 *
 * @author Heiko Scherrer
 */
@Converter
public class PreferenceTypeConverter implements AttributeConverter<PreferenceType, String> {

    @Override
    public String convertToDatabaseColumn(PreferenceType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getClazz();
    }

    @Override
    public PreferenceType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return PreferenceType.getForClazz(dbData);
    }
}
