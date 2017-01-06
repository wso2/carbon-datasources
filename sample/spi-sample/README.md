# Carbon Datasource SPI Sample

This is a sample java client application which uses WSO2 carbon-datasources services exposed through Java SPI to access them in non-OSGi environment. The carbon datasources are supported with In-memory JNDI context service provider to be used in non-OSGi environment by default via carbon-jndi and if needed a custom JNDI context provider can be plugged in to use with carbon-datasources as explained below.

## Using carbon datasources in non-OSGi environment

The datasources required for non-OSGi client application can be defined in a configuration file and naming convention of the configuration file is *-datasources.xml. Refer the sample configuration file as follows;
JNDI configuration cannot be added to a datasource because JNDI support is not there for non-OSGi applications at the moment.

If there is no jndi config defined explicitly in datasource definition, it will use the InMemoryInitialContextFactory to bind datasource objects to jndi context using carbon-jndi.

````xml
<datasources-configuration>
    <datasources>
        <datasource>
            <name>WSO2_ANALYTICS_DB</name>
            <description>The datasource used for registry and user manager</description>
            <definition type="RDBMS">
                <configuration>
                    <url>jdbc:h2:./target/database/ANALYTICS_DB1;DB_CLOSE_ON_EXIT=FALSE;LOCK_TIMEOUT=60000</url>
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

A custom JNDI context can be plugged in easily by adding its initial context factory class in `jndiConfig` as below using the JNDI Context.INITIAL_CONTEXT_FACTORY("java.naming.factory.initial") property.
````
<datasources-configuration>
    <datasources>
        <datasource>
            <name>WSO2_CARBON_DB</name>
            <description>The datasource used for registry and user manager</description>
            <jndiConfig>
                <name>jdbc/WSO2CarbonDB/test</name>
                <useJndiReference>true</useJndiReference>
                <environment>
                    <property name="java.naming.factory.initial">org.wso2.carbon.jndi.internal.InMemoryInitialContextFactory</property>
                </environment>
            </jndiConfig>
            <definition type="RDBMS">
                <configuration>
                    <url>jdbc:h2:./target/database/TEST_DB1;DB_CLOSE_ON_EXIT=FALSE;LOCK_TIMEOUT=60000</url>
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

The carbon-datasources bundle picks these xml configuration and build the data sources. The client application could retrieve datasources using the services provided by the carbon-datasources bundle. The JNDI support to retrieve the carbon datsources in non-OSGi environment will be added soon.
The datasources defined in configuration files are initialized by the `DataSourceManager`. They can be retrieved using `DataSourceService` using their names. The `DataSourceManagementService` can be used to perform managerial operations on datasources.

## Instructions to build and execute the application
1. Build the application
    `mvn clean install`
    
2. Run the application
    `java -jar target/datasources-spi-sample-<version>.jar`
   Ex: `java -jar target/datasources-spi-sample-1.1.0-SNAPSHOT.jar`