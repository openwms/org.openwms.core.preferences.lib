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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * A PreferencesAutoConfiguration activates all components of the Preferences library in a consuming Spring Boot application without the
 * need for component scanning of the {@code org.openwms.core.preferences} package. With the default (JPA) persistence backend the
 * consuming application only needs to include the ORM mapping file in {@code spring.jpa.mapping-resources=META-INF/preferences-orm.xml}.
 *
 * @author Heiko Scherrer
 */
@AutoConfiguration(afterName = "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration")
public class PreferencesAutoConfiguration {

    PreferencesAutoConfiguration() {}

    /**
     * Registers all Spring components of the Preferences library, including the module, web, async, distributed and mongo configurations.
     */
    @Configuration(proxyBeanMethods = false)
    @ComponentScan(basePackages = "org.openwms.core.preferences", excludeFilters = {
            @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
            @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)
    })
    static class PreferencesComponentsConfiguration {}

    /**
     * Activates the default JPA persistence backend with Spring Data repositories, auditing and transaction management. Not active with
     * the MONGODB Spring profile where the MongoDB backend is chosen instead (see {@link PreferencesMongoConfiguration}).
     */
    @Profile("!MONGODB")
    @Configuration(proxyBeanMethods = false)
    @EnableJpaRepositories(basePackages = "org.openwms.core.preferences.impl.jpa")
    @EnableJpaAuditing
    @EnableTransactionManagement
    static class PreferencesJpaConfiguration {}
}
