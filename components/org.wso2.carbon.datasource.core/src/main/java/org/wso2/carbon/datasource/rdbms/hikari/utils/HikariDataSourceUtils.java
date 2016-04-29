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
package org.wso2.carbon.datasource.rdbms.hikari.utils;

import com.zaxxer.hikari.HikariConfig;
import org.wso2.carbon.datasource.rdbms.hikari.HikariConfiguration;
import org.wso2.carbon.datasource.utils.DataSourceUtils;

/**
 * Encapsulates a set of utility methods for HikariDataSource.
 */
public class HikariDataSourceUtils {

    /**
     * Generate the configuration bean by reading the xml configuration.
     *
     * @param xmlConfiguration String
     * @return {@code HikariConfig}
     */
    public static HikariConfig buildConfiguration(String xmlConfiguration) {
        HikariConfiguration configuration = DataSourceUtils
                .loadConfiguration(xmlConfiguration, HikariConfiguration.class, false);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(configuration.getUrl());
        config.setUsername(configuration.getUsername());
        config.setPassword(configuration.getPassword());
        config.setDriverClassName(configuration.getDriverClassName());
        config.setConnectionTimeout(configuration.getConnectionTimeout());
        config.setIdleTimeout(configuration.getIdleTimeout());
        config.setMaxLifetime(configuration.getMaxLifetime());
        config.setMaximumPoolSize(configuration.getMaximumPoolSize());
        config.setMinimumIdle(configuration.getMinimumIdle());
        return config;
    }
}
