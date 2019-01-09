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
package org.wso2.carbon.datasource.rdbms.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.utils.DataSourceUtils;

import java.util.Map;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * HikariDatasource wrapper class which wraps a set of utility methods.
 */
public class HikariRDBMSDataSource {
    private static final String JAVAX_DATASOURCE_CLASS = "javax.sql.DataSource";
    private static final String HIKARI_JNDI_FACTORY = "com.zaxxer.hikari.HikariJNDIFactory";
    private static final Logger log = LoggerFactory.getLogger(HikariRDBMSDataSource.class);
    private static final long retryIntervalInSec = 5;

    private HikariDataSource dataSource;
    private Reference dataSourceFactoryReference;
    private HikariConfig config;

    /**
     * Constructs HikariRDBMSDataSource object.
     *
     * @param config {@link HikariConfig}
     * @throws DataSourceException if there is an error.
     */
    public HikariRDBMSDataSource(HikariConfig config) throws DataSourceException {
        this.config = config;
        this.dataSourceFactoryReference = new Reference(JAVAX_DATASOURCE_CLASS, HIKARI_JNDI_FACTORY, null);
    }

    /**
     * Returns the HikariDataSource by building it based on the configuration.
     *
     * @return {@link HikariDataSource}
     */
    public HikariDataSource getDataSource() {
        while (dataSource == null) {
            try {
                dataSource = new HikariDataSource(config);
            } catch (Exception e) {
                log.error("Cannot connect to JDBC URL " + config.getJdbcUrl() + ". Failed due to " + e.getMessage() +
                        "retrying in " + retryIntervalInSec + " seconds", e);
                try {
                    Thread.sleep(retryIntervalInSec * 1000);
                } catch (InterruptedException e1) {
                    log.error(e1.getMessage(), e1);
                }
            }
        }
        return dataSource;
    }

    /**
     * Returns a {@link Reference} object representing the HikariDataSource.
     *
     * @return {@link Reference}
     * @throws DataSourceException if fieldName can't be retrieved from {@link HikariConfig} object.
     */
    public Reference getDataSourceFactoryReference() throws DataSourceException {
        Map<String, String> poolConfigMap =
                DataSourceUtils.extractPrimitiveFieldNameValuePairs(this.config);
        poolConfigMap.forEach((key, value) -> dataSourceFactoryReference.add(new StringRefAddr(key, value)));
        return dataSourceFactoryReference;
    }
}
