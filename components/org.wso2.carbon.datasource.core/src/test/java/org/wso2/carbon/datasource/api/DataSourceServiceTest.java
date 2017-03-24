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
package org.wso2.carbon.datasource.api;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.wso2.carbon.datasource.core.BaseTest;
import org.wso2.carbon.datasource.core.api.DataSourceService;
import org.wso2.carbon.datasource.core.beans.DataSourcesConfiguration;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceServiceImpl;
import org.wso2.carbon.kernel.configprovider.CarbonConfigurationException;
import org.wso2.carbon.kernel.configprovider.ConfigFileReader;
import org.wso2.carbon.kernel.configprovider.ConfigProvider;
import org.wso2.carbon.kernel.configprovider.YAMLBasedConfigFileReader;
import org.wso2.carbon.kernel.internal.configprovider.ConfigProviderImpl;

import java.net.MalformedURLException;

/**
 * Test class for {@code DataSourceService}.
 */
public class DataSourceServiceTest extends BaseTest {

    private DataSourceService dataSourceService;

    @BeforeSuite
    public void initialize() throws DataSourceException, MalformedURLException {
        super.init();
        dataSourceService = new DataSourceServiceImpl();
    }

    @Test
    public void getDataSourceTest() throws DataSourceException {
        final String datasourceName = "WSO2_CARBON_DB_2";
        Object dataSourceObject = dataSourceService.getDataSource(datasourceName);
        Assert.assertNotNull(dataSourceObject, "Test datasource \"" + datasourceName + "\" should not be null");
    }

    @Test(expectedExceptions = DataSourceException.class)
    public void getDataSourceFailTest() throws DataSourceException {
        dataSourceService.getDataSource(null);
    }

    @Test
    public void createDataSourceTest() throws DataSourceException {
        try {
            ConfigFileReader fileReader = new YAMLBasedConfigFileReader("wso2.datasource.yaml");
            ConfigProvider configProvider = new ConfigProviderImpl(fileReader);
            DataSourcesConfiguration dataSourcesConfiguration = configProvider.getConfigurationObject
                    (DataSourcesConfiguration
                    .class);

            Object dataSourceObj = dataSourceService.createDataSource(dataSourcesConfiguration.getDataSources().get
                    (0).getDefinition());
            Assert.assertNotNull(dataSourceObj, "Failed to create datasource Object!");
        } catch (DataSourceException | CarbonConfigurationException e) {
            Assert.fail("Threw an exception while creating datasource Object");
        }
    }
}
