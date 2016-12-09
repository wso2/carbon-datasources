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
package org.wso2.carbon.datasource.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.wso2.carbon.datasource.core.beans.CarbonDataSource;
import org.wso2.carbon.datasource.core.beans.DataSourceDefinition;
import org.wso2.carbon.datasource.core.beans.DataSourceMetadata;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.spi.DataSourceReader;
import org.wso2.carbon.datasource.utils.DataSourceUtils;

/**
 * DataSourceBuilder is a util class responsible to build data source by passing the data source configuration to the
 * relevant DataSourceReader object.
 */
public class DataSourceBuilder {

    private static Logger logger = LoggerFactory.getLogger(DataSourceBuilder.class);

    /**
     * Build a {@code CarbonDataSource} object from the given {@code DataSourceMetaInfo} object.
     *
     * @param dataSourceMetadata {@code DataSourceMetaInfo}
     * @param dataSourceReader {@link DataSourceReader}
     * @return {@link CarbonDataSource}
     * @throws DataSourceException if CarbonDataSource object can't be built from given DataSourceMetaInfo object.
     */
    public static CarbonDataSource buildCarbonDataSource(DataSourceMetadata dataSourceMetadata, DataSourceReader
            dataSourceReader) throws DataSourceException {
        Object dataSource = buildDataSourceObject(dataSourceMetadata, false, dataSourceReader);
        return new CarbonDataSource(dataSourceMetadata, dataSource);
    }

    /**
     * Creates the data source object by getting the appropriate DataSourceReader. The created object would be either
     * a {@link javax.sql.DataSource} or {@code Reference} if {@code isUseJndiReference} param is true.
     *
     * @param dataSourceMetadata     {@code DataSourceMetaInfo}
     * @param isUseDataSourceFactory {@code boolean}
     * @param dataSourceReader       {@code DataSourceReader} which is used to created the DataSource Object
     * @return {@code Object}
     * @throws DataSourceException if data source object can't be created using given reader.
     */
    public static Object buildDataSourceObject(DataSourceMetadata dataSourceMetadata, boolean isUseDataSourceFactory,
                                               DataSourceReader dataSourceReader) throws DataSourceException {
        return buildDataSourceObject(dataSourceReader, isUseDataSourceFactory, dataSourceMetadata.getDefinition());
    }

    /**
     * Creates the data source object by getting the appropriate DataSourceReader. The created object would be either
     * a {@link javax.sql.DataSource} or {@code Reference} if {@code isUseJndiReference} param is true.
     *
     * @param dataSourceDefinition   {@code DataSourceDefinition} that is converted to DataSource object
     * @param isUseDataSourceFactory if true return the DataSourceReference else return the DataSource
     * @param dataSourceReader       {@code DataSourceReader} which is used to created the DataSource Object
     * @return {@code Object}
     * @throws DataSourceException if data source object can't be created using given data source reader.
     */
    public static Object buildDataSourceObject(DataSourceReader dataSourceReader, boolean isUseDataSourceFactory,
                                               DataSourceDefinition dataSourceDefinition) throws DataSourceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Generating the Datasource object from \"" + dataSourceDefinition.getType() + "\" type " +
                    "reader.");
        }
        Element configurationXmlDefinition = (Element) dataSourceDefinition.getDsXMLConfiguration();
        return dataSourceReader.createDataSource(DataSourceUtils.elementToString(configurationXmlDefinition),
                isUseDataSourceFactory);
    }
}
