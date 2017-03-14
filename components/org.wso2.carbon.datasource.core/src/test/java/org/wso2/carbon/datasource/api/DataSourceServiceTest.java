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
import org.wso2.carbon.datasource.core.beans.DataSourceDefinition;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceServiceImpl;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

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
            String configuration = "      type: RDBMS\n" +
                    "        # data source configuration\n" +
                    "      configuration:\n" +
                    "        jdbcUrl: 'jdbc:h2:./database/WSO2_CARBON_DB;DB_CLOSE_ON_EXIT=FALSE;'\n" +
                    "        username: wso2carbon\n" +
                    "        password: wso2carbon\n" +
                    "        driverClassName: org.h2.Driver\n" +
                    "        maxPoolSize: 50\n" +
                    "        idleTimeout: 60000\n" +
                    "        connectionTestQuery: SELECT 1\n" +
                    "        validationTimeout: 30000\n" +
                    "        isAutoCommit: false";
            Yaml yaml = new Yaml(new CustomClassLoaderConstructor(DataSourceDefinition.class,
                    DataSourceDefinition.class.getClassLoader()));
            yaml.setBeanAccess(BeanAccess.FIELD);
            DataSourceDefinition dataSourceDefinition = yaml.loadAs(configuration, DataSourceDefinition.class);

            Object dataSourceObj = dataSourceService.createDataSource(dataSourceDefinition);
            Assert.assertNotNull(dataSourceObj, "Failed to create datasource Object!");
        } catch (DataSourceException e) {
            Assert.fail("Threw an exception while creating datasource Object");
        }
    }
}
