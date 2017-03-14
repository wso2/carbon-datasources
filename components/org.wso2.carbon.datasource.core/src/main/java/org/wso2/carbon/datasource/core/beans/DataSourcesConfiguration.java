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

import org.wso2.carbon.kernel.annotations.Configuration;
import org.wso2.carbon.kernel.annotations.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the system data sources configuration.
 */
@Configuration(namespace = "wso2.datasources", description = "Data Sources Configuration")
public class DataSourcesConfiguration {

    public DataSourcesConfiguration() {
        dataSources = new ArrayList<>();
        dataSources.add(new DataSourceMetadata());
    }

    @Element(description = "datasources")
    private List<DataSourceMetadata> dataSources;

    public List<DataSourceMetadata> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceMetadata> dataSources) {
        this.dataSources = dataSources;
    }

}
