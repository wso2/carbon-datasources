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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.config.ConfigProviderFactory;
import org.wso2.carbon.config.ConfigurationException;
import org.wso2.carbon.config.provider.ConfigProvider;
import org.wso2.carbon.datasource.core.DataSourceManager;
import org.wso2.carbon.datasource.core.api.DataSourceManagementService;
import org.wso2.carbon.datasource.core.api.DataSourceService;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceManagementServiceImpl;
import org.wso2.carbon.datasource.core.impl.DataSourceServiceImpl;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


/**
 * This is a sample SPI client which uses the carbon datasources service in non-OSGi environment.
 * TODO: Fix issue #32
 */
public class DataSourceClient {
    private static Logger logger = LoggerFactory.getLogger(DataSourceClient.class);
    private static final String OS_NAME_KEY = "os.name";
    private static final String WINDOWS_PARAM = "indow";

    public static void main(String[] args) {
        DataSourceManager dataSourceManager = DataSourceManager.getInstance();
        Path configFilePath = getResourcePath("conf", "deployment.yaml").get();
        DataSourceService dataSourceService = new DataSourceServiceImpl();
        DataSourceManagementService dataSourceMgtService = new DataSourceManagementServiceImpl();
        String analyticsDataSourceName = "WSO2_ANALYTICS_DB";

        try {
            ConfigProvider configProvider = ConfigProviderFactory.getConfigProvider(configFilePath);
            //Load and initialize the datasources defined in configuration files
            dataSourceManager.initDataSources(configProvider);

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

        } catch (DataSourceException | ConfigurationException e) {
            logger.error("Error occurred while using carbon datasource.", e);
        }
    }

    /**
     * Get the path of a provided resource.
     *
     * @param resourcePaths path strings to the location of the resource
     * @return path of the resources
     */
    private static Optional<Path> getResourcePath(String... resourcePaths) {
        URL resourceURL = DataSourceClient.class.getClassLoader().getResource("");
        if (resourceURL != null) {
            String resourcePath = resourceURL.getPath();
            if (resourcePath != null) {
                resourcePath = System.getProperty(OS_NAME_KEY).contains(WINDOWS_PARAM) ?
                        resourcePath.substring(1) : resourcePath;
                System.setProperty("carbon.home", resourcePath);
                return Optional.ofNullable(Paths.get(resourcePath, resourcePaths));
            }
        }
        return Optional.empty(); // Resource do not exist
    }
}

