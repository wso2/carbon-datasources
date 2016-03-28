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
package org.wso2.carbon.datasource.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.wso2.carbon.datasource.core.exception.DataSourceException;
import org.wso2.carbon.kernel.utils.Utils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * Data Sources utility class.
 */
public class DataSourceUtils {

    private static Logger logger = LoggerFactory.getLogger(DataSourceUtils.class);

    private static final String[] CLASS_RETURN_TYPES = {"String", "Byte", "Character",
            "Short", "Integer", "Float", "Double", "Character", "Boolean"};

    private static final String XML_DECLARATION = "xml-declaration";

    private static final String DATASOURCES_DIRECTORY_NAME = "datasources";

    private static ThreadLocal<String> dataSourceId = new ThreadLocal<String>() {
        protected synchronized String initialValue() {
            return null;
        }
    };

    public static String getCurrentDataSourceId() {
        return dataSourceId.get();
    }

    public static boolean nullAllowEquals(Object lhs, Object rhs) {
        return lhs == null && rhs == null || !((lhs == null && rhs != null) || (lhs != null && rhs == null))
                && (lhs != null && lhs.equals(rhs));
    }

    public static String elementToString(Element element) {
        try {
            if (element == null) {
                                /* return an empty string because, the other way around works the same,
                                where if we give a empty string as the XML, we get a null element
                                from "stringToElement" */
                return "";
            }
            Document document = element.getOwnerDocument();
            DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
            LSSerializer serializer = domImplLS.createLSSerializer();
            //by default its true, so set it to false to get String without xml-declaration
            serializer.getDomConfig().setParameter(XML_DECLARATION, false);
            return serializer.writeToString(element);
        } catch (Exception e) {
            logger.error("Error while converting element to string: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns the conf directory path located in carbon.home.
     *
     * @return {@link Path}
     */
    public static Path getDataSourceConfigPath() {
        return Utils.getCarbonConfigHome().resolve(DATASOURCES_DIRECTORY_NAME);
    }


    public static Map<String, String> extractPrimitiveFieldNameValuePairs(Object object) throws DataSourceException {
        Map<String, String> nameValueMap = new HashMap<>();
        Method methods[] = object.getClass().getMethods();
        for (Method method : methods) {
            if (isMethodMatched(method)) {
                String fieldName = getFieldNameFromMethodName(method.getName());
                try {
                    if (method.invoke(object) != null) {
                        String result = method.invoke(object).toString();
                        nameValueMap.put(fieldName, result);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new DataSourceException("Error in retrieving " + fieldName + " value from the object :" +
                            object.getClass() + e.getMessage(), e);
                }
            }
        }
        return nameValueMap;
    }

    private static String getFieldNameFromMethodName(String name) throws DataSourceException {
        String prefixGet = "get";
        String prefixIs = "is";
        String firstLetter;

        if (name.startsWith(prefixGet)) {
            firstLetter = name.substring(3, 4);
            name = name.substring(4);
        } else if (name.startsWith(prefixIs)) {
            firstLetter = name.substring(2, 3);
            name = name.substring(3);
        } else {
            throw new DataSourceException("Error in retrieving attribute name from method : " + name);
        }
        firstLetter = firstLetter.toLowerCase(Locale.getDefault());
        return firstLetter.concat(name);
    }

    private static boolean isMethodMatched(Method method) {
        String returnType = method.getReturnType().getSimpleName();
        String methodName = method.getName();

        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (returnType.equals("void")) {
            return false;
        }
        if (!(methodName.startsWith("get") ||
                (methodName.startsWith("is") && (returnType.equals("Boolean") || returnType.equals("boolean"))))) {
            return false;
        }
        if (!(method.getReturnType().isPrimitive() ||
                Arrays.asList(CLASS_RETURN_TYPES).contains(returnType))) {
            return false;
        }
        return true;
    }

    /**
     * Generate the configuration bean by reading the yml file or string yaml content.
     *
     * @param configuration Object representing the configuration, either a File or String content.
     * @param clazz         T bean class
     * @param beanAccess    if this is true, yaml values will be injected to private fields of T bean class
     * @param <T>           Bean class which represent the yaml content
     * @param <U>           source of configuration, either a File or String content
     * @return an object of type T
     */
    public static <T, U> T loadConfiguration(U configuration, Class<T> clazz, boolean beanAccess) {
        String yamlString;
        if (configuration instanceof File) {
            try (InputStream inputStream = new FileInputStream((File) configuration);
                 Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                yamlString = scanner.useDelimiter("\\A").next();

            } catch (IOException e) {
                String errorMessage = "Failed populate CarbonConfiguration";
                throw new RuntimeException(errorMessage);
            }
        } else if (configuration instanceof String) {
            yamlString = configuration.toString();
        } else {
            throw new RuntimeException("Invalid configuration found!");
        }
        Yaml yaml = new Yaml();
        if (beanAccess) {
            yaml.setBeanAccess(BeanAccess.FIELD);
        }
        return yaml.loadAs(yamlString, clazz);
    }

    /**
     * Generate the yaml String out of key value pairs in the Map.
     *
     * @param map Source to create the yaml string
     * @return String
     */
    public static String mapToYamlString(Map map) {
        final StringBuilder yamlString = new StringBuilder();
        map.forEach((k, v) -> yamlString.append(k + ": " + v + System.lineSeparator()));
        return yamlString.toString();
    }
}
