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
import org.wso2.carbon.datasource.core.exception.DataSourceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Data Sources utility class.
 */
public class DataSourceUtils {

    private static Logger logger = LoggerFactory.getLogger(DataSourceUtils.class);

    private static final String[] CLASS_RETURN_TYPES = {"String", "Byte", "Character",
            "Short", "Integer", "Float", "Double", "Character", "Boolean"};

    public static boolean nullAllowEquals(Object lhs, Object rhs) {
        return lhs == null && rhs == null || !((lhs == null && rhs != null) || (lhs != null && rhs == null))
                && (lhs != null && lhs.equals(rhs));
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

}
