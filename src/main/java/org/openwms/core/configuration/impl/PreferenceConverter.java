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
package org.openwms.core.configuration.impl;

import com.github.dozermapper.core.DozerConverter;
import org.ameba.exception.NotFoundException;
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
public class PreferenceConverter extends DozerConverter<GenericPreference, PreferenceEO> {

    public PreferenceConverter() {
        super(GenericPreference.class, PreferenceEO.class);
    }

    private PreferenceEO.Builder createBuilder(GenericPreference p) {
        return PreferenceEO.newBuilder()
                .description(p.getDescription())
                .val(p.getValue())
                .fromFile(true)
                .type(Arrays.stream(PreferenceType.values()).filter(v -> v.name().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                .scope(PropertyScope.APPLICATION)
                .minValue(p.getMinimum())
                .maxValue(p.getMaximum());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceEO convertTo(GenericPreference source, PreferenceEO destination) {
        if (source == null) {
            return null;
        }
        return switch(source) {
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
}
