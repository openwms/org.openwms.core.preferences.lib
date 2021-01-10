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
package org.openwms.core.configuration;

/**
 * A PropertyScope defines the different scopes for preferences.
 *
 * @author Heiko Scherrer
 */
public enum PropertyScope {

    /** These kind of preferences belong to the main application. */
    APPLICATION,

    /** These kind of preferences are specific to a {@code Module}. */
    MODULE,

    /** These kind of preferences belong to a particular {@code Role}. */
    ROLE,

    /** These kind of preferences belong to a certain {@code User}. */
    USER;
}