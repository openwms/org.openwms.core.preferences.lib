/*
 * Copyright 2005-2025 the original author or authors.
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
package org.openwms.core.preferences.impl.file;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A ApplicationPreferenceTest. Test unmarshalling a valid XML document of preferences.
 *
 * @author Heiko Scherrer
 */
class ApplicationPreferenceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationPreferenceTest.class);
    private static final String APP_PREF1 = "APP_PREF1";
    private static final String APP_PREF2 = "APP_PREF1";
    private static final String APP_PREF3 = "APP_PREF2";

    @Test void testCreation() {
        ApplicationPreference applicationPreference = new ApplicationPreference(APP_PREF1);
        assertThat(applicationPreference.getKey())
                .isEqualTo(APP_PREF1);
    }

    @Test void testCreationNegative() {

        assertThatThrownBy(
                () -> new ApplicationPreference(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not allowed to create an ApplicationPreference with an empty key");

        assertThatThrownBy(
                () -> new ApplicationPreference(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not allowed to create an ApplicationPreference with an empty key");
    }

    @Test void testKeyGeneration() {
        ApplicationPreference ap1 = new ApplicationPreference(APP_PREF1);
        ApplicationPreference ap2 = new ApplicationPreference(APP_PREF2);
        ApplicationPreference ap3 = new ApplicationPreference(APP_PREF3);

        assertThat(ap1.getPrefKey()).isEqualTo(ap2.getPrefKey());
        assertThat(ap1.getPrefKey()).isNotEqualTo(ap3.getPrefKey());
    }

    @Test void testKeyUniqueness() {
        ApplicationPreference ap1 = new ApplicationPreference(APP_PREF1);
        ApplicationPreference ap2 = new ApplicationPreference(APP_PREF2);
        ApplicationPreference ap3 = new ApplicationPreference(APP_PREF3);
        Map<PreferenceKey, ApplicationPreference> map = new HashMap<>();
        map.put(ap1.getPrefKey(), ap1);
        map.put(ap2.getPrefKey(), ap2);

        assertThat(map).hasSize(1);

        map.put(ap3.getPrefKey(), ap3);
        assertThat(map).hasSize(2);
    }

    @Test void testReadPreferences() throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(ResourceUtils.getFile("classpath:preferences.xsd"));
        JAXBContext ctx = JAXBContext.newInstance("org.openwms.core.preferences.impl.file");
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(event -> {
            RuntimeException ex = new RuntimeException(event.getMessage(), event.getLinkedException());
            LOGGER.error(ex.getMessage());
            throw ex;
        });

        Preferences prefs = (Preferences) unmarshaller.unmarshal(ResourceUtils.getFile(
                "classpath:org/openwms/core/preferences/file/preferences.xml"));
        assertThat(prefs.getApplications()).hasSize(3);
        assertThat(prefs.getModules()).hasSize(2);
        for (GenericPreference pref : prefs.getApplications()) {
            LOGGER.debug(pref.toString());
        }
        for (GenericPreference pref : prefs.getModules()) {
            LOGGER.debug(pref.toString());
        }
        for (GenericPreference pref : prefs.getUsers()) {
            LOGGER.debug(pref.toString());
        }
    }

    @Test void testHashCodeEquals() {
        ApplicationPreference ap1 = new ApplicationPreference(APP_PREF1);
        ApplicationPreference ap2 = new ApplicationPreference(APP_PREF2);
        ApplicationPreference ap3 = new ApplicationPreference(APP_PREF3);
        ApplicationPreference ap4 = new ApplicationPreference();

        // Just the key is considered
        assertThat(ap1).isEqualTo(ap2).isNotEqualTo(ap3);
        assertThat(ap4).isNotEqualTo(ap3);

        // Test behavior in hashed collections
        Set<ApplicationPreference> applicationPreferences = new HashSet<>();
        applicationPreferences.add(ap1);
        applicationPreferences.add(ap2);
        assertThat(applicationPreferences).hasSize(1);
        applicationPreferences.add(ap3);
        assertThat(applicationPreferences).hasSize(2);
    }
}