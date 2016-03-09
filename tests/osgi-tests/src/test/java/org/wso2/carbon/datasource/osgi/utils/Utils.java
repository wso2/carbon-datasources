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
package org.wso2.carbon.datasource.osgi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.kernel.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Utility class for unit tests. All the utility methods required for test classes should reside here.
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(OSGiTestUtils.class);

    public static void setEnv() {
        setCarbonHome();
        setStartupTime();
        copyCarbonYAML();
        copyLog4jXMLFile();
        copyLaunchPropertiesFile();
        copyDSConfigFile();
        copyDeploymentFile();
    }

    private static void setCarbonHome() {
        String currentDir = Paths.get("").toAbsolutePath().toString();
        Path carbonHome = Paths.get(currentDir, "target", "carbon-home");
        System.setProperty("carbon.home", carbonHome.toString());
    }

    /**
     * Set the startup time to calculate the server startup time.
     */
    private static void setStartupTime() {
        if (System.getProperty(Constants.START_TIME) == null) {
            System.setProperty(Constants.START_TIME, System.currentTimeMillis() + "");
        }
    }

    /**
     * Replace the existing carbon.yml file with populated carbon.yml file.
     */
    private static void copyCarbonYAML() {
        Path carbonYmlFilePath;

        String basedir = System.getProperty("basedir");
        if (basedir == null) {
            basedir = Paths.get(".").toString();
        }
        try {
            carbonYmlFilePath = Paths.get(basedir, "src", "test", "resources", "conf", "carbon.yml");
            copy(carbonYmlFilePath.toString(), Paths.get(System.getProperty("carbon.home"), "conf",
                    "carbon.yml").toString());
        } catch (IOException e) {
            logger.error("Unable to copy the carbon.yml file", e);
        }
    }

    /**
     * Replace the existing carbon.yml file with populated carbon.yml file.
     */
    private static void copyLog4jXMLFile() {
        Path carbonYmlFilePath;

        String basedir = System.getProperty("basedir");
        if (basedir == null) {
            basedir = Paths.get(".").toString();
        }
        try {
            carbonYmlFilePath = Paths.get(basedir, "src", "test", "resources", "conf", "log4j2.xml");
            copy(carbonYmlFilePath.toString(), Paths.get(System.getProperty("carbon.home"), "conf",
                    "log4j2.xml").toString());
        } catch (IOException e) {
            logger.error("Unable to copy the carbon.yml file", e);
        }
    }

    /**
     * Replace the existing carbon.yml file with populated carbon.yml file.
     */
    private static void copyLaunchPropertiesFile() {
        Path carbonYmlFilePath;

        String basedir = System.getProperty("basedir");
        if (basedir == null) {
            basedir = Paths.get(".").toString();
        }
        try {
            carbonYmlFilePath = Paths.get(basedir, "src", "test", "resources", "conf", "osgi", "launch.properties");
            copy(carbonYmlFilePath.toString(), Paths.get(System.getProperty("carbon.home"), "conf",
                    "osgi", "launch.properties").toString());
        } catch (IOException e) {
            logger.error("Unable to copy the carbon.yml file", e);
        }
    }

    /**
     * Replace the existing carbon.yml file with populated carbon.yml file.
     */
    private static void copyDSConfigFile() {
        Path carbonYmlFilePath;

        String basedir = System.getProperty("basedir");
        if (basedir == null) {
            basedir = Paths.get(".").toString();
        }
        try {
            carbonYmlFilePath = Paths.get(basedir, "src", "test", "resources", "conf", "datasources",
                    "master-datasources.xml");
            copy(carbonYmlFilePath.toString(), Paths.get(System.getProperty("carbon.home"), "conf", "datasources",
                    "master-datasources.xml").toString());
        } catch (IOException e) {
            logger.error("Unable to copy the carbon.yml file", e);
        }
    }

    /**
     * Replace the existing carbon.yml file with populated carbon.yml file.
     */
    private static void copyDeploymentFile() {
        Path carbonYmlFilePath;

        String basedir = System.getProperty("basedir");
        if (basedir == null) {
            basedir = Paths.get(".").toString();
        }
        try {
            carbonYmlFilePath = Paths.get(basedir, "src", "test", "resources", "deployment",
                    "README.txt");
            copy(carbonYmlFilePath.toString(), Paths.get(System.getProperty("carbon.home"), "deployment",
                    "README.txt").toString());
        } catch (IOException e) {
            logger.error("Unable to copy the carbon.yml file", e);
        }
    }

    public static void copy(String src, String dest) throws IOException {
        createOutputFolderStructure(dest);
        try (FileInputStream inputStr = new FileInputStream(src);
             FileOutputStream outputStr = new FileOutputStream(dest)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStr.read(buf)) > 0) {
                outputStr.write(buf, 0, bytesRead);
            }
        }
    }

    private static void createOutputFolderStructure(String destFileLocation) {
        File destFile = new File(destFileLocation);
        File parentFolder = destFile.getParentFile();
        parentFolder.mkdirs();
    }
}
