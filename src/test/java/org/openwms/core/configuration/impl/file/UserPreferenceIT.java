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
package org.openwms.core.configuration.impl.file;

import org.junit.jupiter.api.BeforeEach;
import org.openwms.core.configuration.CoreDataTest;
import org.openwms.core.configuration.DefaultTestProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * An UserPreferenceIT.
 *
 * @author Heiko Scherrer
 */
@CoreDataTest
class UserPreferenceIT extends DefaultTestProfile {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPreferenceIT.class);
    private static final String KNOWN_USER = "KNOWN_USER";
    @Autowired
    private TestEntityManager em;

    @BeforeEach void onSetup() {
        LOGGER.info("Active Profiles: " + System.getenv("spring.profiles.active"));
        em.persist(new UserPreference(KNOWN_USER, "testKey"));
        em.flush();
    }
/*
    @Test void testSimplePersistAndGet() {
        UserPreference up = em.getEntityManager().createNamedQuery(UserPreference.NQ_FIND_BY_OWNER, UserPreference.class).setParameter("owner", KNOWN_USER).getSingleResult();
        assertThat(up)
                .isNotNull()
                .extracting("owner", "key", "type")
                .contains(KNOWN_USER, "testKey", PropertyScope.USER)
        ;
    }

 */
}