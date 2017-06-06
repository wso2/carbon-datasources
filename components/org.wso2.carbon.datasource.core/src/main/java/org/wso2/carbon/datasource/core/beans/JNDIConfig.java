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

import org.wso2.carbon.config.annotation.Configuration;
import org.wso2.carbon.datasource.utils.DataSourceUtils;

import java.util.Hashtable;

/**
 * This class represents properties related to JNDI mapping of a data source.
 */
@Configuration(description = "JNDI mapping of a data source")
public class JNDIConfig {

    private String name;

    private EnvEntry[] environment;

    private boolean useJndiReference;

    public void setName(String name) {
        this.name = name;
    }

    public void setEnvironment(EnvEntry[] environment) {
        this.environment = new EnvEntry[environment.length];
        for (int i = 0; i < environment.length; i++) {
            this.environment[i] = environment[i].copy();
        }
    }

    public String getName() {
        return name;
    }

    public EnvEntry[] getEnvironment() {
        if (environment == null) {
            return new EnvEntry[0];
        }
        EnvEntry[] tempEnvironment = new EnvEntry[environment.length];
        for (int i = 0; i < environment.length; i++) {
            tempEnvironment[i] = environment[i].copy();
        }
        return tempEnvironment;
    }

    public boolean isUseJndiReference() {
        return useJndiReference;
    }

    public void setUseJndiReference(boolean useJndiReference) {
        this.useJndiReference = useJndiReference;
    }

    public Hashtable<String, String> extractHashtableEnv() {
        Hashtable<String, String> env = new Hashtable<>();
        for (EnvEntry entry : this.getEnvironment()) {
            env.put(entry.getName(), entry.getValue());
        }
        return env;
    }

    public JNDIConfig copy() {
        JNDIConfig result = new JNDIConfig();
        result.setName(this.getName());
        result.setUseJndiReference(this.isUseJndiReference());
        EnvEntry[] envEntries = null;
        EnvEntry[] origEntries = this.getEnvironment();
        envEntries = new EnvEntry[origEntries.length];
        for (int i = 0; i < origEntries.length; i++) {
            envEntries[i] = new EnvEntry();
            envEntries[i].setName(origEntries[i].getName());
            envEntries[i].setValue(origEntries[i].getValue());
        }
        result.setEnvironment(envEntries);
        return result;
    }

    @Override
    public boolean equals(Object rhs) {
        if (!(rhs instanceof JNDIConfig)) {
            return false;
        }
        JNDIConfig jc = (JNDIConfig) rhs;
        if (!DataSourceUtils.nullAllowEquals(jc.getName(), this.getName())) {
            return false;
        }
        if (!DataSourceUtils.nullAllowEquals(jc.extractHashtableEnv(), this.extractHashtableEnv())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        assert false : "hashCode() not implemented";
        return -1;
    }

    /**
     * Bean class to hold environment properties.
     */
    public static class EnvEntry {

        private String name;

        private boolean encrypted = true;

        private String value;

        public boolean isEncrypted() {
            return encrypted;
        }

        public void setEncrypted(boolean encrypted) {
            this.encrypted = encrypted;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public EnvEntry copy() {
            EnvEntry temp = new EnvEntry();
            temp.setEncrypted(this.isEncrypted());
            temp.setName(this.getName());
            temp.setValue(this.getValue());
            return temp;
        }
    }
}
