/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.wso2.carbon.config.ConfigProviderFactory;
import org.wso2.carbon.config.ConfigurationException;
import org.wso2.carbon.config.provider.ConfigProvider;
import org.wso2.carbon.datasource.core.DataSourceManager;
import org.wso2.carbon.datasource.core.Utils;
import org.wso2.carbon.datasource.core.api.DataSourceManagementService;
import org.wso2.carbon.datasource.core.beans.DataSourceMetadata;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceManagementServiceImpl;
import org.wso2.carbon.secvault.SecureVault;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Test class for {@code DataSourceManagementService}.
 */
public class StreamlinedConfigTest {

    public SecureVault secureVault;
    protected DataSourceManager dataSourceManager;
    private DataSourceManagementService dataSourceMgtService;
    private static final String DATASOURCE_NAME = "WSO2_CARBON_DB_2";
    private String[] configurationFiles = {"deployment.yaml", "wso2.datasource.yaml"};

    @BeforeSuite
    public void initialize() throws DataSourceException, ConfigurationException {
        setEnv();
        secureVault = EasyMock.mock(SecureVault.class);
        ConfigProvider configProvider = ConfigProviderFactory.getConfigProvider(Paths.get(System.getProperty("carbon" +
                ".home2"), "conf", "deployment.yaml"), secureVault);
        dataSourceManager = DataSourceManager.getInstance();
        dataSourceManager.initDataSources(configProvider);
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

    private void setEnv() {
        Path carbonHomePath = Paths.get("target", "carbonHome2");
        System.setProperty("carbon.home2", carbonHomePath.toFile().getAbsolutePath());

        for (String file : configurationFiles) {
            Path configFilePath = Paths.get("src", "test", "resources", "confnew", file);
            Path configPathCopyLocation = Paths.get("target", "carbonHome2", "conf",
                    file);
            Utils.copy(configFilePath.toFile().getAbsolutePath(), configPathCopyLocation.toFile().getAbsolutePath());
        }
    }

}
