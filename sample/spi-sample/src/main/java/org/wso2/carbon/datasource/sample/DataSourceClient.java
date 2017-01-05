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
package org.wso2.carbon.datasource.sample;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.datasource.core.DataSourceManager;
import org.wso2.carbon.datasource.core.api.DataSourceManagementService;
import org.wso2.carbon.datasource.core.api.DataSourceService;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceManagementServiceImpl;
import org.wso2.carbon.datasource.core.impl.DataSourceServiceImpl;
import org.wso2.carbon.jndi.internal.spi.builder.DefaultContextFactoryBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

/**
 * This is a sample SPI client which uses the carbon datasources service in non-OSGi environment.
 */
public class DataSourceClient {
    private static Logger logger = LoggerFactory.getLogger(DataSourceClient.class);

    public static void main(String[] args) {
        DataSourceManager dataSourceManager = DataSourceManager.getInstance();
        Path configFilePath = Paths.get("src", "main", "resources", "conf", "datasources");
        DataSourceService dataSourceService = new DataSourceServiceImpl();
        DataSourceManagementService dataSourceMgtService = new DataSourceManagementServiceImpl();
        String analyticsDataSourceName = "WSO2_ANALYTICS_DB";
        String analyticsDataSourceJndiConfigName = "java:comp/env/jdbc/WSO2AnalyticsDB/test";

        try {
            //InitialContextFactoryBuilder has to be set to NamingManager to use InitialContext API
            NamingManager.setInitialContextFactoryBuilder(new DefaultContextFactoryBuilder());

            //Load and initialize the datasources defined in configuration files
            dataSourceManager.initDataSources(configFilePath.toFile().getAbsolutePath());

            //Get datasources using DataSourceManagement service
            logger.info("Initial data source count: " + dataSourceMgtService.getDataSource().size());

            //Get a particular datasource using its name
            logger.info("Found " + analyticsDataSourceName + ": " + (
                    dataSourceService.getDataSource(analyticsDataSourceName) != null ? true : false));

            //Get a datasource username using jndi lookup
            Context context = new InitialContext();
            logger.info(analyticsDataSourceName + " datasource username retrieved via jndi lookup: "
                    + ((HikariDataSource) context.lookup(analyticsDataSourceJndiConfigName)).getUsername());

            //Delete a datasource using its name
            dataSourceMgtService.deleteDataSource(analyticsDataSourceName);
            logger.info("Deleted " + analyticsDataSourceName + " successfully");
            logger.info("Data source count after deleting " + analyticsDataSourceName + ": " + dataSourceMgtService
                    .getDataSource().size());
        } catch (DataSourceException | NamingException e) {
            logger.error("Error occurred while using carbon datasource.", e);
        }
    }
}

