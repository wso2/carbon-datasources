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
package org.wso2.carbon.datasource.api;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.wso2.carbon.datasource.core.BaseTest;
import org.wso2.carbon.datasource.core.api.DataSourceService;
import org.wso2.carbon.datasource.core.beans.DataSourceDefinition;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.datasource.core.impl.DataSourceServiceImpl;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DataSourceServiceTest extends BaseTest {

    private DataSourceService dataSourceService;

    @BeforeSuite
    public void initialize() throws DataSourceException, MalformedURLException {
        super.init();
        dataSourceService = new DataSourceServiceImpl();
    }

    @Test
    public void getDataSourceTest() throws DataSourceException {
        Object dataSourceObject = dataSourceService.getDataSource("WSO2_CARBON_DB_2");
        Assert.assertNotNull(dataSourceObject, "Test datasource \"WSO2_CARBON_DB_2\" should not be null");
    }

    @Test(expectedExceptions = DataSourceException.class)
    public void getDataSourceFailTest() throws DataSourceException {
        dataSourceService.getDataSource(null);
    }

    @Test
    public void createDataSourceTest() throws DataSourceException {
        try {
            String dsXMLConfiguration = "<definition type=\"RDBMS\"><configuration><url>jdbc:h2:" +
                    "./database/WSO2_CARBON_DB;DB_CLOSE_ON_EXIT=FALSE;" +
                    "LOCK_TIMEOUT=60000</url><username>wso2carbon</username><password>wso2carbon</password" +
                    "><driverClassName>org.h2" +
                    ".Driver</driverClassName><maxActive>50</maxActive><maxWait>60000</maxWait><testOnBorrow>true" +
                    "</testOnBorrow><validationQuery>SELECT " +
                    "1</validationQuery><validationInterval>30000</validationInterval><defaultAutoCommit>false" +
                    "</defaultAutoCommit></configuration></definition>";
            DataSourceDefinition dataSourceDefinition = new DataSourceDefinition();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(dsXMLConfiguration.getBytes(StandardCharsets.UTF_8)));
            JAXBContext ctx = JAXBContext.newInstance(dataSourceDefinition.getClass());
            dataSourceDefinition = (DataSourceDefinition) ctx.createUnmarshaller().unmarshal(doc);

            Object dataSourceObj = dataSourceService.createDataSource(dataSourceDefinition);
            Assert.assertNotNull(dataSourceObj, "Failed to create datasource Object!");
        } catch (DataSourceException | ParserConfigurationException | SAXException | IOException | JAXBException e) {
            Assert.fail("Threw an exception while creating datasource Object");
        }
    }
}
