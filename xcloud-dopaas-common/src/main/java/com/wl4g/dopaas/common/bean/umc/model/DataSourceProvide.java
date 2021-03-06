/*
 * Copyright 2017 ~ 2050 the original author or authors <Wanglsir@gmail.com, 983708408@qq.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wl4g.dopaas.common.bean.umc.model;

/**
 * @author vjay
 * @date 2020-03-30 17:01:00
 */
public enum DataSourceProvide {
    /**
     * Mysql
     */
    MYSQL,

    /**
     * Oracle
     */
    ORACLE
    ;


    public static DataSourceProvide parse(String dataSourceProvider) {
        for (DataSourceProvide cacl : DataSourceProvide.values()) {
            if (cacl.toString().equalsIgnoreCase(dataSourceProvider) || dataSourceProvider.equalsIgnoreCase(cacl.name())) {
                return cacl;
            }
        }
        throw new IllegalArgumentException("Unable to parse the provided acl " + dataSourceProvider);
    }

    public static String[] dataSourceProvides() {
        String[] result = new String[DataSourceProvide.values().length];
        for (int i = 0; i < DataSourceProvide.values().length; i++) {
            result[i] = DataSourceProvide.values()[i].toString();
        }
        return result;
    }

}