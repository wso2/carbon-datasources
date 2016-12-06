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
package org.wso2.carbon.datasource.osgi;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.testng.listener.PaxExam;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.wso2.carbon.datasource.core.api.DataSourceManagementService;
import org.wso2.carbon.datasource.core.beans.DataSourceMetadata;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.osgi.utils.OSGiTestUtils;
import org.wso2.carbon.kernel.utils.CarbonServerInfo;

import java.util.List;
import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

/**
 * Test class for {@link DataSourceManagementService}.
 */
@Listeners(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class DataSourceManagementServiceTest {
    private static final String DATASOURCE_NAME = "WSO2_CARBON_DB";

    @Inject
    private DataSourceManagementService dataSourceManagementService;

    @Inject
    private CarbonServerInfo carbonServerInfo;

    @Configuration
    public Option[] createConfiguration() {
        OSGiTestUtils.setEnv();

        Option[] options = CoreOptions.options(
                mavenBundle().artifactId("commons-io").groupId("commons-io.wso2").version("2.4.0.wso2v1"),
                mavenBundle().artifactId("HikariCP").groupId("com.zaxxer").version("2.4.1"),
                mavenBundle().artifactId("h2").groupId("com.h2database").version("1.4.191"),
                mavenBundle().artifactId("org.wso2.carbon.datasource.core").groupId("org.wso2.carbon.datasources")
                        .versionAsInProject(),
                mavenBundle().artifactId("org.wso2.carbon.jndi").groupId("org.wso2.carbon.jndi").versionAsInProject()
        );
        return OSGiTestUtils.getDefaultPaxOptions(options);
    }

    @Test
    public void testDataSourceManagementServiceInject() {
        Assert.assertNotNull(dataSourceManagementService, "DataSourceManagementService not found");
    }

    @Test
    public void testGetDataSource() {
        try {
            List<DataSourceMetadata> list  = dataSourceManagementService.getDataSource();
            Assert.assertEquals(list.size(), 1, "Only one data source is registered");
        } catch (DataSourceException e) {
            Assert.fail("Thew DataSourceException when fetching data sources");
        }
    }

    @Test
    public void testGetDataSourceForName() {
        try {
            DataSourceMetadata dataSource  = dataSourceManagementService.getDataSource(DATASOURCE_NAME);
            Assert.assertNotNull(dataSource, "Data source " + DATASOURCE_NAME + " should exist");
        } catch (DataSourceException e) {
            Assert.fail("Thew DataSourceException when fetching data sources");
        }
    }

    @Test(dependsOnMethods = { "testGetDataSource", "testGetDataSourceForName" })
    public void testAddAndDeleteDataSource() {
        try {
            DataSourceMetadata dataSource  = dataSourceManagementService.getDataSource(DATASOURCE_NAME);
            Assert.assertNotNull(dataSource, "Data source " + DATASOURCE_NAME + " should exist");
            dataSourceManagementService.deleteDataSource(DATASOURCE_NAME);
            DataSourceMetadata dataSource2  = dataSourceManagementService.getDataSource(DATASOURCE_NAME);
            Assert.assertNull(dataSource2, "After deleting the data source should not exist");
            dataSourceManagementService.addDataSource(dataSource);
            dataSource2  = dataSourceManagementService.getDataSource(DATASOURCE_NAME);
            Assert.assertNotNull(dataSource2, "The service did not fetch the inserted data source!!!");
        } catch (DataSourceException e) {
            Assert.fail("Thew DataSourceException when fetching data sources");
        }

    }
}
