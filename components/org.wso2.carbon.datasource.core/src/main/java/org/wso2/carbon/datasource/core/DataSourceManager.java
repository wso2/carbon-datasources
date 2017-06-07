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
import org.wso2.carbon.config.ConfigurationException;
import org.wso2.carbon.config.provider.ConfigProvider;
import org.wso2.carbon.datasource.core.beans.CarbonDataSource;
import org.wso2.carbon.datasource.core.beans.DataSourceDefinition;
import org.wso2.carbon.datasource.core.beans.DataSourceMetadata;
import org.wso2.carbon.datasource.core.beans.DataSourcesConfiguration;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.spi.DataSourceReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import javax.naming.NamingException;

/**
 * This class contains the functionality in managing the data sources.
 */
public class DataSourceManager {

    private static Logger logger = LoggerFactory.getLogger(DataSourceManager.class);
    private static DataSourceManager instance = new DataSourceManager();
    private static final String WSO2_DATASOURCES_NAMESPACE = "wso2.datasources";
    private DataSourceRepository dataSourceRepository;
    private Map<String, DataSourceReader> dataSourceReaders;
    private boolean initialized = false;

    /**
     * Private constructor for DataSourceManager class. This is a singleton class, thus the constructor is private.
     */
    private DataSourceManager() {
        this.dataSourceReaders = new HashMap<>();
        this.dataSourceRepository = new DataSourceRepository();
    }

    /**
     * Returns the singleton instance of DataSourceManager.
     *
     * @return DataSourceManager
     */
    public static DataSourceManager getInstance() {
        return instance;
    }

    /**
     * Returns the DataSourceRepository object, which encapsulates all the defined data sources.
     *
     * @return DataSourceRepository
     */
    public DataSourceRepository getDataSourceRepository() {
        return dataSourceRepository;
    }

    /**
     * Returns the types of data source readers registered in the system.
     *
     * @return {@code List<String>}
     */
    public List<String> getDataSourceTypes() {
        return new ArrayList<>(dataSourceReaders.keySet());
    }

    /**
     * Returns a DataSourceReader for the given DataSourceReader type.
     *
     * @param dataSourceType String
     * @return {@code DataSourceReader}
     * @throws DataSourceException if the particular type {@link DataSourceReader} is not available.
     */
    public DataSourceReader getDataSourceReader(String dataSourceType) throws DataSourceException {
        DataSourceReader reader = dataSourceReaders.get(dataSourceType);
        if (reader == null) {
            throw new DataSourceException("No reader found for type: " + dataSourceType);
        }
        return reader;
    }

    /**
     * Initializes data sources configured in deployment.yaml.
     *
     * @param configProvider configProvider service object
     * @throws DataSourceException if an error occurred while initializing the data source.
     */
    public void initDataSources(ConfigProvider configProvider)
            throws DataSourceException {
        loadDataSourceProviders();
        initDataSources(configProvider, dataSourceReaders);
    }

    /**
     * Initializes data sources configured in deployment.yaml.
     *
     * @param configProvider configProvider service object
     * @param dataSourceReaders {@code Map<String, DataSourceReader>}
     * @throws DataSourceException if an error occurred while initializing the data source.
     */
    public void initDataSources(ConfigProvider configProvider, Map<String, DataSourceReader> dataSourceReaders)
            throws DataSourceException {
        this.dataSourceReaders = dataSourceReaders;
        if (initialized) {
            logger.debug("Data sources are already initialized.");
            return;
        }
        logger.debug("Initializing the data sources.");

        if (dataSourceReaders.isEmpty()) {
            throw new RuntimeException("No data source readers found. Data sources will not be initialized!");
        }

        DataSourcesConfiguration dataSourceConfiguration = null;
        try {
            // check whether datasource is configured in deployment.yaml. create datasource only if configuration
            // exists in deployment.yaml
            if (configProvider.getConfigurationObject(WSO2_DATASOURCES_NAMESPACE) != null) {
                dataSourceConfiguration = configProvider.getConfigurationObject(DataSourcesConfiguration.class);
                if (dataSourceConfiguration.getDataSources() == null && dataSourceConfiguration.getDataSources()
                    .isEmpty()) {
                    throw new DataSourceException("Configuration doesn't specify any datasource configurations");
                }
            }
        } catch (ConfigurationException e) {
            throw new DataSourceException("Error while reading datasource configuration from file", e);
        }

        if (dataSourceConfiguration != null) {
            for (DataSourceMetadata dsmInfo : dataSourceConfiguration.getDataSources()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating datasource object| datasource name: " + dsmInfo.getName());
                }
                if (dsmInfo != null && dsmInfo.getDefinition() != null) {
                    try {
                        DataSourceReader dataSourceReader = getDataSourceReader(dsmInfo.getDefinition().getType());
                        CarbonDataSource carbonDataSource = DataSourceBuilder.buildCarbonDataSource(dsmInfo,
                            dataSourceReader);
                        dataSourceRepository.addDataSource(carbonDataSource);
                        DataSourceJndiManager.register(carbonDataSource, dataSourceReader);
                    } catch (NamingException | DataSourceException e) {
                        throw new DataSourceException("Error while initializing the datasource: " + dsmInfo.getName()
                            , e);
                    }
                }
            }
        }
        initialized = true;
    }

    /**
     * Load {@code DataSourceReader} implementations from the class path. This make use of {@link ServiceLoader}.
     */
    private void loadDataSourceProviders() {
        if (dataSourceReaders.size() == 0) {
            ServiceLoader<DataSourceReader> dsReaderLoader = ServiceLoader.load(DataSourceReader.class);
            dsReaderLoader.forEach(reader -> dataSourceReaders.put(reader.getType(), reader));
        }
    }

    /**
     * Creates datasource Object from the data.
     * @param dataSourceDefinition {@code DataSourceDefinition} that is converted to DataSource object
     * @return {@code Object}
     * @throws DataSourceException if defined type of data source object can't be created.
     */
    public Object createDataSource(DataSourceDefinition dataSourceDefinition) throws DataSourceException {

        if (logger.isDebugEnabled()) {
            logger.debug("Creating datasoure object of type: " + dataSourceDefinition.getType());
        }
        try {
            DataSourceReader dataSourceReader = getDataSourceReader(dataSourceDefinition.getType());
            return DataSourceBuilder.buildDataSourceObject(dataSourceReader, false, dataSourceDefinition);
        } catch (DataSourceException e) {
            throw new DataSourceException("Error creating data source object of type: " + dataSourceDefinition.
                    getType() + " - " + e.getMessage(), e);
        }
    }
}
