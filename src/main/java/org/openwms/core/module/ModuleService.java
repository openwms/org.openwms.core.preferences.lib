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
package org.openwms.core.module;

import java.util.List;

/**
 * A ModuleService offers functionality of common {@link Module} management tasks. Handling {@link Module}s is an essential functionality of
 * the CORE openwms.org subproject. {@link Module}s can be created, saved, loaded or unloaded.
 * 
 * @author Heiko Scherrer
 * @see Module
 */
public interface ModuleService {

    /**
     * Return a list of all existing {@link Module}s.
     * 
     * @return A list of {@link Module}s or an empty list when no {@link Module} s exist
     */
    List<Module> findAll();

    /**
     * Save a {@link Module}.
     * 
     * @param module
     *            {@link Module} instance to be saved
     * @return The saved {@link Module} instance
     */
    Module save(Module module);

    /**
     * Remove an already existing {@link Module}.
     * 
     * @param module
     *            {@link Module} to be removed
     */
    void remove(Module module);

    /**
     * Save the {@code startupOrder} for a list of {@link Module}s. The {@code startupOrder} of all {@link Module}s in the list
     * {@code modules} has to be calculated before.
     * 
     * @param modules
     *            The list of {@link Module}s to be saved
     */
    void saveStartupOrder(List<Module> modules);
}