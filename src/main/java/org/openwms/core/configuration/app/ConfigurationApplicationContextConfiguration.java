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

import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * A ConfigurationApplicationContextConfiguration is the Spring Java Configuration that initializes the application properties as
 * {@link PropertySourcesPlaceholderConfigurer}. Import this configuration to take advantage of the JavaConfig mechanism instead of XML
 * configuration.
 * 
 * @author Heiko Scherrer
 */
@Configuration
class ConfigurationApplicationContextConfiguration /* implements WebMvcConfigurer*/ {

}
