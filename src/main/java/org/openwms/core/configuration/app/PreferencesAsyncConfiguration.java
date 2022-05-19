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

import org.openwms.core.SpringProfiles;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * A PreferencesAsyncConfiguration contains the microservice' asynchronous configuration that is activated on Spring Profile
 * {@link SpringProfiles#ASYNCHRONOUS_PROFILE}.
 *
 * @author Heiko Scherrer
 */
@Profile(SpringProfiles.ASYNCHRONOUS_PROFILE)
@RefreshScope
@EnableRabbit
@Configuration
public class PreferencesAsyncConfiguration {

    @RefreshScope
    @Bean
    TopicExchange lgExchange(@Value("${owms.events.core.preferences.exchange-name}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }
}
