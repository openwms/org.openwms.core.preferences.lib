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

import org.openwms.core.preferences.PropertyScope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converter to read PropertyScope from MongoDB String representation.
 *
 * @author Heiko Scherrer
 */
@ReadingConverter
public class PropertyScopeReadConverter implements Converter<String, PropertyScope> {

    @Override
    public PropertyScope convert(String source) {
        if (source == null) {
            return null;
        }
        return PropertyScope.of(source);
    }
}
