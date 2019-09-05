/*
 * Copyright 2005-2019 the original author or authors.
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
package org.openwms.core.configuration.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openwms.core.configuration.PropertyScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An UserPreferenceIT.
 *
 * @author Heiko Scherrer
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("TEST")
@DataJpaTest(showSql = false)
class UserPreferenceIT {

    private static final String KNOWN_USER = "KNOWN_USER";
    @Autowired
    private TestEntityManager em;

    @BeforeEach void onSetup() {
        em.persist(new UserPreference(KNOWN_USER, "testKey"));
    }

    @Test void testSimplePersistAndGet() {
        UserPreference up = em.getEntityManager().createNamedQuery(UserPreference.NQ_FIND_BY_OWNER, UserPreference.class).setParameter("owner", KNOWN_USER).getSingleResult();
        assertThat(up)
                .isNotNull()
                .extracting("owner", "key", "type")
                .contains(KNOWN_USER, "testKey", PropertyScope.USER)
        ;
    }
}