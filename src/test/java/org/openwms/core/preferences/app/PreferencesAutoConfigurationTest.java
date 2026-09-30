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
import org.openwms.core.preferences.PropertyScopeRegistrar;
import org.openwms.core.preferences.PropertyScopes;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A PreferencesAutoConfigurationTest verifies that all library components are activated through the auto-configuration only, without any
 * component scanning of the library package by the consuming application.
 *
 * @author Heiko Scherrer
 */
class PreferencesAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            // Registers a TypeExcludeFilter delegate like a @SpringBootTest bootstrap does, so @TestConfiguration classes from
            // sibling tests (the scope IT registrars) are not picked up by the library's component scan
            .withInitializer(ctx -> ctx.getBeanFactory().registerSingleton("testConfigurationExcludeFilter", new TestConfigurationExcludeFilter()))
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
            // Without a consumer registrar the default backs in and registers exactly the four built-in scopes
            assertThat(ctx).hasSingleBean(PropertyScopes.class);
            var scopes = ctx.getBean(PropertyScopes.class);
            assertThat(scopes.all()).hasSize(4);
            assertThat(scopes.isRegistered("APPLICATION")).isTrue();
            assertThat(scopes.isRegistered("USER")).isTrue();
        });
    }

    @Test
    void shall_back_off_default_registrar_when_consumer_defines_one() {
        contextRunner
                .withBean("phenixScopeRegistrar", PropertyScopeRegistrar.class, () -> () -> java.util.List.of("WAREHOUSE"))
                .run(ctx -> {
                    assertThat(ctx).hasNotFailed();
                    var scopes = ctx.getBean(PropertyScopes.class);
                    assertThat(scopes.all()).hasSize(1);
                    assertThat(scopes.isRegistered("WAREHOUSE")).isTrue();
                    assertThat(scopes.isRegistered("APPLICATION")).isFalse();
                });
    }

    /**
     * Mirrors the TypeExcludeFilter contribution of a @SpringBootTest bootstrap: excludes @TestConfiguration classes from
     * component scanning, keeping the ApplicationContextRunner isolated from sibling tests' registrar beans.
     */
    static class TestConfigurationExcludeFilter extends TypeExcludeFilter {

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
            return metadataReader.getAnnotationMetadata().isAnnotated(TestConfiguration.class.getName());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestConfigurationExcludeFilter;
        }

        @Override
        public int hashCode() {
            return TestConfigurationExcludeFilter.class.hashCode();
        }
    }
}
