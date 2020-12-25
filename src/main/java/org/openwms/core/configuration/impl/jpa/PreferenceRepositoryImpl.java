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
package org.openwms.core.configuration.impl.jpa;

import org.openwms.core.configuration.impl.file.GenericPreference;
import org.openwms.core.configuration.impl.file.Preferences;
import org.openwms.core.exception.WrongClassTypeException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.openwms.core.configuration.impl.file.AbstractPreference.FIND_BY_OWNER;

/**
 * A PreferenceRepositoryImpl implements custom generic find methods of {@link PreferenceRepositoryCustom}.
 * <p>
 * All methods have to be invoked within an active transaction context.
 * </p>
 * 
 * @author Heiko Scherrer
 */
@Transactional(propagation = Propagation.MANDATORY)
@Repository
class PreferenceRepositoryImpl implements PreferenceRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends GenericPreference> List<T> findByType(Class<T> clazz) {
        return em.createNamedQuery(getQueryName(clazz) + ".findAll", clazz)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends GenericPreference> List<T> findByType(Class<T> clazz, String owner) {
        return em.createNamedQuery(getQueryName(clazz) + FIND_BY_OWNER, clazz)
                .setParameter("owner", owner)
                .getResultList();
    }

    private <T extends GenericPreference> String getQueryName(Class<T> clazz) {
        for (int i = 0; i < Preferences.TYPES.length; i++) {
            if (Preferences.TYPES[i].equals(clazz)) {
                return clazz.getSimpleName();
            }
        }
        throw new WrongClassTypeException("Type " + clazz + " not a valid Preferences type");
    }
}