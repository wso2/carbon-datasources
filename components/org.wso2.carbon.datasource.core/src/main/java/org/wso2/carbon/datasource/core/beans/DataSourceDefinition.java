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
package org.wso2.carbon.datasource.core.beans;

import org.w3c.dom.Element;
import org.wso2.carbon.datasource.utils.DataSourceUtils;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class holding the data source definition.
 */
@XmlRootElement(name = "definition")
public class DataSourceDefinition {

    private String type;

    private Object dsXMLConfiguration;

    @XmlAttribute(name = "type", required = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAnyElement
    public Object getDsXMLConfiguration() {
        return dsXMLConfiguration;
    }

    public void setDsXMLConfiguration(Object dsXMLConfiguration) {
        this.dsXMLConfiguration = dsXMLConfiguration;
    }

    public boolean equals(Object rhs) {
        if (!(rhs instanceof DataSourceDefinition)) {
            return false;
        }
        DataSourceDefinition dsDef = (DataSourceDefinition) rhs;
        if (!DataSourceUtils.nullAllowEquals(dsDef.getType(), this.getType())) {
            return false;
        }
        return DataSourceUtils.nullAllowEquals(DataSourceUtils.elementToString(
                (Element) dsDef.getDsXMLConfiguration()),
                DataSourceUtils.elementToString((Element) this.getDsXMLConfiguration()));
    }

    @Override
    public int hashCode() {
        assert false : "hashCode() not implemented";
        return -1;
    }
}
