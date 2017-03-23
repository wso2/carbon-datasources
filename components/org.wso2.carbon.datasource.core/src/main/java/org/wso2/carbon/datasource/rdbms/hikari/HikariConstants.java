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
package org.wso2.carbon.datasource.rdbms.hikari;

/**
 * Class holding the default values for Hikari configuration.
 */
public class HikariConstants {

    /**
     * Private constructor.
     */
    private HikariConstants() {
    }

    public static final long CONNECTION_TIME_OUT = 30000;

    public static final long IDLE_TIME_OUT = 600000;

    public static final long MAX_LIFE_TIME = 1800000;

    public static final int MAXIMUM_POOL_SIZE = 50;

    public static final int MINIMUM_IDLE_SIZE = 10;

    /**
     * Default value for auto commit flag (for a connection).
     */
    public static final boolean AUTO_COMMIT = false;

}
