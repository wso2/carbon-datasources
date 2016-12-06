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
import org.wso2.carbon.datasource.core.api.DataSourceManagementService;
import org.wso2.carbon.datasource.core.beans.DataSourceMetadata;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceManagementServiceImpl;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Test class for {@code DataSourceManagementService}.
 */
public class DataSourceManagementServiceTest extends BaseTest {

    private DataSourceManagementService dataSourceMgtService;
    private static final String DATASOURCE_NAME = "WSO2_CARBON_DB_2";

    @BeforeSuite
    public void initialize() throws DataSourceException, MalformedURLException {
        super.init();
        dataSourceMgtService = new DataSourceManagementServiceImpl();
    }

    @Test
    public void getDataSourceTest() throws DataSourceException {
        DataSourceMetadata dataSourceMetadata = dataSourceMgtService.getDataSource(DATASOURCE_NAME);
        Assert.assertNotNull(dataSourceMetadata, "metadata for \"" + DATASOURCE_NAME + "\" should not be null");
    }

    @Test(dependsOnMethods = "getDataSourceTest")
    public void getDataSourceListTest() throws DataSourceException {
        List<DataSourceMetadata> dataSourceMetadata = dataSourceMgtService.getDataSource();
        Assert.assertEquals(1, dataSourceMetadata.size(), "Only one " + DATASOURCE_NAME + " exist in the repository.");
    }

    @Test(dependsOnMethods = "getDataSourceListTest")
    public void addAndDeleteDataSourceTest() throws DataSourceException {
        DataSourceMetadata dataSourceMetadata = dataSourceMgtService.getDataSource(DATASOURCE_NAME);
        Assert.assertNotNull(dataSourceMetadata, "dataSourceMetadata should not be null");
        dataSourceMgtService.deleteDataSource(DATASOURCE_NAME);
        DataSourceMetadata dataSourceMetadata2 = dataSourceMgtService.getDataSource(DATASOURCE_NAME);
        Assert.assertNull(dataSourceMetadata2, "After deleting, " + DATASOURCE_NAME + " should not exist in the "
                + "repository");
        dataSourceMgtService.addDataSource(dataSourceMetadata);
        Assert.assertNotNull(dataSourceMgtService.getDataSource(DATASOURCE_NAME), DATASOURCE_NAME + " should exist in"
                + " the repository");
    }
}
