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
package org.openwms.core.preferences;

import org.ameba.exception.NotFoundException;
import org.mapstruct.Mapper;
import org.openwms.core.preferences.api.ApplicationPreferenceVO;
import org.openwms.core.preferences.api.ModulePreferenceVO;
import org.openwms.core.preferences.api.PreferenceVO;
import org.openwms.core.preferences.api.RolePreferenceVO;
import org.openwms.core.preferences.api.UserPreferenceVO;
import org.openwms.core.preferences.api.messages.PreferenceMO;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A PreferenceVOMapper maps between {@link Preference} (domain) and {@link PreferenceVO} (API VO).
 * Replaces the Dozer-based {@code PreferenceVOConverter}.
 *
 * @author Heiko Scherrer
 */
@Mapper(componentModel = "spring")
public abstract class PreferenceVOMapper {

    public static <T extends PreferenceVO> PropertyScope resolveScope(T preference) {
        return switch (preference) {
            case ApplicationPreferenceVO ignored -> PropertyScope.APPLICATION;
            case ModulePreferenceVO ignored -> PropertyScope.MODULE;
            case RolePreferenceVO ignored -> PropertyScope.ROLE;
            case UserPreferenceVO ignored -> PropertyScope.USER;
            default -> throw new IllegalStateException("Unexpected value: " + preference);
        };
    }

    public PreferenceVO toVO(Preference source) {
        if (source == null) {
            return null;
        }
        PreferenceVO p;
        switch (source.getScope()) {
            case APPLICATION -> p = new ApplicationPreferenceVO();
            case MODULE -> p = new ModulePreferenceVO();
            case ROLE -> p = new RolePreferenceVO();
            case USER -> p = new UserPreferenceVO();
            default -> throw new IllegalArgumentException("Source entity preferences type is unknown: " + source.getScope());
        }
        p.setpKey(source.getPersistentKey());
        p.setKey(source.getKey());
        p.setOwner(source.getOwner());
        p.setVal(source.getVal());
        p.setDescription(source.getDescription());
        p.setType(source.getType() != null ? source.getType().name() : null);
        p.setGroupName(source.getGroupName());
        return p;
    }

    public UserPreferenceVO toUserVO(Preference source) {
        if (source == null) {
            return null;
        }
        var p = new UserPreferenceVO();
        p.setpKey(source.getPersistentKey());
        p.setKey(source.getKey());
        p.setOwner(source.getOwner());
        p.setVal(source.getVal());
        p.setDescription(source.getDescription());
        p.setType(source.getType() != null ? source.getType().name() : null);
        p.setGroupName(source.getGroupName());
        return p;
    }

    public List<PreferenceVO> toVOList(Collection<Preference> sources) {
        if (sources == null) {
            return List.of();
        }
        return sources.stream().map(this::toVO).toList();
    }

    public List<UserPreferenceVO> toUserVOList(Collection<Preference> sources) {
        if (sources == null) {
            return List.of();
        }
        return sources.stream().map(this::toUserVO).toList();
    }

    public Preference toDomain(PreferenceVO source) {
        if (source == null) {
            return null;
        }
        return Preference.newBuilder()
                .pKey(source.getpKey())
                .key(source.getKey())
                .owner(source.getOwner())
                .description(source.getDescription())
                .val(source.getVal() == null ? null : source.getVal().toString())
                .type(Arrays.stream(PreferenceType.values())
                        .filter(v -> v.name().equals(source.getType()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("PreferenceType " + source.getType())))
                .scope(resolveScope(source))
                .groupName(source.getGroupName())
                .build();
    }

    public PreferenceMO toMO(Preference source) {
        if (source == null) {
            return null;
        }
        var mo = new PreferenceMO(source.getKey());
        mo.setpKey(source.getPersistentKey());
        mo.setOwner(source.getOwner());
        mo.setDescription(source.getDescription());
        mo.setVal(source.getVal());
        mo.setGroupName(source.getGroupName());
        mo.setType(source.getType() != null ? source.getType().name() : null);
        return mo;
    }
}
