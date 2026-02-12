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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.openwms.core.preferences.Preference;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * A PreferenceDocumentMapper maps between {@link PreferenceDocument} (MongoDB document) and {@link Preference} (domain model).
 *
 * @author Heiko Scherrer
 */
@Profile("MONGODB")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PreferenceDocumentMapper {

    @Mapping(target = "pKey", source = "PKey")
    Preference toDomain(PreferenceDocument document);

    List<Preference> toDomainList(List<PreferenceDocument> documents);

    PreferenceDocument toDocument(Preference preference);

    PreferenceDocument updateDocument(Preference preference, @MappingTarget PreferenceDocument document);
}
