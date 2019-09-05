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

import javax.annotation.PostConstruct;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A XMLPreferenceDaoImpl reads a XML file of preferences and keeps them internally in a Map. An initial preferences file can be configured
 * with a property <i>openwms.core.config.initial-properties</i> in the application.properties file. <p> On a {@link
 * ReloadFilePreferencesEvent} the internal Map is cleared and reloaded. </p>
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
    @Value("${openwms.core.config.initial-properties:}")
    private String fileName;
    private volatile Resource fileResource;
    private volatile Preferences preferences;
    private Map<PreferenceKey, AbstractPreference> prefs = new ConcurrentHashMap<>();

    XMLPreferenceDaoImpl(ApplicationContext ctx, Unmarshaller unmarshaller) {
        this.ctx = ctx;
        this.unmarshaller = unmarshaller;
    }

    /**
     * {@inheritDoc}
     *
     * @see PreferenceDao#findAll()
     */
    @Override
    public List<AbstractPreference> findAll() {
        return preferences == null ? Collections.emptyList() : preferences.getAll();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ReloadFilePreferencesEvent event) {
        reloadResources();
    }

    /**
     * On bean initialization load all preferences into a Map.
     */
    @PostConstruct
    private void loadResources() {
        if (initialPropertiesExist()) {
            try {
                preferences = (Preferences) unmarshaller.unmarshal(new StreamSource(fileResource.getInputStream()));
                for (AbstractPreference pref : preferences.getAll()) {
                    if (prefs.containsKey(pref.getPrefKey())) {
                        throw new NoUniqueResultException("Preference with key " + pref.getPrefKey() + " already loaded.");
                    }
                    prefs.put(pref.getPrefKey(), pref);
                }
                LOGGER.debug("Loaded {} properties into cache", preferences.getAll().size());
            } catch (XmlMappingException xme) {
                throw new IntegrationLayerException("Exception while unmarshalling from " + fileName, xme);
            } catch (IOException ioe) {
                throw new ResourceNotFoundException("Exception while accessing the resource with name " + fileName, ioe);
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