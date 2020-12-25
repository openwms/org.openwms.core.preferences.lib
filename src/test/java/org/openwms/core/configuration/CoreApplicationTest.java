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
package org.openwms.core.configuration;

import org.ameba.test.categories.SpringTestSupport;
import org.openwms.core.configuration.Starter;
import org.openwms.core.configuration.app.ModuleConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A CoreApplicationTest.
 *
 * @author Heiko Scherrer
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@SpringTestSupport
@SpringBootTest(classes = {Starter.class, ModuleConfiguration.class}, properties = {
        "spring.main.web-application-type=reactive",
        "spring.jpa.show-sql=false",
        "spring.main.banner-mode=OFF",
        "spring.jackson.serialization.INDENT_OUTPUT=true"
})
public @interface CoreApplicationTest {
}
