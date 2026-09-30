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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A PreferenceVOMapper maps between {@link Preference} (domain) and {@link PreferenceVO} (API VO).
 *
 * @author Heiko Scherrer
 */
@Mapper(componentModel = "spring")
public abstract class PreferenceVOMapper {

    private PropertyScopes propertyScopes;

    @Autowired
    public void setPropertyScopes(PropertyScopes propertyScopes) {
        this.propertyScopes = propertyScopes;
    }

    /**
     * Resolves the {@link PropertyScope} of the given VO through the {@link PropertyScopes} registry. The four typed VO
     * subclasses map onto the built-in scope names; a base {@link PreferenceVO} carries a custom scope by name. In both cases
     * the scope must be registered: if the consumer excluded a built-in scope from its registrar, the corresponding typed VO
     * is rejected consistently with the generic endpoints.
     *
     * @param preference the VO to resolve the scope for
     * @param <T> the VO type
     * @return the resolved PropertyScope
     * @throws IllegalArgumentException if the VO carries no scope name or an unregistered one
     */
    public <T extends PreferenceVO> PropertyScope resolveScope(T preference) {
        var scopeName = switch (preference) {
            case ApplicationPreferenceVO ignored -> PropertyScope.APPLICATION.name();
            case ModulePreferenceVO ignored -> PropertyScope.MODULE.name();
            case RolePreferenceVO ignored -> PropertyScope.ROLE.name();
            case UserPreferenceVO ignored -> PropertyScope.USER.name();
            default -> {
                if (preference.getScope() == null || preference.getScope().isBlank()) {
                    throw new IllegalArgumentException("Preference has no scope name set: " + preference);
                }
                yield preference.getScope();
            }
        };
        return propertyScopes.resolve(scopeName);
    }

    public PreferenceVO toVO(Preference source) {
        if (source == null) {
            return null;
        }
        PreferenceVO p;
        PropertyScope scope = source.getScope();
        if (PropertyScope.APPLICATION.equals(scope)) {
            p = new ApplicationPreferenceVO();
        } else if (PropertyScope.MODULE.equals(scope)) {
            p = new ModulePreferenceVO();
        } else if (PropertyScope.ROLE.equals(scope)) {
            p = new RolePreferenceVO();
        } else if (PropertyScope.USER.equals(scope)) {
            p = new UserPreferenceVO();
        } else {
            // Custom scopes are represented by the base PreferenceVO with scope name
            p = new PreferenceVO();
            p.setScope(source.getScope().name());
        }
        return fillVO(p, source);
    }

    public UserPreferenceVO toUserVO(Preference source) {
        if (source == null) {
            return null;
        }
        return fillVO(new UserPreferenceVO(), source);
    }

    public RolePreferenceVO toRoleVO(Preference source) {
        if (source == null) {
            return null;
        }
        return fillVO(new RolePreferenceVO(), source);
    }

    public ModulePreferenceVO toModuleVO(Preference source) {
        if (source == null) {
            return null;
        }
        return fillVO(new ModulePreferenceVO(), source);
    }

    private <T extends PreferenceVO> T fillVO(T p, Preference source) {
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

    public List<RolePreferenceVO> toRoleVOList(Collection<Preference> sources) {
        if (sources == null) {
            return List.of();
        }
        return sources.stream().map(this::toRoleVO).toList();
    }

    public List<ModulePreferenceVO> toModuleVOList(Collection<Preference> sources) {
        if (sources == null) {
            return List.of();
        }
        return sources.stream().map(this::toModuleVO).toList();
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
        mo.setScope(source.getScope() != null ? source.getScope().name() : null);
        return mo;
    }
}
