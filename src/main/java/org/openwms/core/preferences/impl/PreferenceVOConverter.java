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
package org.openwms.core.preferences.impl;

import com.github.dozermapper.core.DozerConverter;
import org.ameba.exception.NotFoundException;
import org.openwms.core.preferences.PreferenceType;
import org.openwms.core.preferences.PropertyScope;
import org.openwms.core.preferences.api.ApplicationPreferenceVO;
import org.openwms.core.preferences.api.ModulePreferenceVO;
import org.openwms.core.preferences.api.PreferenceVO;
import org.openwms.core.preferences.api.RolePreferenceVO;
import org.openwms.core.preferences.api.UserPreferenceVO;
import org.openwms.core.preferences.impl.jpa.PreferenceEO;

import java.util.Arrays;

/**
 * A PreferenceVOConverter.
 *
 * @author Heiko Scherrer
 */
public class PreferenceVOConverter extends DozerConverter<PreferenceVO, PreferenceEO> {

    public PreferenceVOConverter() {
        super(PreferenceVO.class, PreferenceEO.class);
    }

    public static <T extends PreferenceVO> PropertyScope resolveScope(T preference) {
        return switch (preference) {
            case ApplicationPreferenceVO ignored -> PropertyScope.APPLICATION;
            case ModulePreferenceVO ignored -> PropertyScope.MODULE;
            case RolePreferenceVO ignored -> PropertyScope.ROLE;
            case UserPreferenceVO ignored -> PropertyScope.USER;
            default -> throw new IllegalStateException("Unexpected value: " + preference);
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceEO convertTo(PreferenceVO source, PreferenceEO destination) {
        if (source == null) {
            return null;
        }
        return PreferenceEO.newBuilder()
                .pKey(source.getpKey())
                .key(source.getKey())
                .owner(source.getOwner())
                .description(source.getDescription())
                .val(source.getVal() == null ? null : source.getVal().toString())
                .type(Arrays.stream(PreferenceType.values()).filter(v -> v.name().equals(source.getType())).findFirst().orElseThrow(() -> new NotFoundException("PreferenceType " + source.getType())))
                .scope(resolveScope(source))
                .groupName(source.getGroupName())
                .build();
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
            var p = new ApplicationPreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().name());
            p.setGroupName(source.getGroupName());
            return p;
        }
        if (source.getScope() == PropertyScope.MODULE) {
            var p = new ModulePreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().name());
            p.setGroupName(source.getGroupName());
            return p;
        }
        if (source.getScope() == PropertyScope.ROLE) {
            var p = new RolePreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().name());
            p.setGroupName(source.getGroupName());
            return p;
        }
        if (source.getScope() == PropertyScope.USER) {
            var p = new UserPreferenceVO();
            p.setpKey(source.getPersistentKey());
            p.setKey(source.getKey());
            p.setOwner(source.getOwner());
            p.setVal(source.getVal());
            p.setDescription(source.getDescription());
            p.setType(source.getType().name());
            p.setGroupName(source.getGroupName());
            return p;
        }
        throw new IllegalArgumentException("Source entity preferences type is unknown: " + source.getScope());
    }
}
