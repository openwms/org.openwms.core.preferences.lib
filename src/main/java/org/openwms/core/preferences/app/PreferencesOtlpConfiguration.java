/*
 * Copyright 2005-2025 the original author or authors.
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

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A PreferencesOtlpConfiguration.
 *
 * @author Heiko Scherrer
 */
@ConditionalOnClass(name = "io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter")
@AutoConfiguration
public class PreferencesOtlpConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter")
    public static class OtelConfiguration {
        @ConditionalOnClass(name = "io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter")
        public @Bean OtlpGrpcSpanExporter otlpHttpSpanExporter(@Value("${owms.tracing.url}") String url) {
            return OtlpGrpcSpanExporter.builder().setEndpoint(url).build();
        }
    }
}
