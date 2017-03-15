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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.kernel.configprovider.ConfigFileReader;
import org.wso2.carbon.kernel.configprovider.ConfigProvider;
import org.wso2.carbon.kernel.configprovider.XMLBasedConfigFileReader;
import org.wso2.carbon.kernel.configprovider.YAMLBasedConfigFileReader;
import org.wso2.carbon.kernel.internal.configprovider.ConfigProviderImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Base test class for unit testing. Common methods for unit tests reside here.
 */
public class BaseTest {
    public static final String CONFIGURATION_FILE = "deployment.yaml";
    protected DataSourceManager dataSourceManager;
    private static Logger logger = LoggerFactory.getLogger(BaseTest.class.getName());

    protected void init() throws DataSourceException {
        setEnv();
        clearDeploymentConfiguration();
        ConfigProvider configProvider = new ConfigProviderImpl(new YAMLBasedConfigFileReader(CONFIGURATION_FILE));
        dataSourceManager = DataSourceManager.getInstance();
        dataSourceManager.initDataSources(configProvider);
    }

    private void setEnv() {
        Path carbonHomePath = Paths.get("target", "carbonHome");
        System.setProperty("carbon.home", carbonHomePath.toFile().getAbsolutePath());

        Path configFilePath = Paths.get("src", "test", "resources", "conf", CONFIGURATION_FILE);
        Path configPathCopyLocation = Paths.get("target", "carbonHome", "conf",
                CONFIGURATION_FILE);
        Utils.copy(configFilePath.toFile().getAbsolutePath(), configPathCopyLocation.toFile().getAbsolutePath());
    }

    protected void clearEnv() {
        System.clearProperty("carbon.home");
    }


    protected void clearDeploymentConfiguration() {
        try {
            Class providerClass = Class.forName("org.wso2.carbon.kernel.internal.configprovider.ConfigProviderImpl");
            ConfigFileReader fileReader = new XMLBasedConfigFileReader(CONFIGURATION_FILE);
            Constructor<?> cons = providerClass.getConstructor(ConfigFileReader.class);
            Object providerObject = cons.newInstance(fileReader);
            Field field = providerClass.getDeclaredField("deploymentConfigs");
            field.setAccessible(true);
            field.set(providerObject, null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | InstantiationException |
                NoSuchMethodException | InvocationTargetException e) {
            logger.error("Error while cleaning deployment configuration.", e);
        }
    }
}
