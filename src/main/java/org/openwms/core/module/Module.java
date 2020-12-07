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

import org.ameba.integration.jpa.BaseEntity;
import org.openwms.core.values.CoreTypeDefinitions;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Comparator;

/**
 * A Module represents an Adobe Flex Module and is used to store some basic information about that module, i.e. a name, an URL where the
 * module from, or whether the Adobe Flex Module should be loaded on application startup.
 * 
 * @GlossaryTerm
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "COR_MODULE")
@NamedQueries({ @NamedQuery(name = Module.NQ_FIND_ALL, query = "select m from Module m order by m.startupOrder"),
        @NamedQuery(name = Module.NQ_FIND_BY_UNIQUE_QUERY, query = "select m from Module m where m.moduleName = ?1") })
public class Module extends BaseEntity implements Serializable {

    /**
     * Unique name of the <code>Module</code> (natural key, unique, not-null).
     */
    @Column(name = "C_MODULE_NAME", unique = true, nullable = false)
    private String moduleName;
    /**
     * URL from where to load this <code>Module</code> (unique, not-null).
     */
    @Column(name = "C_URL", unique = true, nullable = false)
    private String url;
    /**
     * Flag used on the client side to store whether the <code>Module</code> is actually loaded or not. It's a dynamic value and not
     * persisted.
     */
    @Transient
    private boolean loaded = false;
    /**
     * <code>true</code> when the <code>Module</code> should be loaded on application startup.
     */
    @Column(name = "C_LOAD_ON_STARTUP")
    private boolean loadOnStartup = true;
    /**
     * Defines the startup order compared with other Modules. Modules with a lower <code>startupOrder</code> are loaded before this one.
     */
    @Column(name = "C_STARTUP_ORDER")
    @OrderBy
    private int startupOrder;
    /**
     * A description text of this <code>Module</code>.
     */
    @Column(name = "C_DESCRIPTION", length = CoreTypeDefinitions.DESCRIPTION_LENGTH)
    private String description = "--";
    /**
     * Query to find all <code>Module</code>s. Name is {@value} .
     */
    public static final String NQ_FIND_ALL = "Module.findAll";
    /**
     * Query to find <strong>one</strong> <code>Module</code> by its natural key. <li>
     * Query parameter index <strong>1</strong> : The <code>moduleName</code> of the <code>Module</code> to search for</li><br />
     * Name is {@value} .
     */
    public static final String NQ_FIND_BY_UNIQUE_QUERY = "Module.findByModuleName";

    /**
     * A ModuleComparator.
     * 
     * @author Heiko Scherrer
     */
    public static class ModuleComparator implements Comparator<Module>, Serializable {

        /**
         * {@inheritDoc}
         * 
         * Return 1 when the startupOrder of o1 is greater or equals than the startupOrder of o2, -1 when it is less.
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Module o1, Module o2) {
            return o1.getStartupOrder() >= o2.getStartupOrder() ? 1 : -1;
        }
    }

    /**
     * Dear JPA ...
     */
    protected Module() {
    }

    /**
     * Create a new <code>Module</code>.
     * 
     * @param moduleName
     *            The unique <code>Module</code> name
     * @param url
     *            The unique URL
     * @throws IllegalArgumentException
     *             in case the new moduleName is <code>null</code> or empty
     */
    public Module(String moduleName, String url) {
        super();
        Assert.hasText(moduleName, "Not allowed to set the moduleName to null or an empty String");
        this.moduleName = moduleName;
        this.url = url;
    }

    /**
     * Get the <code>moduleName</code>.
     * 
     * @return the moduleName.
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Set the <code>moduleName</code>.
     * 
     * @param moduleName
     *            The moduleName to set
     * @throws IllegalArgumentException
     *             in case the new moduleName is <code>null</code> or empty
     */
    public void setModuleName(String moduleName) {
        Assert.hasText(moduleName, "Not allowed to set the moduleName to null or an empty String");
        this.moduleName = moduleName;
    }

    /**
     * Get the <code>url</code>.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the <code>url</code>.
     * 
     * @param url
     *            The url to set
     * @throws IllegalArgumentException
     *             in case the new url is <code>null</code> or empty
     */
    public void setUrl(String url) {
        Assert.hasText(url, "Not allowed to set the url to null or an empty String");
        this.url = url;
    }

    /**
     * Is the <code>Module</code> currently loaded.
     * 
     * @return <code>true</code> if loaded, otherwise <code>false</code>
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Set the <code>loaded</code> flag.
     * 
     * @param loaded
     *            The loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Get the <code>description</code>.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the <code>description</code>.
     * 
     * @param description
     *            The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Should the <code>Module</code> be loaded on application startup.
     * 
     * @return <code>true</code> if the <code>Module</code> should be loaded on application startup, otherwise <code>false</code>
     */
    public boolean isLoadOnStartup() {
        return loadOnStartup;
    }

    /**
     * Set the <code>loadOnStartup</code> flag.
     * 
     * @param loadOnStartup
     *            The loadOnStartup to set
     */
    public void setLoadOnStartup(boolean loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    /**
     * Get the <code>startupOrder</code>.
     * 
     * @return the startupOrder
     */
    public int getStartupOrder() {
        return startupOrder;
    }

    /**
     * Set the <code>startupOrder</code>.
     * 
     * @param startupOrder
     *            The startupOrder to set
     */
    public void setStartupOrder(int startupOrder) {
        this.startupOrder = startupOrder;
    }

    /**
     * Returns the <code>moduleName</code>.
     * 
     * @see java.lang.Object#toString()
     * @return The moduleName
     */
    @Override
    public String toString() {
        return moduleName;
    }
}