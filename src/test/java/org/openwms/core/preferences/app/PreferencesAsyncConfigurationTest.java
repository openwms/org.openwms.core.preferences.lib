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
package org.openwms.core.preferences.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openwms.core.preferences.CoreApplicationTest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A PreferencesAsyncConfigurationTest verifies the AMQP infrastructure beans. Since the amqpTemplate is a refresh-scoped bean it is
 * instantiated lazily on first access, therefore the test explicitly dereferences the scoped proxy to fail on an invalid retry
 * configuration.
 *
 * @author Heiko Scherrer
 */
@EnabledIfSystemProperty(named = "spring.profiles.active", matches = ".*AMQP.*")
@CoreApplicationTest
class PreferencesAsyncConfigurationTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void shall_create_the_amqp_template_with_retry_policy() {
        var amqpTemplate = ctx.getBean("amqpTemplate", RabbitTemplate.class);
        assertThat(amqpTemplate.getMessageConverter()).isNotNull();
    }
}
