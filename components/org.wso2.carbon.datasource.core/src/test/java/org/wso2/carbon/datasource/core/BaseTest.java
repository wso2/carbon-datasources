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
package org.wso2.carbon.datasource.core;

import org.easymock.EasyMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.config.ConfigProviderFactory;
import org.wso2.carbon.config.ConfigurationException;
import org.wso2.carbon.config.provider.ConfigProvider;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.secvault.SecureVault;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Base test class for unit testing. Common methods for unit tests reside here.
 */
public class BaseTest {
    public SecureVault secureVault;
    protected DataSourceManager dataSourceManager;
    private static Logger logger = LoggerFactory.getLogger(BaseTest.class.getName());
    private String[] configurationFiles = {"deployment.yaml", "wso2.datasource.yaml"};

    protected void init() throws DataSourceException, ConfigurationException {
        setEnv();
        secureVault = EasyMock.mock(SecureVault.class);
        ConfigProvider configProvider = ConfigProviderFactory.getConfigProvider(Paths.get(System.getProperty("carbon" +
                ".home"), "conf", "deployment.yaml"), secureVault);
        dataSourceManager = DataSourceManager.getInstance();
        dataSourceManager.initDataSources(configProvider);
    }

    private void setEnv() {
        Path carbonHomePath = Paths.get("target", "carbonHome");
        System.setProperty("carbon.home", carbonHomePath.toFile().getAbsolutePath());

        for (String file : configurationFiles) {
            Path configFilePath = Paths.get("src", "test", "resources", "conf", file);
            Path configPathCopyLocation = Paths.get("target", "carbonHome", "conf",
                    file);
            Utils.copy(configFilePath.toFile().getAbsolutePath(), configPathCopyLocation.toFile().getAbsolutePath());
        }
    }

    protected void clearEnv() {
        System.clearProperty("carbon.home");
    }

}
