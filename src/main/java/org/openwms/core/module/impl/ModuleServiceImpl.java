/*
 * Copyright 2005-2019 the original author or authors.
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
package org.openwms.core.module.impl;

import org.ameba.annotation.TxService;
import org.ameba.exception.NotFoundException;
import org.openwms.core.exception.ExceptionCodes;
import org.openwms.core.module.Module;
import org.openwms.core.module.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A ModuleServiceImpl is a Spring powered transactional service using a repository to execute simple CRUD operations.
 *
 * @author Heiko Scherrer
 */
@TxService
class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;

    @Autowired
    ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is expected that the list of {@link Module}s is already ordered by their startup
     * order. Each {@link Module}'s {@code startupOrder} is synchronized with the
     * persistence storage.
     *
     * @throws IllegalArgumentException when {@code modules} is {@literal null}
     */
    @Override
    public void saveStartupOrder(@NotNull List<Module> modules) {
        Assert.notEmpty(modules, ExceptionCodes.MODULE_SAVE_STARTUP_ORDER_NOT_BE_NULL);
        for (Module module : modules) {
            Module toSave = moduleRepository.findById(module.getPk()).orElseThrow(() -> new NotFoundException(String.format("Module with id [%s] not found", module.getPk())));
            toSave.setStartupOrder(module.getStartupOrder());
            save(toSave);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Module> findAll() {
        return moduleRepository.findAll();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additionally the {@code startupOrder} is re-calculated for a new {@code module}.
     *
     * @throws IllegalArgumentException when {@code module} is {@literal null}
     */
    @Override
    public Module save(@NotNull Module module) {
        Assert.notNull(module, ExceptionCodes.MODULE_SAVE_NOT_BE_NULL);
        if (module.isNew()) {
            List<Module> all = findAll();
            if (!all.isEmpty()) {
                all.sort(new Module.ModuleComparator());
                module.setStartupOrder(all.get(all.size() - 1).getStartupOrder() + 1);
            }
        }
        return moduleRepository.save(module);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull Module module) {
        moduleRepository.delete(module);
    }
}