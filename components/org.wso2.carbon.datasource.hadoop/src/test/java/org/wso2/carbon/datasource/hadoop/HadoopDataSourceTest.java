/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.datasource.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.datasource.core.DataSourceManager;
import org.wso2.carbon.datasource.core.api.DataSourceManagementService;
import org.wso2.carbon.datasource.core.api.DataSourceService;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceManagementServiceImpl;
import org.wso2.carbon.datasource.core.impl.DataSourceServiceImpl;
import org.wso2.carbon.datasource.core.spi.DataSourceReader;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.wso2.carbon.datasource.hadoop.HadoopDataSourceConstants.DATASOURCE_TYPE_HADOOP;

public class HadoopDataSourceTest {

    private DataSourceManager dataSourceManager;
    private DataSourceManagementService dataSourceMgtService;
    private DataSourceService dataSourceService;
    private static Logger logger = LoggerFactory.getLogger(HadoopDataSourceTest.class);

    @BeforeClass
    public void initialize() throws DataSourceException, MalformedURLException {
        logger.info("Running tests for Hadoop datasource");
        Path configPathLocation = Paths.get("src", "test", "resources", "conf", "datasources");
        dataSourceManager = DataSourceManager.getInstance();
        dataSourceManager.initDataSources(configPathLocation.toAbsolutePath().toString());
        dataSourceMgtService = new DataSourceManagementServiceImpl();
        dataSourceService = new DataSourceServiceImpl();
    }

    @Test
    public void testDataSourceAvailability() throws DataSourceException {
        int dataSourcesCount = dataSourceMgtService.getDataSource().size();
        Assert.assertTrue(dataSourcesCount > 0, "There should be at least one defined datasource available");
    }

    @Test(dependsOnMethods = "testDataSourceAvailability")
    public void testDataSourceType() {
        List<String> types = dataSourceManager.getDataSourceTypes();
        Assert.assertEquals(types.get(0), DATASOURCE_TYPE_HADOOP,
                "Expected defined data source of type '" + DATASOURCE_TYPE_HADOOP + "' but found '"
                        + types.get(0) + "'");
    }

    @Test(dependsOnMethods = "testDataSourceAvailability")
    public void testDataSourceProvider() throws DataSourceException {
        DataSourceReader reader = dataSourceManager.getDataSourceReader(DATASOURCE_TYPE_HADOOP);
        Assert.assertEquals(reader.getType(), DATASOURCE_TYPE_HADOOP,
                "Expected data source reader of type '" + DATASOURCE_TYPE_HADOOP + "' but found '"
                        + reader.getType() + "'");
    }

    @Test(dependsOnMethods = "testDataSourceType")
    public void testDataSourceInitialization() throws DataSourceException {
        final String dataSourceName = "WSO2_HADOOP_DB";
        Object dataSource = dataSourceService.getDataSource(dataSourceName);
        Assert.assertNotNull(dataSource, "Test datasource '" + dataSourceName + "' should not be null");
    }

    @Test(dependsOnMethods = "testDataSourceInitialization")
    public void testHadoopDataSource() throws DataSourceException {
        final String dataSourceName = "WSO2_HADOOP_DB";
        Configuration hadoopDataSource = (Configuration) dataSourceService.getDataSource(dataSourceName);
        String propertyValue = hadoopDataSource.get("hbase.master");
        Assert.assertEquals(propertyValue, "localhost:60000",
                "Expected Hadoop property to be 'localhost:60000' but found '" + propertyValue + "'");
    }

}
