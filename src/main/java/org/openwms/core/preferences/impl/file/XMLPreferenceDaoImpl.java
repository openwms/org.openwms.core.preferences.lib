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
package org.openwms.core.preferences.impl.file;

import jakarta.annotation.PostConstruct;
import org.ameba.annotation.Measured;
import org.ameba.exception.IntegrationLayerException;
import org.openwms.core.event.ReloadFilePreferencesEvent;
import org.openwms.core.exception.NoUniqueResultException;
import org.openwms.core.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

/**
 * A XMLPreferenceDaoImpl reads an XML file of {@code Preferences} and keeps them in-memory in a Map. An initial preferences file can be
 * configured with a property {@literal owms.core.config.initial-properties} in the {@literal application.properties} file.
 *
 * <p>On a {@link ReloadFilePreferencesEvent} the internal Map is cleared and reloaded.</p>
 *
 * @author Heiko Scherrer
 * @see org.openwms.core.event.ReloadFilePreferencesEvent
 */
@Transactional(propagation = Propagation.MANDATORY)
@Repository
class XMLPreferenceDaoImpl implements PreferenceDao, ApplicationListener<ReloadFilePreferencesEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLPreferenceDaoImpl.class);
    private final ApplicationContext ctx;
    private final Unmarshaller unmarshaller;
    private final String fileName;
    private Resource fileResource;
    private Preferences preferences;
    private final Map<PreferenceKey, GenericPreference> prefs = new ConcurrentHashMap<>();

    XMLPreferenceDaoImpl(ApplicationContext ctx, Unmarshaller unmarshaller,
            @Value("${owms.core.config.initial-properties:}") String fileName) {
        this.ctx = ctx;
        this.unmarshaller = unmarshaller;
        this.fileName = fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GenericPreference> findAll() {
        return preferences == null ? Collections.emptyList() : preferences.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void onApplicationEvent(ReloadFilePreferencesEvent event) {
        reloadResources();
    }

    /**
     * On bean initialization load all {@code Preferences} into a Map.
     */
    @PostConstruct
    private void loadResources() {
        if (initialPropertiesExist()) {
            try {
                preferences = (Preferences) unmarshaller.unmarshal(new StreamSource(fileResource.getInputStream()));
                for (var pref : preferences.getAll()) {
                    if (prefs.containsKey(pref.getPrefKey())) {
                        throw new NoUniqueResultException(format("Preference with key [%s] already loaded", pref.getPrefKey()));
                    }
                    prefs.put(pref.getPrefKey(), pref);
                }
                LOGGER.debug("Loaded [{}] properties into cache", preferences.getAll().size());
            } catch (XmlMappingException xme) {
                throw new IntegrationLayerException(format("Exception while unmarshalling from [%s]", fileName), xme);
            } catch (IOException ioe) {
                throw new ResourceNotFoundException(format("Exception while accessing the resource with name [%s]", fileName), ioe);
            }
        }
    }

    private boolean initialPropertiesExist() {
        if (fileName != null && !fileName.isEmpty()) {
            fileResource = ctx.getResource(fileName);
        }
        if (fileResource == null || !fileResource.exists()) {
            LOGGER.debug("File to load initial preferences does not exist or is not preset. Filename [{}]", fileName);
            return false;
        }
        return true;
    }

    private void reloadResources() {
        preferences = null;
        prefs.clear();
        loadResources();
    }
}