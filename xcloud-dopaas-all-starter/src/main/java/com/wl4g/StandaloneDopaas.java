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
package com.wl4g;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wl4g.component.core.web.mapping.annotation.EnableSmartRequestMapping;
import com.wl4g.component.data.annotation.EnableComponentDataConfiguration;
import com.wl4g.iam.client.annotation.EnableIamClient;
import com.wl4g.shell.springboot.annotation.EnableShellServer;

/**
 * {@link StandaloneDopaas} </br>
 * </br>
 * 
 * for example: It should at least includes( "com.wl4g.iam.web",
 * "com.wl4g.dopaas.uci.web", "com.wl4g.dopaas.lcdp.web",
 * "com.wl4g.dopaas.lcdp.codegen.web", "com.wl4g.dopaas.udm.web",
 * "com.wl4g.dopaas.cmdb.web", "com.wl4g.dopaas.urm.web",
 * "com.wl4g.dopaas.umc.web" )
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2021-01-25
 * @sine v1.0
 * @see
 */
// Only this package is considered for mapping.
@EnableSmartRequestMapping({ "com.wl4g.iam.web", "com.wl4g.dopaas.**.web" })
@EnableIamClient
@EnableShellServer
@EnableComponentDataConfiguration({ "com.wl4g.iam.data", "com.wl4g.dopaas.**.data" })
@SpringBootApplication
public class StandaloneDopaas {

	public static void main(String[] args) {
		SpringApplication.run(StandaloneDopaas.class, args);
	}

}