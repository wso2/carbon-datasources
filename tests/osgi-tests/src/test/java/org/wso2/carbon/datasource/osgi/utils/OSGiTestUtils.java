/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.datasource.osgi.utils;

import org.ops4j.pax.exam.Option;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.repositories;

/**
 * This class contains Utility methods to configure PAX-EXAM container.
 *
 * @since 5.0.0
 */
public class OSGiTestUtils {

    /**
     * Returns an array of default PAX-EXAM options.
     *
     * @return array of Options
     */
    public static Option[] getDefaultPaxOptions() {
        return options(
                repositories("http://maven.wso2.org/nexus/content/groups/wso2-public"),

                mavenBundle().artifactId("testng").groupId("org.testng").versionAsInProject(),
                mavenBundle().artifactId("jdbc-pool").groupId("org.apache.tomcat.wso2").versionAsInProject(),
                mavenBundle().artifactId("org.wso2.carbon.datasource.core").groupId("org.wso2.carbon.datasources")
                        .versionAsInProject(),

                mavenBundle().artifactId("snakeyaml").groupId("org.wso2.orbit.org.yaml")
                        .versionAsInProject(),
                mavenBundle().artifactId("org.wso2.carbon.core").groupId("org.wso2.carbon").versionAsInProject(),
                mavenBundle().artifactId("commons-io").groupId("commons-io.wso2").versionAsInProject(),
                mavenBundle().artifactId("HikariCP").groupId("com.zaxxer").versionAsInProject(),
                mavenBundle().artifactId("mysql-connector-java").groupId("mysql").versionAsInProject()
        );
    }

}
