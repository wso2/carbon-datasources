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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/***
 * Bean class a single datasource configuration specification, which contains an array of configuration properties.
 */
@XmlRootElement(name = "configuration")
public class HadoopDataSourceConfiguration {

    private List<HadoopDataSourceProperty> properties;

    @XmlElement(name = "property")
    public List<HadoopDataSourceProperty> getProperties() {
        return (properties);
    }

    public void setProperties(List<HadoopDataSourceProperty> properties) {
        this.properties = properties;
    }

}
