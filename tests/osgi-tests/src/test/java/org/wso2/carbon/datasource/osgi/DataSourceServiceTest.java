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
import org.wso2.carbon.osgi.test.util.CarbonOSGiTestEnvConfigs;
import org.wso2.carbon.osgi.test.util.utils.CarbonOSGiTestUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

@Listeners(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class DataSourceServiceTest {

    @Configuration
    public Option[] createConfiguration() {
        Utils.copyResources();
        List<Option> customOptions = new ArrayList<>();
        customOptions.add(mavenBundle().artifactId("org.wso2.carbon.datasource.core")
                .groupId("org.wso2.carbon.datasources")
                .versionAsInProject());
        customOptions.add(mavenBundle().artifactId("datasources-sample")
                .groupId("org.wso2.carbon.datasources")
                .version("1.0.0-SNAPSHOT"));
        CarbonOSGiTestEnvConfigs configs = new CarbonOSGiTestEnvConfigs();
        Path carbonHome = Paths.get("tests", "osgi-tests", "target", "carbonHome");
//        Path carbonHome = Paths.get("target", "carbonHome");
        configs.setCarbonHome(carbonHome.toFile().getAbsolutePath());
        return CarbonOSGiTestUtils.getAllPaxOptions(configs, customOptions);
    }

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
