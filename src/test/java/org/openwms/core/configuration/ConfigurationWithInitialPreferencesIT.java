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
package org.openwms.core.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openwms.core.configuration.file.AbstractPreference;
import org.openwms.core.configuration.file.ApplicationPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

/**
 * A ConfigurationIT.
 *
 * @author Heiko Scherrer
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ConfigurationWithInitialPreferencesIT {

    private ApplicationPreference saved = new ApplicationPreference.Builder()
            .withKey("defaultLanguage")
            .withDescription("description")
            .withMinimum(10)
            .withMaximum(100)
            .withFloatValue(22.1F)
            .withValue("en_US")
            .build();

    @Autowired
    private ConfigurationController testee;

    @Test void testSave() {
        Iterable<AbstractPreference> all = testee.findAll();
        assertThat(all)
                .isNotNull()
                .hasSize(1)
                .contains(saved);
        assertThat(all)
                .extracting("key", "type", "value", "minimum", "maximum", "floatValue")
                .contains(tuple("defaultLanguage", PropertyScope.APPLICATION, "en_US", 10, 100, 22.1F));
    }
}
