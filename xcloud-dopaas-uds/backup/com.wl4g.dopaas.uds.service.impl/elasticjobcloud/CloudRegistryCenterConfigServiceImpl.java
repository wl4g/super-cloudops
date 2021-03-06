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

package com.wl4g.dopaas.uds.service.elasticjobcloud;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wl4g.dopaas.uds.service.elasticjobcloud.domain.GlobalConfiguration;
import com.wl4g.dopaas.uds.service.elasticjobcloud.domain.RegistryCenterConfig;
import com.wl4g.dopaas.uds.service.elasticjobcloud.domain.RegistryCenterConfigs;
import com.wl4g.dopaas.uds.service.elasticjobcloud.repository.ConfigurationsXmlRepository;
import com.wl4g.dopaas.uds.service.elasticjobcloud.repository.ConfigurationsXmlRepositoryImpl;

/**
 * Registry center configuration service implementation.
 */
@Service
public class CloudRegistryCenterConfigServiceImpl implements CloudRegistryCenterConfigService {

	private ConfigurationsXmlRepository configurationsXmlRepository = new ConfigurationsXmlRepositoryImpl();

	@Override
	public RegistryCenterConfigs loadAll() {
		return loadGlobal().getRegistryCenterConfigurations();
	}

	@Override
	public RegistryCenterConfig load(final String name) {
		GlobalConfiguration configs = loadGlobal();
		RegistryCenterConfig result = find(name, configs.getRegistryCenterConfigurations());
		setActivated(configs, result);
		return result;
	}

	@Override
	public RegistryCenterConfig find(final String name, final RegistryCenterConfigs configs) {
		for (RegistryCenterConfig each : configs.getRegistryCenterConfiguration()) {
			if (name.equals(each.getName())) {
				return each;
			}
		}
		return null;
	}

	private void setActivated(final GlobalConfiguration configs, final RegistryCenterConfig toBeConnectedConfig) {
		RegistryCenterConfig activatedConfig = findActivatedRegistryCenterConfiguration(configs);
		if (!toBeConnectedConfig.equals(activatedConfig)) {
			if (null != activatedConfig) {
				activatedConfig.setActivated(false);
			}
			toBeConnectedConfig.setActivated(true);
			configurationsXmlRepository.save(configs);
		}
	}

	@Override
	public Optional<RegistryCenterConfig> loadActivated() {
		return Optional.ofNullable(findActivatedRegistryCenterConfiguration(loadGlobal()));
	}

	private RegistryCenterConfig findActivatedRegistryCenterConfiguration(final GlobalConfiguration configs) {
		for (RegistryCenterConfig each : configs.getRegistryCenterConfigurations().getRegistryCenterConfiguration()) {
			if (each.isActivated()) {
				return each;
			}
		}
		return null;
	}

	@Override
	public boolean add(final RegistryCenterConfig config) {
		GlobalConfiguration configs = loadGlobal();
		boolean result = configs.getRegistryCenterConfigurations().getRegistryCenterConfiguration().add(config);
		if (result) {
			configurationsXmlRepository.save(configs);
		}
		return result;
	}

	@Override
	public void delete(final String name) {
		GlobalConfiguration configs = loadGlobal();
		RegistryCenterConfig toBeRemovedConfig = find(name, configs.getRegistryCenterConfigurations());
		if (null != toBeRemovedConfig) {
			configs.getRegistryCenterConfigurations().getRegistryCenterConfiguration().remove(toBeRemovedConfig);
			configurationsXmlRepository.save(configs);
		}
	}

	private GlobalConfiguration loadGlobal() {
		GlobalConfiguration result = configurationsXmlRepository.load();
		if (null == result.getRegistryCenterConfigurations()) {
			result.setRegistryCenterConfigurations(new RegistryCenterConfigs());
		}
		return result;
	}
}
