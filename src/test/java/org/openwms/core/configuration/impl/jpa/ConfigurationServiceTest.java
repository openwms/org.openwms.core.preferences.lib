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

import org.ameba.test.MockitoExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.openwms.core.configuration.impl.file.MockApplicationPreference;
import org.openwms.core.configuration.impl.file.ApplicationPreference;
import org.openwms.core.configuration.impl.file.GenericPreference;
import org.openwms.core.configuration.impl.file.ModulePreference;
import org.openwms.core.configuration.impl.file.PreferenceDao;
import org.openwms.core.event.MergePropertiesEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * A ConfigurationServiceTest.
 *
 * @author Heiko Scherrer
 */
@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    private static final String PERSISTED_APP_PREF1 = "persPref1";
    private static final String PERSISTED_APP_PREF2 = "persPref2";
    private List<GenericPreference> filePrefs = new ArrayList<>();
    private List<GenericPreference> persistedPrefs = new ArrayList<>();

    @Mock
    private PreferenceRepository writer;
    @Mock
    private PreferenceDao reader;
    private ConfigurationServiceImpl srv;

    @BeforeEach void doBefore() {
        srv = new ConfigurationServiceImpl(reader, writer);
        // Prepare some preferences coming from a file
        filePrefs.add(new ApplicationPreference("filePref1"));
        filePrefs.add(new ApplicationPreference("filePref2"));
        // And a few from the database
        persistedPrefs.add(new ApplicationPreference(PERSISTED_APP_PREF1));
        persistedPrefs.add(new ApplicationPreference(PERSISTED_APP_PREF2));
    }

    @AfterEach void doAfter() {
        filePrefs.clear();
        persistedPrefs.clear();
        reset(writer);
        reset(reader);
    }

    @Test void testOnApplicationEvent() {
        // That one must not be persisted
        filePrefs.add(new ApplicationPreference(PERSISTED_APP_PREF2));
        when(reader.findAll()).thenReturn(filePrefs);
        when(writer.findAll()).thenReturn(persistedPrefs);

        srv.onApplicationEvent(new MergePropertiesEvent(this));
        // new file preferences should be saved
        verify(writer).save(new ApplicationPreference("filePref1"));
        verify(writer).save(new ApplicationPreference("filePref2"));
        // save must not be called for an already existing preference.
        verify(writer, never()).save(new ApplicationPreference(PERSISTED_APP_PREF2));
    }

    @Test void testFindAll() {
        when(writer.findAll()).thenReturn(persistedPrefs);
        assertThat(srv.findAll()).isEqualTo(persistedPrefs);
        verify(writer, times(1)).findAll();
    }

    @Test void testFindByType() {
        filePrefs.add(new ModulePreference("CORE", PERSISTED_APP_PREF2));
        when(writer.findByType(ModulePreference.class, "CORE")).thenReturn(
                Collections.singletonList(new ModulePreference("CORE", PERSISTED_APP_PREF2)));

        Collection<ModulePreference> prefs = srv.findByType(ModulePreference.class, "CORE");
        verify(writer).findByType(ModulePreference.class, "CORE");
        assertThat(prefs.size()).isEqualTo(1);
    }

    @Test void testSaveNull() {
        assertThatThrownBy(
                () -> srv.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testSaveNewEntity() {
        when(writer.findByType(ApplicationPreference.class)).thenReturn(
                Collections.singletonList(new ApplicationPreference("PERSISTED")));

        ApplicationPreference newEntity = new ApplicationPreference("TRANSIENT");
        srv.save(newEntity);
        verify(writer).save(newEntity);
    }

    @Test void testSaveDuplicatedEntity() {
        when(writer.findByType(ApplicationPreference.class)).thenReturn(
                Collections.singletonList(new ApplicationPreference("TRANSIENT")));

        ApplicationPreference newEntity = new ApplicationPreference("TRANSIENT");
        srv.save(newEntity);
        verify(writer).save(newEntity);
    }

    @Test void testSaveExistingEntity() {
        MockApplicationPreference mock = new MockApplicationPreference("TRANSIENT");
        when(writer.findByType(MockApplicationPreference.class)).thenReturn(
                Collections.singletonList(mock));
        when(writer.save(mock)).thenReturn(mock);

        assertThat(srv.save(mock)).isEqualTo(mock);
        verify(writer).save(mock);
    }

    @Test void testRemoveNull() {
        assertThatThrownBy(
                () -> srv.delete(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testRemove() {
        srv.delete(new ApplicationPreference("TRANSIENT"));
        verify(writer).delete(new ApplicationPreference("TRANSIENT"));
    }
}