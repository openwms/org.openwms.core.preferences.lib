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
package org.openwms.core.configuration.impl;

import org.ameba.exception.NotFoundException;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;
import org.openwms.core.configuration.PreferenceType;
import org.openwms.core.configuration.PropertyScope;
import org.openwms.core.configuration.impl.file.ApplicationPreference;
import org.openwms.core.configuration.impl.file.GenericPreference;
import org.openwms.core.configuration.impl.file.ModulePreference;
import org.openwms.core.configuration.impl.file.ObjectFactory;
import org.openwms.core.configuration.impl.file.RolePreference;
import org.openwms.core.configuration.impl.file.UserPreference;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;

import java.util.Arrays;

/**
 * A PreferenceConverter.
 *
 * @author Heiko Scherrer
 */
public class PreferenceConverter extends DozerConverter<GenericPreference, PreferenceEO> implements MapperAware {

    private Mapper mapper;

    /**
     * {@inheritDoc}
     */
    public PreferenceConverter() {
        super(GenericPreference.class, PreferenceEO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceEO convertTo(GenericPreference source, PreferenceEO destination) {
        if (source == null) {
            return null;
        }
        if (source.getClass().equals(ApplicationPreference.class)) {
            ApplicationPreference p = (ApplicationPreference) source;
            return PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .description(p.getDescription())
                    .val(p.getValue())
                    .fromFile(true)
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.getClazz().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.APPLICATION)
                    .minValue(p.getMinimum())
                    .maxValue(p.getMaximum())
                    .build();
        }
        if (source.getClass().equals(ModulePreference.class)) {
            ModulePreference p = (ModulePreference) source;
            return PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .description(p.getDescription())
                    .val(p.getValue())
                    .fromFile(true)
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.getClazz().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.MODULE)
                    .minValue(p.getMinimum())
                    .maxValue(p.getMaximum())
                    .build();
        }
        if (source.getClass().equals(RolePreference.class)) {
            RolePreference p = (RolePreference) source;
            return PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .description(p.getDescription())
                    .val(p.getValue())
                    .fromFile(true)
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.getClazz().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.ROLE)
                    .minValue(p.getMinimum())
                    .maxValue(p.getMaximum())
                    .build();
        }
        if (source.getClass().equals(UserPreference.class)) {
            UserPreference p = (UserPreference) source;
            return PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .description(p.getDescription())
                    .val(p.getValue())
                    .fromFile(true)
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.getClazz().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.USER)
                    .minValue(p.getMinimum())
                    .maxValue(p.getMaximum())
                    .build();
        }
        throw new IllegalArgumentException("Source XML preferences type is unknown: " + source.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericPreference convertFrom(PreferenceEO source, GenericPreference destination) {
        if (source == null) {
            return null;
        }
        if (source.getScope() == PropertyScope.APPLICATION) {
            ApplicationPreference p = new ObjectFactory().createApplicationPreference();
            p.setKey(source.getKey());
            p.setValue(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().getClazz());
            p.setMinimum(source.getMinValue());
            p.setMaximum(source.getMaxValue());
            return p;
        }
        if (source.getScope() == PropertyScope.MODULE) {
            ModulePreference p = new ObjectFactory().createModulePreference();
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setValue(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().getClazz());
            p.setMinimum(source.getMinValue());
            p.setMaximum(source.getMaxValue());
            return p;
        }
        if (source.getScope() == PropertyScope.ROLE) {
            RolePreference p = new ObjectFactory().createRolePreference();
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setValue(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().getClazz());
            p.setMinimum(source.getMinValue());
            p.setMaximum(source.getMaxValue());
            return p;
        }
        if (source.getScope() == PropertyScope.USER) {
            UserPreference p = new ObjectFactory().createUserPreference();
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setValue(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().getClazz());
            p.setMinimum(source.getMinValue());
            p.setMaximum(source.getMaxValue());
            return p;
        }
        throw new IllegalArgumentException("Source entity preferences type is unknown: " + source.getScope());
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
