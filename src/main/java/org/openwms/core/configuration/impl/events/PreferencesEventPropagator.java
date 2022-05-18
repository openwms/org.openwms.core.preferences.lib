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
package org.openwms.core.configuration.impl.events;

import org.ameba.mapping.BeanMapper;
import org.openwms.core.SpringProfiles;
import org.openwms.core.configuration.PreferencesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.Assert;

import javax.validation.Validator;

import static org.ameba.system.ValidationUtil.validate;

/**
 * A PreferencesEventPropagator propagates internal {@link PreferencesEvent}s to the outer world, only active with Spring Profile
 * {@link SpringProfiles#ASYNCHRONOUS_PROFILE}.
 *
 * @author Heiko Scherrer
 */
@Profile(SpringProfiles.ASYNCHRONOUS_PROFILE)
@Component
class PreferencesEventPropagator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesEventPropagator.class);
    private final AmqpTemplate amqpTemplate;
    private final Validator validator;
    private final BeanMapper mapper;
    private final String exchangeName;

    PreferencesEventPropagator(
            AmqpTemplate amqpTemplate,
            Validator validator,
            BeanMapper mapper, @Value("${owms.events.core.preferences.exchange-name}") String exchangeName
    ) {
        this.amqpTemplate = amqpTemplate;
        this.validator = validator;
        this.mapper = mapper;
        this.exchangeName = exchangeName;
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void onEvent(PreferencesEvent event) {
        var preference = event.getSource();
        switch (event.getType()) {
            case CREATED -> {
                LOGGER.debug("Preference created: [{}]", preference);
                amqpTemplate.convertAndSend(exchangeName, "preference.event.created", validateAndConvert(event));
            }
            case UPDATED -> {
                LOGGER.debug("Preference updated: [{}]", preference);
                amqpTemplate.convertAndSend(exchangeName, "preference.event.changed", validateAndConvert(event));
            }
            case DELETED -> {
                LOGGER.debug("Preference deleted: [{}]", preference);
                amqpTemplate.convertAndSend(exchangeName, "preference.event.deleted", validateAndConvert(event));
            }
            default -> LOGGER.warn("Eventtype [{}] not supported", event.getType());
        }
    }

    private PreferenceMO validateAndConvert(PreferencesEvent event) {
        Assert.notNull(event, "Event to propagate is NULL");
        return validate(validator, mapper.map(event.getSource(), PreferenceMO.class));
    }
}
