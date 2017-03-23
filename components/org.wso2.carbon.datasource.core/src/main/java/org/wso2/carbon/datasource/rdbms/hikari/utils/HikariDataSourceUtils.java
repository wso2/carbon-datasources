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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.rdbms.hikari.HikariConstants;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Encapsulates a set of utility methods for HikariDataSource.
 */
public class HikariDataSourceUtils {

    private static Logger log = LoggerFactory.getLogger(HikariDataSourceUtils.class);
    /**
     * Generate the configuration bean by reading the xml configuration.
     *
     * @param configuration Object
     * @return {@code HikariConfig}
     * @throws DataSourceException if there is an error when loading Hikari configuration.
     */
    public static HikariConfig buildConfiguration(Object configuration) throws DataSourceException {
        if (configuration == null) {
            throw new DataSourceException("datasource configurations are not provided. datasource object will not be " +
                "initialized!");
        }
        if (configuration instanceof Map) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setConnectionTimeout(HikariConstants.CONNECTION_TIME_OUT);
            hikariConfig.setIdleTimeout(HikariConstants.IDLE_TIME_OUT);
            hikariConfig.setMaxLifetime(HikariConstants.MAX_LIFE_TIME);
            hikariConfig.setMinimumIdle(HikariConstants.MINIMUM_IDLE_SIZE);
            hikariConfig.setMinimumIdle(HikariConstants.MAXIMUM_POOL_SIZE);
            hikariConfig.setAutoCommit(HikariConstants.AUTO_COMMIT);

            Map<String, ? extends Object> configMap = (Map<String, ? extends Object>) configuration;
            // Loop through the property name/value pairs to be set
            Class hikariClass = hikariConfig.getClass();
            for (final Map.Entry<String, ? extends Object> entry : configMap.entrySet()) {
                // Identify the property name and value(s) to be assigned
                final String name = entry.getKey();
                if (name == null) {
                    continue;
                }
                try {
                    //get matching to the config key. key name should match with the field name to apply the value.
                    Field beanField = hikariClass.getDeclaredField(name);
                    if (!beanField.isAccessible()) {
                        AccessController
                            .doPrivileged((PrivilegedAction<Object>) () -> {
                                beanField.setAccessible(true);
                                return null;
                            });
                    }

                    if (entry.getValue() instanceof List) {
                        Class fieldTypeClass = beanField.getType();
                        if (!fieldTypeClass.isAssignableFrom(Properties.class)) {
                            continue;
                        }
                        Properties properties = new Properties();
                        for (Object property : (ArrayList) entry.getValue()) {
                            if (property instanceof Map) {
                                Map<String, String> propertyMap = (Map<String, String>) property;
                                properties.setProperty(propertyMap.get("name"), propertyMap.get("value"));
                            }

                        }
                        beanField.set(hikariConfig, properties);
                    } else {
                        beanField.set(hikariConfig, entry.getValue());
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error("Error while reading the datasource configuration key: " + name, e);
                }
            }
            return hikariConfig;
        } else {
            throw new DataSourceException("Error while reading the datasource configuration, " +
                "couldn't create HikariConfig object");
        }
    }
}
