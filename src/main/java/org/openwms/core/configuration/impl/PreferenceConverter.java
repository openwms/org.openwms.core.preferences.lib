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

import org.dozer.DozerConverter;
import org.openwms.core.configuration.impl.file.GenericPreference;
import org.openwms.core.configuration.impl.file.ModulePreference;
import org.openwms.core.configuration.impl.jpa.PreferenceEO;
import org.openwms.core.units.api.Measurable;
import org.openwms.core.units.api.Piece;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.wms.inventory.api.UnitTypeVO;

import java.math.BigDecimal;

/**
 * A PreferenceConverter.
 *
 * @author Heiko Scherrer
 */
public class PreferenceConverter extends DozerConverter<GenericPreference, PreferenceEO> {

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
        if (source.getClass().equals(ModulePreference.class)) {
            ModulePreference mp = (ModulePreference) source;
            return PreferenceEO.newBuilder()
                    .key(mp.getKey())
                    .owner(mp.getOwner())
                    .description(mp.getDescription())
                    .currentValue(mp.getValue())
                    .type(mp.getType().)
                    .build();
        }
        return new UnitTypeVO(new BigDecimal(source.getMagnitude().floatValue()), source.getUnitType().name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericPreference convertFrom(PreferenceEO source, GenericPreference destination) {
        if (source.getUnit().equals("PC")) {
            return Piece.of(source.getAmount(), PieceUnit.PC);
        }
        return null;
    }
}
