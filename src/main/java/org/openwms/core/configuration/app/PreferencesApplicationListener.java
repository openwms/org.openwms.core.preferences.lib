/*
 * Copyright 2005-2022 the original author or authors.
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
package org.openwms.core.configuration.app;

import org.openwms.core.configuration.PreferencesService;
import org.openwms.core.event.MergePropertiesEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * A PreferencesApplicationListener.
 *
 * @author Heiko Scherrer
 */
@Component
class PreferencesApplicationListener implements ApplicationListener<MergePropertiesEvent> {

    private final PreferencesService service;

    PreferencesApplicationListener(PreferencesService service) {
        this.service = service;
    }

    /**
     * {@inheritDoc}
     *
     * When a {@link MergePropertiesEvent} is caught, <i>new</i> {@code Preferences} received from the file provider are persisted. Already
     * existing {@code Preferences} are ignored.
     */
    @Override
    public void onApplicationEvent(MergePropertiesEvent event) {
        service.reloadInitialPreferences();
    }
}
