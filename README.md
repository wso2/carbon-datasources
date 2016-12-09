# carbon-datasources

### Introduction

Carbon-datasources project is the data source implementation for Carbon 5. For any bundle deployed in Carbon 5 and any non-OSGi component , if it requires to access a database, datasources
are the preferred means of getting a connection. DataSource objects can provide connection pooling which is a must for performance improvement. For carbon-datasources,
[HikariCP](https://github.com/brettwooldridge/HikariCP) is used as its database connection pooling implementation.

Carbon-datasources is responsible to read the data source configuration files, bind the data sources into JNDI context and make these data sources available
as an OSGi service in OSGi environment or exposed through Java SPI in non-OSGi environment.

## Features

* Reading the given configuration xml files and create data source object which internally maintain a connection pool
* Exposing services to fetch and manage data source objects in both OSGi and non-OSGi environment.
* If specified in the configuration, binding data source objects to the carbon-jndi context.

## Important

* This project has a dependency with [carbon-jndi](https://github.com/wso2/carbon-jndi). Thus in order for this to work carbon-jndi needs to be in place.
* Place the required jdbc driver jar in the CARBON_HOME/osgi/dropins folder.

## Getting Started

A client bundle which needs to use data sources should put their database configuration xml files under CARBON_HOME/Conf/datasources directory. The naming
convention of the configuration file is *-datasources.xml. Refer the sample configuration file as follows;

````xml
<datasources-configuration>
    <datasources>
        <datasource>
            <name>WSO2_CARBON_DB</name>
            <description>The datasource used for registry and user manager</description>
            <jndiConfig>
                <name>jdbc/WSO2CarbonDB</name>
            </jndiConfig>
            <definition type="RDBMS">
                <configuration>
                    <url>jdbc:h2:./target/database/TEST_DB;DB_CLOSE_ON_EXIT=FALSE;LOCK_TIMEOUT=60000</url>
                    <username>wso2carbon</username>
                    <password>wso2carbon</password>
                    <driverClassName>org.h2.Driver</driverClassName>
                    <maxActive>50</maxActive>
                    <maxWait>60000</maxWait>
                    <testOnBorrow>true</testOnBorrow>
                    <validationQuery>SELECT 1</validationQuery>
                    <validationInterval>30000</validationInterval>
                    <defaultAutoCommit>false</defaultAutoCommit>
                </configuration>
            </definition>
        </datasource>
    </datasources>
</datasources-configuration>
````

The carbon-datasources bundle picks these xml configuration and build the data sources.

The client bundles could retrieve data sources in one of two ways;

* If JNDI configuration is provided in the data source configuration, use [JNDI Context Manager](https://github.com/wso2/carbon-jndi) to fetch data sources.
* Use OSGi Services provided by the carbon-datasources bundle.


1) Fetching a data source object from JNDI Context Manager

````java
public class ActivatorComponent {

    @Reference(
            name = "org.wso2.carbon.datasource.jndi",
            service = JNDIContextManager.class,
            cardinality = ReferenceCardinality.AT_LEAST_ONE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "onJNDIUnregister"
    )
    protected void onJNDIReady(JNDIContextManager jndiContextManager) {
        try {
            Context ctx = jndiContextManager.newInitialContext();
            //Cast the object to required DataSource type and perform crud operation.
            HikariDataSource dsObject = (HikariDataSource)ctx.lookup("java:comp/env/jdbc/WSO2CarbonDB");
        } catch (NamingException e) {
            logger.info("Error occurred while jndi lookup", e);
        }
    }

    protected void onJNDIUnregister(JNDIContextManager jndiContextManager) {
        logger.info("Unregistering data sources sample");
    }
}
````

Note that all the data sources are bound under the following context, `java:comp/env/jdbc`.

2) Fetching a data source object from the OSGi Service

````java
public class ActivatorComponent {

    @Reference(
            name = "org.wso2.carbon.datasource.DataSourceService",
            service = DataSourceService.class,
            cardinality = ReferenceCardinality.AT_LEAST_ONE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unregisterDataSourceService"
    )
    protected void onDataSourceServiceReady(DataSourceService dataSourceService) {
        Connection connection = null;
        try {
            HikariDataSource dsObject = (HikariDataSource) dataSourceService.getDataSource("WSO2_CARBON_DB");
            connection = dsObject.getConnection();
        } catch (DataSourceException e) {
            logger.error("error occurred while fetching the data source.", e);
        } catch (SQLException e) {
            logger.error("error occurred while fetching the connection.", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("error occurred while closing the connection.", e);
                }
            }
        }
    }

    protected void unregisterDataSourceService(DataSourceService dataSourceService) {
        logger.info("Unregistering data sources sample");
    }
}
````

In addition to retrieval of data sources, carbon-datasources bundle provides a management service for data source management activities. Following sample code
snippet illustrates how to access the management service.

````java
public class ActivatorComponent {

    @Reference(
            name = "org.wso2.carbon.datasource.DataSourceManagementService",
            service = DataSourceManagementService.class,
            cardinality = ReferenceCardinality.AT_LEAST_ONE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unregisterDataSourceManagementService"
    )
    protected void onDataSourceManagementServiceReady(DataSourceManagementService dataSourceManagementService) {
        logger.info("Sample bundle register method fired");
        try {
            DataSourceMetadata metadata = dataSourceManagementService.getDataSource("WSO2_CARBON_DB");
            logger.info(metadata.getName());
            //You can perform your functionalities by using the injected service.
        } catch (DataSourceException e) {
            logger.error("Error occurred while fetching the data sources", e);
        }
    }

    protected void unregisterDataSourceManagementService(DataSourceManagementService dataSourceManagementService) {
        logger.info("Unregistering data sources sample");
    }
}
````

Please refer the javadocs of `org.wso2.carbon.datasource.core.api.DataSourceManagementService` for usage.

For full source code, see [Carbon Datasource sample] (sample).
## Download

Use Maven snippet:
````xml
<dependency>
    <groupId>org.wso2.carbon.datasources</groupId>
    <artifactId>org.wso2.carbon.datasource.core</artifactId>
    <version>${carbon.datasource.version}</version>
</dependency>
````

### Snapshot Releases

Use following Maven repository for snapshot versions of Carbon Datasources.

````xml
<repository>
    <id>wso2.snapshots</id>
    <name>WSO2 Snapshot Repository</name>
    <url>http://maven.wso2.org/nexus/content/repositories/snapshots/</url>
    <snapshots>
        <enabled>true</enabled>
        <updatePolicy>daily</updatePolicy>
    </snapshots>
    <releases>
        <enabled>false</enabled>
    </releases>
</repository>
````

### Released Versions

Use following Maven repository for released stable versions of Carbon Datasources.

````xml
<repository>
    <id>wso2.releases</id>
    <name>WSO2 Releases Repository</name>
    <url>http://maven.wso2.org/nexus/content/repositories/releases/</url>
    <releases>
        <enabled>true</enabled>
        <updatePolicy>daily</updatePolicy>
        <checksumPolicy>ignore</checksumPolicy>
    </releases>
</repository>
````

## Building From Source

Clone this repository first (`git clone https://github.com/wso2/carbon-datasources`) and use `mvn clean install` to build .

## Contributing to Carbon Datasources Project

Pull requests are highly encouraged and we recommend you to create a GitHub issue to discuss the issue or feature that you are contributing to.

## License

Carbon Datasources is available under the Apache 2 License.

## Copyright

Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
