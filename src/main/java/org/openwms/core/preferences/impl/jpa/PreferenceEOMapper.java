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

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.openwms.core.preferences.Preference;

import java.util.List;

/**
 * A PreferenceEOMapper maps between {@link PreferenceEO} (JPA entity) and {@link Preference} (domain model).
 *
 * @author Heiko Scherrer
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
interface PreferenceEOMapper {

    @Mapping(target = "PKey", source = "persistentKey")
    Preference toDomain(PreferenceEO eo);

    List<Preference> toDomainList(List<PreferenceEO> eos);

    @Mapping(target = "persistentKey", source = "PKey")
    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "ol", ignore = true)
    @Mapping(target = "new", ignore = true)
    @Mapping(target = "createDt", ignore = true)
    @Mapping(target = "lastModifiedDt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    PreferenceEO toEntity(Preference preference);

    @Mapping(target = "persistentKey", ignore = true)
    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "ol", ignore = true)
    @Mapping(target = "new", ignore = true)
    @Mapping(target = "createDt", ignore = true)
    @Mapping(target = "lastModifiedDt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    PreferenceEO updateEntity(Preference preference, @MappingTarget PreferenceEO eo);
}
