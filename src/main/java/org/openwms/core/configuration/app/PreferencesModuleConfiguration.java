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
package org.openwms.core.configuration.app;

import io.micrometer.core.instrument.MeterRegistry;
import org.ameba.app.BaseConfiguration;
import org.ameba.mapping.BeanMapper;
import org.ameba.mapping.DozerMapperImpl;
import org.openwms.core.configuration.config.ModuleProperties;
import org.openwms.core.configuration.impl.file.FilePackage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.reactive.config.EnableWebFlux;

import javax.validation.Validator;

/**
 * A PreferencesModuleConfiguration.
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableWebFlux
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties(ModuleProperties.class)
@Import(BaseConfiguration.class)
public class PreferencesModuleConfiguration {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    @Bean
    MethodValidationPostProcessor methodValidationPostProcessor(Validator validatorFactoryBean) {
        MethodValidationPostProcessor mvpp = new MethodValidationPostProcessor();
        mvpp.setValidator(validatorFactoryBean);
        return mvpp;
    }

    public @Bean
    BeanMapper beanMapper() {
        return new DozerMapperImpl("META-INF/dozer/core-prefs-bean-mappings.xml");
    }

    @Bean
    Unmarshaller unmarshaller() {
        Jaxb2Marshaller um = new Jaxb2Marshaller();
        um.setContextPath(FilePackage.class.getPackageName());
        return um;
    }
}
