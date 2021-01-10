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
package org.openwms.core.configuration.impl.jpa;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

/**
 * A PreferenceRepositoryImpl implements custom generic find methods of {@link PreferenceRepositoryCustom}.
 * <p>
 * All methods have to be invoked within an active transaction context.
 * </p>
 * 
 * @author Heiko Scherrer
 */
@Transactional(propagation = Propagation.MANDATORY)
class PreferenceRepositoryImpl implements PreferenceRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends PreferenceEO> List<T> findByType(Class<T> clazz) {
        List<T> result = em.createNamedQuery(clazz.getSimpleName() + PreferenceEO.FIND_ALL, clazz).getResultList();
        return result == null ? Collections.emptyList() : result;
    }
}