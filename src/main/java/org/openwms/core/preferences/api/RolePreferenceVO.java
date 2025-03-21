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
package org.openwms.core.preferences.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * A RolePreferenceVO.
 *
 * @author Heiko Scherrer
 */
public class RolePreferenceVO extends PreferenceVO implements Serializable {

    /** HTTP media sub type representation. */
    public static final String SUB_TYPE = "vnd.openwms.core.role-preference-v1+json";
    /** HTTP media type representation. */
    public static final String MEDIA_TYPE = TYPE + "/" + SUB_TYPE;

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getContentType() {
        return MEDIA_TYPE;
    }
}
