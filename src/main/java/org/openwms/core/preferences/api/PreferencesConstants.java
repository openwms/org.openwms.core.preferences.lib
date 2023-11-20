/*
 * Copyright 2005-2023 the original author or authors.
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
package org.openwms.core.preferences.api;

/**
 * A PreferencesConstants.
 *
 * @author Heiko Scherrer
 */
public final class PreferencesConstants {

    private PreferencesConstants() {}

    /*~ --- Messages --- */
    public static final String NOT_FOUND_BY_PKEY = "owms.core.preferences.notFoundByPKey";
    public static final String NOT_FOUND_BY_OWNER_AND_SCOPE_AND_KEY = "owms.core.preferences.notFoundByOwnerAndScopeAndKey";
    public static final String ALREADY_EXISTS = "owms.core.preferences.alreadyExists";
    public static final String ALREADY_EXISTS_WITH_OWNER_AND_SCOPE_AND_KEY = "owms.core.preferences.alreadyExistsWithOwnerAndScopeAndKey";
    public static final String NOT_ALLOWED_PKEY = "owms.core.preferences.notAllowedPKey";
    public static final String PROPERTY_SCOPE_NOT_DEFINED = "owms.core.preferences.propertyScopeNotDefined";
    public static final String NOT_ALLOWED_FETCH_USER_PREFS = "owms.core.preferences.notAllowedToFetchUserPrefs";

    /*~ --- Length --- */
    public static final int LENGTH_KEY = 60;
    public static final int LENGTH_OWNER = 60;
    public static final int LENGTH_DESCRIPTION = 255;
    public static final int LENGTH_VALUE = 4096;
    public static final int LENGTH_TYPE = 36;
    public static final int LENGTH_GROUP = 36;
}
