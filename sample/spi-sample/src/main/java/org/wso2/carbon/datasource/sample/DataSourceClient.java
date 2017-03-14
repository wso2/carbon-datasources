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

/**
 * This is a sample SPI client which uses the carbon datasources service in non-OSGi environment.
 * TODO: Fix issue #32
 */
public class DataSourceClient {
/*    private static Logger logger = LoggerFactory.getLogger(DataSourceClient.class);

    public static void main(String[] args) {
        DataSourceManager dataSourceManager = DataSourceManager.getInstance();
        Path configFilePath = Paths.get("src", "main", "resources", "conf", "datasources");
        DataSourceService dataSourceService = new DataSourceServiceImpl();
        DataSourceManagementService dataSourceMgtService = new DataSourceManagementServiceImpl();
        String analyticsDataSourceName = "WSO2_ANALYTICS_DB";

        try {
            //Load and initialize the datasources defined in configuration files
            dataSourceManager.initDataSources(configFilePath.toFile().getAbsolutePath());

            //Get datsources using DataSourceManagement service
            logger.info("Initial data source count: " + dataSourceMgtService.getDataSource().size());

            //Get a particular datasource using its name
            logger.info("Found " + analyticsDataSourceName + ": " + (
                    dataSourceService.getDataSource(analyticsDataSourceName) != null ? true : false));

            //Delete a datasource using its name
            dataSourceMgtService.deleteDataSource(analyticsDataSourceName);
            logger.info("Deleted " + analyticsDataSourceName + " successfully");
            logger.info("Data source count after deleting " + analyticsDataSourceName + ": " + dataSourceMgtService
                    .getDataSource().size());

        } catch (DataSourceException e) {
            logger.error("Error occurred while using carbon datasource.", e);
        }
    }*/
}

