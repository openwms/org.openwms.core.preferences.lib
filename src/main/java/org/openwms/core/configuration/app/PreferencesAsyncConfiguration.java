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

import org.ameba.amqp.RabbitTemplateConfigurable;
import org.openwms.core.SpringProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;

import java.util.Objects;

import static org.ameba.LoggingCategories.BOOT;

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

    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(BOOT);

    @RefreshScope
    @ConditionalOnExpression("'${owms.preferences.serialization}'=='json'")
    @Bean
    MessageConverter messageConverter() {
        var messageConverter = new Jackson2JsonMessageConverter();
        BOOT_LOGGER.info("Using JSON serialization over AMQP");
        return messageConverter;
    }

    @RefreshScope
    @ConditionalOnExpression("'${owms.preferences.serialization}'=='barray'")
    @Bean
    MessageConverter serializerMessageConverter() {
        var messageConverter = new SerializerMessageConverter();
        BOOT_LOGGER.info("Using byte array serialization over AMQP");
        return messageConverter;
    }

    @Bean
    StatefulRetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateful()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000)
                .build();
    }

    @RefreshScope
    @Primary
    @Bean(name = "amqpTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
            ObjectProvider<MessageConverter> messageConverter,
            @Autowired(required = false) RabbitTemplateConfigurable rabbitTemplateConfigurable) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        var backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setMaxInterval(15000);
        backOffPolicy.setInitialInterval(500);
        var retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setMessageConverter(Objects.requireNonNull(messageConverter.getIfUnique()));
        if (rabbitTemplateConfigurable != null) {
            rabbitTemplateConfigurable.configure(rabbitTemplate);
        }
        return rabbitTemplate;
    }

    @RefreshScope
    @Bean
    TopicExchange lgExchange(@Value("${owms.events.core.preferences.exchange-name}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }
}
