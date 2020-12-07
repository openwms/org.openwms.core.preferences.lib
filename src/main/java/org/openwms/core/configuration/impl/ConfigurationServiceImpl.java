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

import org.ameba.annotation.TxService;
import org.openwms.core.annotation.FireAfterTransaction;
import org.openwms.core.configuration.ConfigurationService;
import org.openwms.core.configuration.file.GenericPreference;
import org.openwms.core.configuration.file.PreferenceDao;
import org.openwms.core.event.ConfigurationChangedEvent;
import org.openwms.core.event.MergePropertiesEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A ConfigurationServiceImpl is a transactional Spring powered service implementation to manage preferences.
 *
 * @author Heiko Scherrer
 */
@Validated
@TxService
class ConfigurationServiceImpl implements ConfigurationService, ApplicationListener<MergePropertiesEvent> {

    private final PreferenceDao fileDao;
    private final PreferenceRepository preferenceRepository;

    public ConfigurationServiceImpl(PreferenceDao fileDao, PreferenceRepository preferenceRepository) {
        this.fileDao = fileDao;
        this.preferenceRepository = preferenceRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * When an event arrives all <i>new</i> preferences received from the file provider are persisted. Already persisted preferences are
     * ignored.
     */
    @Override
    public void onApplicationEvent(MergePropertiesEvent event) {
        mergeApplicationProperties();
    }

    /**
     * {@inheritDoc}
     * <p>
     * No match returns an empty List ({@link Collections#emptyList()}).
     */
    @Override
    public Collection<GenericPreference> findAll() {
        return preferenceRepository.findAll();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If owner is set to {@literal null} or is empty, all preferences of this type are returned. No match returns an empty List ( {@link
     * Collections#emptyList()}).
     */
    @Override
    public <T extends GenericPreference> Collection<T> findByType(Class<T> clazz, String owner) {
        Collection<T> result;
        result = (owner == null || owner.isEmpty()) ? preferenceRepository.findByType(clazz) : preferenceRepository.findByType(clazz, owner);
        return result == null ? Collections.<T>emptyList() : result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Not allowed to call this implementation with a {@literal null} argument.
     *
     * @throws IllegalArgumentException when {@code preference} is {@literal null}
     */
    @Override
    @FireAfterTransaction(events = {ConfigurationChangedEvent.class})
    public <T extends GenericPreference> T save(T preference) {
        Assert.notNull(preference, "Not allowed to call save with a NULL argument");
        return preferenceRepository.save(preference);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException when {@code preference} is {@literal null}
     */
    @Override
    public void delete(GenericPreference preference) {
        Assert.notNull(preference, "Not allowed to call remove with a NULL argument");
        preferenceRepository.delete(preference);
    }

    private void mergeApplicationProperties() {
        List<GenericPreference> fromFile = fileDao.findAll();
        List<GenericPreference> persistedPrefs = preferenceRepository.findAll();
        for (GenericPreference pref : fromFile) {
            if (!persistedPrefs.contains(pref)) {
                preferenceRepository.save(pref);
            }
        }
    }
}