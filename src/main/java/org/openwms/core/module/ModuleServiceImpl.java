/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core.module;

import org.ameba.annotation.TxService;
import org.ameba.exception.NotFoundException;
import org.openwms.core.exception.ExceptionCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A ModuleServiceImpl is a Spring powered transactional service using a repository to execute simple CRUD operations.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
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