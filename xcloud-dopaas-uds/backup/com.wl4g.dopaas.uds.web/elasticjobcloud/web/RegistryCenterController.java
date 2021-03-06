/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wl4g.dopaas.uds.elasticjobcloud.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shardingsphere.elasticjob.reg.exception.RegException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wl4g.component.common.web.rest.RespBase;
import com.wl4g.dopaas.uds.service.elasticjobcloud.CloudRegistryCenterConfigService;
import com.wl4g.dopaas.uds.service.elasticjobcloud.domain.RegistryCenterConfig;

/**
 * Registry center RESTful API.
 */
@RestController
@RequestMapping("/api/registry-center")
public class RegistryCenterController {

	public static final String REG_CENTER_CONFIG_KEY = "reg_center_config_key";

	private CloudRegistryCenterConfigService regCenterService;

	public RegistryCenterController() {
	}

	@Autowired
	public RegistryCenterController(final CloudRegistryCenterConfigService regCenterService) {
		this.regCenterService = regCenterService;
	}

	/**
	 * Judge whether registry center is activated.
	 *
	 * @return registry center is activated or not
	 */
	@GetMapping("/activated")
	public RespBase<RegistryCenterConfig> activated() {
		return RespBase.<RegistryCenterConfig> create().withData(regCenterService.loadActivated().get());
	}

	/**
	 * Load configuration from registry center.
	 *
	 * @param request
	 *            HTTP request
	 * @return registry center configurations
	 */
	@GetMapping("/load")
	public RespBase<Collection<RegistryCenterConfig>> load(final HttpServletRequest request) {
		RespBase<Collection<RegistryCenterConfig>> resp = RespBase.create();
		regCenterService.loadActivated()
				.ifPresent(regCenterConfig -> setRegistryCenterNameToSession(regCenterConfig, request.getSession()));
		return resp.withData(regCenterService.loadAll().getRegistryCenterConfiguration());
	}

	/**
	 * Add registry center.
	 *
	 * @param config
	 *            registry center configuration
	 * @return success to add or not
	 */
	@PostMapping("/add")
	public RespBase<Boolean> add(@RequestBody final RegistryCenterConfig config) {
		return RespBase.<Boolean> create().withData(regCenterService.add(config));
	}

	/**
	 * Delete registry center.
	 *
	 * @param config
	 *            registry center configuration
	 */
	@DeleteMapping
	public RespBase<?> delete(@RequestBody final RegistryCenterConfig config) {
		regCenterService.delete(config.getName());
		return RespBase.create();
	}

	/**
	 * Connect to registry center.
	 *
	 * @param config
	 *            config of registry center
	 * @param request
	 *            HTTP request
	 * @return connected or not
	 */
	@PostMapping(value = "/connect")
	public RespBase<Boolean> connect(@RequestBody final RegistryCenterConfig config, final HttpServletRequest request) {
		boolean isConnected = setRegistryCenterNameToSession(regCenterService.find(config.getName(), regCenterService.loadAll()),
				request.getSession());
		if (isConnected) {
			regCenterService.load(config.getName());
		}
		return RespBase.<Boolean> create().withData(isConnected);
	}

	private boolean setRegistryCenterNameToSession(final RegistryCenterConfig regCenterConfig, final HttpSession session) {
		session.setAttribute(REG_CENTER_CONFIG_KEY, regCenterConfig);
		try {
			RegistryCenterFactory.createCoordinatorRegistryCenter(regCenterConfig.getZkAddressList(),
					regCenterConfig.getNamespace(), regCenterConfig.getDigest());
			SessionRegistryCenterFactory
					.setRegistryCenterConfiguration((RegistryCenterConfig) session.getAttribute(REG_CENTER_CONFIG_KEY));
		} catch (final RegException ex) {
			return false;
		}
		return true;
	}

}
