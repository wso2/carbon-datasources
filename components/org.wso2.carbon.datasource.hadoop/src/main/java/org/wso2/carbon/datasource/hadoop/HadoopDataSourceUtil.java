/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.datasource.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.utils.DataSourceUtils;

import java.util.List;

/**
 * Contains the set of utility methods needed for {@link HadoopDataSourceReader}
 */
public class HadoopDataSourceUtil {

    /**
     * Creates an Apache Hadoop Configuration object from the textual configuration
     *
     * @param xmlConfiguration the text-based configuration read from file
     * @return {@code {@link Configuration}}
     * @throws DataSourceException when there is
     */
    public static Configuration createConfigFromFile(String xmlConfiguration) throws DataSourceException {
        try {
            HadoopDataSourceConfiguration configuration = DataSourceUtils
                    .loadJAXBConfiguration(xmlConfiguration, HadoopDataSourceConfiguration.class);

            Configuration config = new Configuration();
            List<HadoopDataSourceProperty> properties = configuration.getProperties();

            for (HadoopDataSourceProperty configEntry : properties) {
                if (!("".equals(configEntry.getPropertyName())) && !("".equals(configEntry.getPropertyValue()))) {
                    config.set(configEntry.getPropertyName(), configEntry.getPropertyValue());
                }
            }
            return config;
        } catch (DataSourceException e) {
            throw new DataSourceException("Error in loading Hikari configuration: " + e.getMessage(), e);
        }
    }
}
