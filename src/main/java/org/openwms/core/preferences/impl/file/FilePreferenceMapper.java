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
package org.openwms.core.preferences.impl.file;

import org.ameba.exception.NotFoundException;
import org.mapstruct.Mapper;
import org.openwms.core.preferences.Preference;
import org.openwms.core.preferences.PreferenceType;
import org.openwms.core.preferences.PropertyScope;

import java.util.Arrays;

/**
 * A FilePreferenceMapper maps from {@link GenericPreference} (XML file) to {@link Preference} (domain model).
 * Replaces the Dozer-based {@code PreferenceConverter}.
 *
 * @author Heiko Scherrer
 */
@Mapper(componentModel = "spring")
public abstract class FilePreferenceMapper {

    public Preference toDomain(GenericPreference source) {
        if (source == null) {
            return null;
        }
        return switch (source) {
            case ApplicationPreference p -> createBuilder(p)
                    .key(p.getKey())
                    .scope(PropertyScope.APPLICATION)
                    .build();
            case ModulePreference p -> createBuilder(p)
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .scope(PropertyScope.MODULE)
                    .build();
            case RolePreference p -> createBuilder(p)
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .scope(PropertyScope.ROLE)
                    .build();
            case UserPreference p -> createBuilder(p)
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .scope(PropertyScope.USER)
                    .build();
            default -> throw new IllegalArgumentException("Source XML preferences type is unknown: " + source.getClass());
        };
    }

    private Preference.Builder createBuilder(GenericPreference p) {
        return Preference.newBuilder()
                .description(p.getDescription())
                .val(p.getValue())
                .fromFile(true)
                .type(Arrays.stream(PreferenceType.values())
                        .filter(v -> v.name().equals(p.getType()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                .minValue(p.getMinimum())
                .maxValue(p.getMaximum());
    }
}
