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

import org.ameba.annotation.TxService;
import org.openwms.core.configuration.PreferencesService;
import org.openwms.core.configuration.impl.file.PreferenceDao;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A PreferencesServiceImpl is a transactional Spring powered service implementation to manage {@code Preferences}.
 *
 * @author Heiko Scherrer
 */
@Validated
@TxService
class PreferencesServiceImpl implements PreferencesService {

    private final PreferenceDao fileDao;
    private final PreferenceRepository preferenceRepository;

    PreferencesServiceImpl(PreferenceDao fileDao, PreferenceRepository preferenceRepository) {
        this.fileDao = fileDao;
        this.preferenceRepository = preferenceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<AbstractPreferenceEO> findAll(@NotEmpty String owner) {
        List<AbstractPreferenceEO> result = preferenceRepository.findAllByOwner(owner);
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * {@inheritDoc}
    @Override
    public Collection<AbstractPreferenceEO> findBy(@NotEmpty String owner, @NotEmpty String key) {
        Collection<T> result = (owner == null || owner.isEmpty())
                ? preferenceRepository.find(clazz)
                : preferenceRepository.findByType(clazz, owner);
        return result == null ? Collections.emptyList() : result;
    }
     */

    /**
     * {@inheritDoc}
     * <p>
     * Not allowed to call this implementation with a {@literal null} argument.
     *
     * @throws IllegalArgumentException when {@code preference} is {@literal null}
    @Override
    @FireAfterTransaction(events = {ConfigurationChangedEvent.class})
    public <T extends AbstractPreferenceEO> T save(T preference) {
        Assert.notNull(preference, "Not allowed to call save with a NULL argument");
        return preferenceRepository.save(preference);
    }
     */

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException when {@code preference} is {@literal null}
    @Override
    public void delete(AbstractPreferenceEO preference) {
        Assert.notNull(preference, "Not allowed to call remove with a NULL argument");
        preferenceRepository.delete(preference);
    }
     */

    /**
     *
    @Override
    public void reloadInitialPreferences() {
        mergeApplicationProperties();
    }
     */

    /**
     *
    private void mergeApplicationProperties() {
        List<GenericPreference> fromFile = fileDao.findAll();
        List<AbstractPreferenceEO> persistedPrefs = preferenceRepository.findAll();
        for (GenericPreference pref : fromFile) {
            if (!persistedPrefs.contains(pref)) {
                preferenceRepository.save(pref);
            }
        }
    }
     */
}