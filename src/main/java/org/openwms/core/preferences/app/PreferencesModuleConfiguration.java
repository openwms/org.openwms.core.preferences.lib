/*
 * Copyright 2005-2023 the original author or authors.
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

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Validator;
import org.ameba.annotation.EnableAspects;
import org.ameba.i18n.AbstractSpringTranslator;
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.ameba.mapping.DozerMapperImpl;
import org.ameba.system.NestedReloadableResourceBundleMessageSource;
import org.openwms.core.preferences.config.ModuleProperties;
import org.openwms.core.preferences.impl.file.FilePackage;
import org.openwms.core.startup.LocalServiceInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.Properties;

/**
 * A PreferencesModuleConfiguration is the microservice default Spring configuration, active by default.
 * It enables:
 * <ul>
 *     <li>OpenWMS Aspects</li>
 * </ul>
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableAspects(propagateRootCause = true)
@EnableConfigurationProperties(ModuleProperties.class)
public class PreferencesModuleConfiguration {

    @Bean
    LocalServiceInitializer localServiceInitializer(ApplicationContext ctx) {
        return new LocalServiceInitializer(ctx);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    public @Bean Translator translator() {
        return new AbstractSpringTranslator() {
            @Override
            protected MessageSource getMessageSource() {
                return messageSource();
            }
        };
    }

    public @Bean MessageSource messageSource() {
        var nrrbm = new NestedReloadableResourceBundleMessageSource();
        nrrbm.setBasenames(
                "classpath:META-INF/i18n/preferences"
        );
        nrrbm.setDefaultEncoding("UTF-8");
        nrrbm.setCommonMessages(new Properties());
        return nrrbm;
    }

    @Bean
    MethodValidationPostProcessor methodValidationPostProcessor(Validator validatorFactoryBean) {
        var mvpp = new MethodValidationPostProcessor();
        mvpp.setValidator(validatorFactoryBean);
        return mvpp;
    }

    public @Bean
    BeanMapper beanMapper() {
        return new DozerMapperImpl("META-INF/dozer/core-prefs-bean-mappings.xml");
    }

    @Bean
    Unmarshaller unmarshaller() {
        var um = new Jaxb2Marshaller();
        um.setContextPath(FilePackage.class.getPackageName());
        return um;
    }
}
