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

import org.openwms.core.preferences.impl.mongodb.PropertyScopeReadConverter;
import org.openwms.core.preferences.impl.mongodb.PropertyScopeWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

/**
 * A PreferencesMongoConfiguration activates support for the MongoDB persistent storage.
 * It enables:
 * <ul>
 *     <li>Spring Data MongoDB Support (Repository definitions)</li>
 *     <li>Spring Data MongoDB Auditing</li>
 *     <li>Custom converters for PropertyScope</li>
 * </ul>
 *
 * @author Heiko Scherrer
 */
@Profile("MONGODB")
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "org.openwms.core.preferences.impl.mongodb")
@Configuration
public class PreferencesMongoConfiguration {

    @Bean
    MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new PropertyScopeReadConverter(),
                new PropertyScopeWriteConverter()
        ));
    }
}
