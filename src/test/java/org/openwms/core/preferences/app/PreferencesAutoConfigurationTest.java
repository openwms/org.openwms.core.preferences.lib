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
import org.openwms.core.preferences.PreferencesController;
import org.openwms.core.preferences.PreferencesService;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A PreferencesAutoConfigurationTest verifies that all library components are activated through the auto-configuration only, without any
 * component scanning of the library package by the consuming application.
 *
 * @author Heiko Scherrer
 */
class PreferencesAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    DataSourceAutoConfiguration.class,
                    HibernateJpaAutoConfiguration.class,
                    JacksonAutoConfiguration.class,
                    PreferencesAutoConfiguration.class
            ))
            .withPropertyValues(
                    "spring.application.name=preferences-test",
                    "spring.jpa.mapping-resources=META-INF/preferences-orm.xml",
                    // Decouple from any profiles passed to the surefire JVM, like AMQP in CI
                    "spring.profiles.active=DEFAULT",
                    // Decouple from any datasource url passed to the surefire JVM, like the PostgreSQL url in the CI Site step
                    "spring.datasource.url=jdbc:h2:mem:autoconfig;DB_CLOSE_DELAY=-1"
            );

    @Test
    void shall_provide_all_components_without_component_scanning() {
        contextRunner.run(ctx -> {
            assertThat(ctx).hasNotFailed();
            assertThat(ctx).hasSingleBean(PreferencesService.class);
            assertThat(ctx).hasSingleBean(PreferencesController.class);
            assertThat(ctx).hasBean("preferenceRepository");
        });
    }
}
