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

import org.openwms.core.SpringProfiles;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * A PreferencesDistributedConfiguration is activated with the Spring Profile {@link SpringProfiles#DISTRIBUTED} that shall be activated
 * when the service is deployed as a microservice, not packaged within a standalone application, to enable service discovery.
 *
 * @author Heiko Scherrer
 */
@Profile(SpringProfiles.DISTRIBUTED)
@EnableDiscoveryClient
@Configuration
public class PreferencesDistributedConfiguration {
}
