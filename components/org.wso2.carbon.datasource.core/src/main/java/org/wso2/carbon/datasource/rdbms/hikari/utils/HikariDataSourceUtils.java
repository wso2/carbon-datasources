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
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * Encapsulates a set of utility methods for HikariDataSource.
 */
public class HikariDataSourceUtils {

    /**
     * Generate the configuration bean by reading the xml configuration.
     *
     * @param configuration String
     * @return {@code HikariConfig}
     * @throws DataSourceException if there is an error when loading Hikari configuration.
     */
    public static HikariConfig buildConfiguration(String configuration) throws DataSourceException {
        Yaml yaml = new Yaml(new CustomClassLoaderConstructor(HikariConfig.class, HikariConfig.class.getClassLoader()));
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml.loadAs(configuration, HikariConfig.class);
    }
}
