/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.spi.DataSourceReader;

/**
 * The HadoopDataSourceReader is responsible for reading the Hadoop connection parameters from the provided config
 * of type {@link HadoopDataSourceConstants#DATASOURCE_TYPE_HADOOP} found in the datasource config directory.
 */
@Component(
        name = "org.wso2.carbon.datasource.hadoop.HadoopDataSourceReader"
)
public class HadoopDataSourceReader implements DataSourceReader {

    @Activate
    protected void activate(BundleContext bundleContext) {
    }

    @Deactivate
    protected void deactivate(BundleContext bundleContext) {
    }

    @Override
    public String getType() {
        return HadoopDataSourceConstants.DATASOURCE_TYPE_HADOOP;
    }

    @Override
    public Object createDataSource(String xmlConfiguration, boolean isDataSourceFactoryReference)
            throws DataSourceException {
        return HadoopDataSourceUtil.createConfigFromFile(xmlConfiguration);
    }
}
