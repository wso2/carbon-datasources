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

import org.w3c.dom.Element;
import org.wso2.carbon.datasource.utils.DataSourceUtils;

/**
 * This class represents data source meta information.
 */
public class DataSourceMetadata {

    private String name;

    private String description;

    private JNDIConfig jndiConfig;

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

    /**
     * CLass holding the data source definition.
     */
    public static class DataSourceDefinition {

        private String type;

        private Object configuration;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getDataSourceConfiguration() {
            return configuration;
        }

        public void setDataSourceConfiguration(Object configuration) {
            this.configuration = configuration;
        }

        public boolean equals(Object rhs) {
            if (!(rhs instanceof DataSourceDefinition)) {
                return false;
            }
            DataSourceDefinition dsDef = (DataSourceDefinition) rhs;
            if (!DataSourceUtils.nullAllowEquals(dsDef.getType(), this.getType())) {
                return false;
            }
            return DataSourceUtils.nullAllowEquals(DataSourceUtils.elementToString(
                            (Element) dsDef.getDataSourceConfiguration()),
                    DataSourceUtils.elementToString((Element) this.getDataSourceConfiguration()));
        }

        @Override
        public int hashCode() {
            assert false : "hashCode() not implemented";
            return -1;
        }

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
        assert false : "hashCode() not implemented";
        return -1;
    }

}
