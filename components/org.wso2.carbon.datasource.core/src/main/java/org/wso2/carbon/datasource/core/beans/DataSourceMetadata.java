/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.datasource.core.beans;

import org.wso2.carbon.datasource.utils.DataSourceUtils;
import org.wso2.carbon.kernel.annotations.Configuration;
import org.wso2.carbon.kernel.annotations.Element;

/**
 * This class represents data source meta information.
 */
@Configuration(description = "Data Source metadata configuration")
public class DataSourceMetadata {

    @Element(description = "datasource name", required = true)
    private String name;

    @Element(description = "datasource description")
    private String description;

    private JNDIConfig jndiConfig;

    @Element(description = "datasource definition", required = true)
    private DataSourceDefinition definition;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setJndiConfig(JNDIConfig jndiConfig) {
        this.jndiConfig = jndiConfig;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public JNDIConfig getJndiConfig() {
        return jndiConfig;
    }

    public DataSourceDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(DataSourceDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object rhs) {
        if (!(rhs instanceof DataSourceMetadata)) {
            return false;
        }
        DataSourceMetadata dsmInfo = (DataSourceMetadata) rhs;
        if (!DataSourceUtils.nullAllowEquals(dsmInfo.getName(), this.getName())) {
            return false;
        }
        if (!DataSourceUtils.nullAllowEquals(dsmInfo.getDescription(), this.getDescription())) {
            return false;
        }
        if (!DataSourceUtils.nullAllowEquals(dsmInfo.getJndiConfig(), this.getJndiConfig())) {
            return false;
        }
        return DataSourceUtils.nullAllowEquals(dsmInfo.getDefinition(), this.getDefinition());
    }

    @Override
    public int hashCode() {
        if (name != null && description != null && jndiConfig != null && definition != null) {
            return name.hashCode() + description.hashCode() + jndiConfig.hashCode() + definition.hashCode();
        } else {
            return super.hashCode();
        }
    }

}
