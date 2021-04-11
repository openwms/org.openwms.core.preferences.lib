/*
 * Copyright 2005-2021 the original author or authors.
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
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MapperAware;
import org.ameba.exception.NotFoundException;
import org.openwms.core.configuration.PreferenceType;
import org.openwms.core.configuration.PropertyScope;
import org.openwms.core.configuration.api.ApplicationPreferenceVO;
import org.openwms.core.configuration.api.ModulePreferenceVO;
import org.openwms.core.configuration.api.PreferenceVO;
import org.openwms.core.configuration.api.RolePreferenceVO;
import org.openwms.core.configuration.api.UserPreferenceVO;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;

import java.util.Arrays;

/**
 * A PreferenceVOConverter.
 *
 * @author Heiko Scherrer
 */
public class PreferenceVOConverter extends DozerConverter<PreferenceVO, PreferenceEO> implements MapperAware {

    private Mapper mapper;

    /**
     * {@inheritDoc}
     */
    public PreferenceVOConverter() {
        super(PreferenceVO.class, PreferenceEO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceEO convertTo(PreferenceVO source, PreferenceEO destination) {
        if (source == null) {
            return null;
        }
        if (ApplicationPreferenceVO.class.equals(source.getClass())) {
            ApplicationPreferenceVO p = (ApplicationPreferenceVO) source;
            PreferenceEO eo = PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .description(p.getDescription())
                    .val(p.getVal().toString())
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.name().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.APPLICATION)
                    .build();
            eo.setPersistentKey(p.getpKey());
            return eo;
        }
        if (ModulePreferenceVO.class.equals(source.getClass())) {
            ModulePreferenceVO p = (ModulePreferenceVO) source;
            PreferenceEO eo = PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .description(p.getDescription())
                    .val(p.getVal().toString())
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.name().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.MODULE)
                    .build();
            eo.setPersistentKey(p.getpKey());
            return eo;
        }
        if (RolePreferenceVO.class.equals(source.getClass())) {
            RolePreferenceVO p = (RolePreferenceVO) source;
            PreferenceEO eo = PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .description(p.getDescription())
                    .val(p.getVal().toString())
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.name().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.ROLE)
                    .build();
            eo.setPersistentKey(p.getpKey());
            return eo;
        }
        if (UserPreferenceVO.class.equals(source.getClass())) {
            UserPreferenceVO p = (UserPreferenceVO) source;
            PreferenceEO eo = PreferenceEO.newBuilder()
                    .key(p.getKey())
                    .owner(p.getOwner())
                    .description(p.getDescription())
                    .val(p.getVal().toString())
                    .type(Arrays.stream(PreferenceType.values()).filter(v -> v.name().equals(p.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + p.getType())))
                    .scope(PropertyScope.USER)
                    .build();
            eo.setPersistentKey(p.getpKey());
            return eo;
        }
        throw new IllegalArgumentException("Source XML preferences type is unknown: " + source.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceVO convertFrom(PreferenceEO source, PreferenceVO destination) {
        if (source == null) {
            return null;
        }
        if (source.getScope() == PropertyScope.APPLICATION) {
            ApplicationPreferenceVO p = new ApplicationPreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(mapper.map(source.getType(), String.class));
            return p;
        }
        if (source.getScope() == PropertyScope.MODULE) {
            ModulePreferenceVO p = new ModulePreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(mapper.map(source.getType(), String.class));
            return p;
        }
        if (source.getScope() == PropertyScope.ROLE) {
            RolePreferenceVO p = new RolePreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(mapper.map(source.getType(), String.class));
            return p;
        }
        if (source.getScope() == PropertyScope.USER) {
            UserPreferenceVO p = new UserPreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(mapper.map(source.getType(), String.class));
            return p;
        }
        throw new IllegalArgumentException("Source entity preferences type is unknown: " + source.getScope());
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
