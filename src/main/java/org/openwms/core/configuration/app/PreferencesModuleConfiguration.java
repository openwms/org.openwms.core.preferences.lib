/*
 * Copyright 2005-2021 the original author or authors.
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
import org.ameba.annotation.EnableAspects;
import org.ameba.app.SpringProfiles;
import org.ameba.mapping.BeanMapper;
import org.ameba.mapping.DozerMapperImpl;
import org.openwms.core.configuration.config.ModuleProperties;
import org.openwms.core.configuration.impl.file.FilePackage;
import org.openwms.core.startup.LocalServiceInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;

import javax.validation.Validator;

import static java.util.Arrays.asList;
import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;
import static org.ameba.Constants.HEADER_VALUE_X_REQUESTID;
import static org.ameba.Constants.HEADER_VALUE_X_TENANT;

/**
 * A PreferencesModuleConfiguration is the microservice default Spring configuration, active by default.
 * It enables:
 * <ul>
 *     <li>WebFlux</li>
 *     <li>OpenWMS Aspects</li>
 * </ul>
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableWebFlux
@EnableAspects
@EnableConfigurationProperties(ModuleProperties.class)
public class PreferencesModuleConfiguration {

    @Profile(SpringProfiles.DEVELOPMENT_PROFILE)
    @Bean
    CorsWebFilter corsWebFilter() {
        var corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");
        corsConfig.setMaxAge(1800L);
        corsConfig.setAllowedMethods(asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(asList("Content-Type", "X-REQUESTED-WITH", "Authorization", HEADER_VALUE_X_TENANT, HEADER_VALUE_X_REQUESTID, HEADER_VALUE_X_IDENTITY));
        corsConfig.setExposedHeaders(asList("Location", HEADER_VALUE_X_TENANT, HEADER_VALUE_X_REQUESTID, HEADER_VALUE_X_IDENTITY));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }

    @Bean
    LocalServiceInitializer localServiceInitializer(ApplicationContext ctx) {
        return new LocalServiceInitializer(ctx);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
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
