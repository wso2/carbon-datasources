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


    /**
     * Set the carbon home for execute tests.
     * Carbon home is set to /carbon-kernel/tests/osgi-tests/target/carbon-home
     */
    public static void copyResources() {

        Path configFilePath = Paths.get("tests", "osgi-tests", "src", "test", "resources", "conf",
                "datasources", "master-datasources.xml");
        Path configPathCopyLocation = Paths.get("tests", "osgi-tests", "target", "carbonHome", "conf", "datasources",
                "master-datasources.xml");
//        Path configFilePath = Paths.get("src", "test", "resources", "conf", "datasources", "master-datasources.xml");
//        Path configPathCopyLocation = Paths.get("target", "carbonHome", "conf", "datasources",
//                "master-datasources.xml");

        Utils.copy(configFilePath.toFile().getAbsolutePath(), configPathCopyLocation.toFile().getAbsolutePath());
    }

    public static void copy(String src, String dest) {
        createOutputFolderStructure(dest);

        try (FileInputStream inputStr = new FileInputStream(src);
             FileOutputStream outputStr = new FileOutputStream(dest)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStr.read(buf)) > 0) {
                outputStr.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {

        }
    }

    private static void createOutputFolderStructure(String destFileLocation) {
        File destFile = new File(destFileLocation);
        File parentFolder = destFile.getParentFile();
        parentFolder.mkdirs();
    }
}
