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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ameba.app.SpringProfiles;
import org.ameba.http.PermitAllCorsConfigurationSource;
import org.openwms.core.configuration.api.ApplicationPreferenceVO;
import org.openwms.core.configuration.api.PreferenceVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.Filter;

/**
 * A PreferencesWebConfiguration is the microservice Spring web configuration, active by default.
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableWebMvc
public class PreferencesWebConfiguration {

    @Bean
    public HttpMessageConverter customTypeHttpMessageConverter(ObjectMapper objectMapper) {
        return new AbstractJackson2HttpMessageConverter(objectMapper,
                new MediaType(ApplicationPreferenceVO.TYPE, ApplicationPreferenceVO.SUB_TYPE),
                //new MediaType(ModulePreferenceVO.MEDIA_TYPE),
                //new MediaType(RolePreferenceVO.MEDIA_TYPE),
                //new MediaType(UserPreferenceVO.MEDIA_TYPE),
                new MediaType(PreferenceVO.TYPE, PreferenceVO.SUB_TYPE)
        ){};
    }

    @Profile(SpringProfiles.DEVELOPMENT_PROFILE)
    @Bean
    Filter corsFiler() {
        return new CorsFilter(new PermitAllCorsConfigurationSource());
    }
}
