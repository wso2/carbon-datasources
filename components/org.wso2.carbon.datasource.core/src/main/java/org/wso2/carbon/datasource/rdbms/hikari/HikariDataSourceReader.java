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
package org.wso2.carbon.datasource.rdbms.hikari;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.spi.DataSourceReader;
import org.wso2.carbon.datasource.rdbms.RDBMSDataSourceConstants;
import org.wso2.carbon.datasource.rdbms.hikari.utils.HikariDataSourceUtils;

/**
 * HikariDataSourceReader is responsible for reading the hikari configuration from the configuration file and build
 * HikariDataSource.
 */
@Component(
        name = "org.wso2.carbon.datasource.rdbms.hikari.HikariDataSourceReader",
        immediate = true
)
public class HikariDataSourceReader implements DataSourceReader {


    @Activate
    protected void activate(BundleContext bundleContext) {

    }

    @Deactivate
    protected void deactivate(BundleContext bundleContext) {

    }

    /**
     * Return the type of the reader.
     *
     * @return String
     */
    @Override
    public String getType() {
        return RDBMSDataSourceConstants.RDBMS_DATASOURCE_TYPE;
    }

    /**
     * Creating the data source by reading the configuration.
     *
     * @param configuration             Object
     * @param isDataSourceFactoryReference boolean
     * @return Object
     * @throws DataSourceException if data source can't be created using configuration.
     */
    @Override
    public Object createDataSource(Object configuration, boolean isDataSourceFactoryReference)
            throws DataSourceException {
        HikariRDBMSDataSource dataSource = new
                HikariRDBMSDataSource(HikariDataSourceUtils.buildConfiguration(configuration));
        if (isDataSourceFactoryReference) {
            return dataSource.getDataSourceFactoryReference();
        } else {
            return dataSource.getDataSource();
        }
    }
}
