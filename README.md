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
