package org.wso2.carbon.datasource.osgi;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.testng.listener.PaxExam;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.wso2.carbon.datasource.core.api.DataSourceService;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.osgi.utils.Utils;
import org.wso2.carbon.kernel.utils.CarbonServerInfo;
import org.wso2.carbon.osgi.test.util.CarbonOSGiTestEnvConfigs;
import org.wso2.carbon.osgi.test.util.utils.CarbonOSGiTestUtils;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

@Listeners(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class DataSourceServiceTest {

    @Configuration
    public Option[] createConfiguration() {
        Utils.setEnv();
        List<Option> customOptions = new ArrayList<>();
        customOptions.add(mavenBundle().artifactId("commons-io")
                .groupId("commons-io.wso2")
                .version("2.4.0.wso2v1"));
        customOptions.add(mavenBundle().artifactId("HikariCP")
                .groupId("com.zaxxer")
                .version("2.4.1"));
        customOptions.add(mavenBundle().artifactId("mysql-connector-java")
                .groupId("mysql")
                .version("5.1.38"));


        customOptions.add(mavenBundle().artifactId("org.wso2.carbon.datasource.core")
                .groupId("org.wso2.carbon.datasources")
                .versionAsInProject());
        CarbonOSGiTestEnvConfigs configs = new CarbonOSGiTestEnvConfigs();

        String carbonHome = System.getProperty("carbon.home");
        configs.setCarbonHome(carbonHome);
        return CarbonOSGiTestUtils.getAllPaxOptions(configs, customOptions);
    }

    @Inject
    private CarbonServerInfo carbonServerInfo;

    @Inject
    private DataSourceService dataSourceService;

    @Test
    public void testDataSourceServiceInject() {
        Assert.assertNotNull(dataSourceService, "DataSourceService not found");
    }

    @Test
    public void testGetAllDataSources() {
        try {
            Object obj = dataSourceService.getDataSource("WSO2_CARBON_DB");
            Assert.assertNotNull(obj, "WSO2_CARBON_DB should exist");
        } catch (DataSourceException e) {
            Assert.fail("Threw an exception while retrieving data sources");
        }
    }
}
