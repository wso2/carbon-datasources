# carbon-datasources

### Introduction

Carbon-datasources project is the data source implementation for Carbon 5. For any bundle deployed in Carbon 5, if it require to access a database, datasources
are the preferred means of getting a connection. DataSource objects can provide connection pooling which is a must for performance improvement. For carbon-datasources,
[HikariCP](https://github.com/brettwooldridge/HikariCP) is used as it's database connection pooling implementation.

Carbon-datasources is responsible to read the data source configuration files, bind the data sources into JNDI context and make these data sources available
as an OSGi service.

## Features:

* Reading the given configuration xml files and create data source object which internally maintain a connection pool
* Exposing OSGi Services to add, fetch data source objects.
* If specified in the configuration, binding data source objects to the carbon-jndi context.

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
    protected void onJNDIReady(JNDIContextManager service) {

        try {
            Context ctx = service.newInitialContext();
            Object obj = ctx.lookup("java:comp/env/jdbc/WSO2CarbonDB");
            logger.info("Fetched data source: " + obj.toString());
            //Cast the object to required DataSource type and perform crud operation.
        } catch (NamingException e) {
            logger.info("Error occurred while jndi lookup", e);
        }
    }

    protected void onJNDIUnregister(JNDIContextManager jndiContextManager) {
        logger.info("Unregistering data sources sample");
    }
}
````

Note that all the data sources will be bound under the following context, java:comp/env.

### Usage

- This maven project contains 3 maven modules.
- org.wso2.carbon.datasource.core module will create an OSGi bundle which will read the datasource configuration files and bind through jndi.
- feature module will encapsulate org.wso2.carbon.datasource.core.jar and it's dependencies and create a feature so it is installable into carbon-kernel.
- distribution module, which will download carbon kernel 5.0.0, install the feature and zip it with the name 'carbon-datasource-1.0.0-SNAPSHOT.zip'
- Extracted location of the aforementioned zip file is considered as CARBON_HOME for this readme.
- data source configuration file can be found in CARBON_HOME/conf/datasources directory. Update the master-datasources.xml file to suite your requirement.
- In addition you can place any data source configuration file in CARBON_HOME/conf/datasources directory having it's file name ends with '-datasources.xml'. This is the convention used in previous carbon versions.
- Place the required jdbc driver jar in the CARBON_HOME/osgi/dropins folder.

#### Important

This project has a dependency with carbon-jndi. Thus in order for this to work carbon-jndi needs to be in place.


After completing aforementioned activities, start C5 kernel by running carbon.sh or carbon.bat.
