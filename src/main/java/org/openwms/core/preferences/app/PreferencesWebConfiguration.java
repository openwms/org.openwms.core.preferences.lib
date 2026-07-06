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

import jakarta.servlet.Filter;
import org.ameba.app.SpringProfiles;
import org.ameba.http.PermitAllCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * A PreferencesWebConfiguration is the microservice Spring web configuration, active by default.
 *
 * @author Heiko Scherrer
 */
@Configuration
public class PreferencesWebConfiguration {

    @Profile(SpringProfiles.DEVELOPMENT_PROFILE)
    @Bean
    Filter corsFiler() {
        return new CorsFilter(new PermitAllCorsConfigurationSource());
    }

    /**
     * Register an unrestricted Jackson converter for the custom {@code application/*+json} vendor media types. Actuator and Spring
     * HATEOAS bind type-specific {@code ObjectMapper}s to the shared converter, which makes it serve only those registered types.
     * This converter, appended last, keeps their precedence while still serializing the plain Preference value objects.
     */
    @Bean
    WebMvcConfigurer preferencesMessageConverterConfigurer(JsonMapper jsonMapper) {
        return new WebMvcConfigurer() {
            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(new JacksonJsonHttpMessageConverter(jsonMapper));
            }
        };
    }
}
