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

package com.wl4g.dopaas.uds.service.elasticjoblite;

import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl4g.dopaas.uds.service.elasticjoblite.EventTraceDataSourceConfigService;
import com.wl4g.dopaas.uds.service.elasticjoblite.config.DataSourceFactory;
import com.wl4g.dopaas.uds.service.elasticjoblite.config.DynamicDSAutoConfiguration;
import com.wl4g.dopaas.uds.service.elasticjoblite.config.GlobalConfig;
import com.wl4g.dopaas.uds.service.elasticjoblite.domain.EventTraceDataSourceConfig;
import com.wl4g.dopaas.uds.service.elasticjoblite.domain.EventTraceDataSourceConfigs;
import com.wl4g.dopaas.uds.service.elasticjoblite.exception.JdbcDriverNotFoundException;
import com.wl4g.dopaas.uds.service.elasticjoblite.repository.ConfigurationsXmlRepository;
import com.wl4g.dopaas.uds.service.elasticjoblite.repository.impl.ConfigurationsXmlRepositoryImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Event trace data source configuration service implementation.
 */
@Slf4j
@Service
public final class EventTraceDataSourceConfigServiceImpl implements EventTraceDataSourceConfigService, InitializingBean {
	private final ConfigurationsXmlRepository configurationsXmlRepository = new ConfigurationsXmlRepositoryImpl();

	private @Autowired DynamicDSAutoConfiguration.DynamicDataSource dynamicDataSource;

	@Override
	public EventTraceDataSourceConfigs loadAll() {
		return loadGlobal().getEventTraceDataSourceConfigurations();
	}

	@Override
	public EventTraceDataSourceConfig load(final String name) {
		GlobalConfig configs = loadGlobal();
		EventTraceDataSourceConfig result = find(name, configs.getEventTraceDataSourceConfigurations());
		setActivated(configs, result);
		// Activate the dataSource by data source name for spring boot
		DynamicDSAutoConfiguration.DynamicDataSourceContextHolder.setDataSourceName(name);
		return result;
	}

	@Override
	public EventTraceDataSourceConfig find(final String name, final EventTraceDataSourceConfigs configs) {
		for (EventTraceDataSourceConfig each : configs.getEventTraceDataSourceConfiguration()) {
			if (name.equals(each.getName())) {
				return each;
			}
		}
		return null;
	}

	private void setActivated(final GlobalConfig configs, final EventTraceDataSourceConfig toBeConnectedConfig) {
		EventTraceDataSourceConfig activatedConfig = findActivatedDataSourceConfiguration(configs);
		if (!toBeConnectedConfig.equals(activatedConfig)) {
			if (null != activatedConfig) {
				activatedConfig.setActivated(false);
			}
			toBeConnectedConfig.setActivated(true);
			configurationsXmlRepository.save(configs);
		}
	}

	@Override
	public Optional<EventTraceDataSourceConfig> loadActivated() {
		return Optional.ofNullable(findActivatedDataSourceConfiguration(loadGlobal()));
	}

	private EventTraceDataSourceConfig findActivatedDataSourceConfiguration(final GlobalConfig configs) {
		for (EventTraceDataSourceConfig each : configs.getEventTraceDataSourceConfigurations()
				.getEventTraceDataSourceConfiguration()) {
			if (each.isActivated()) {
				return each;
			}
		}
		return null;
	}

	@Override
	public boolean add(final EventTraceDataSourceConfig config) {
		GlobalConfig configs = loadGlobal();
		DataSource dataSource = DataSourceFactory.createDataSource(config);
		dynamicDataSource.addDataSource(config.getName(), dataSource);
		boolean result = configs.getEventTraceDataSourceConfigurations().getEventTraceDataSourceConfiguration().add(config);
		if (result) {
			configurationsXmlRepository.save(configs);
		}
		return result;
	}

	@Override
	public void delete(final String name) {
		GlobalConfig configs = loadGlobal();
		EventTraceDataSourceConfig toBeRemovedConfig = find(name, configs.getEventTraceDataSourceConfigurations());
		if (null != toBeRemovedConfig) {
			configs.getEventTraceDataSourceConfigurations().getEventTraceDataSourceConfiguration().remove(toBeRemovedConfig);
			configurationsXmlRepository.save(configs);
		}
	}

	private GlobalConfig loadGlobal() {
		GlobalConfig result = configurationsXmlRepository.load();
		if (null == result.getEventTraceDataSourceConfigurations()) {
			result.setEventTraceDataSourceConfigurations(new EventTraceDataSourceConfigs());
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() {
		for (EventTraceDataSourceConfig each : loadGlobal().getEventTraceDataSourceConfigurations()
				.getEventTraceDataSourceConfiguration()) {
			try {
				afterLoad(each);
			} catch (final JdbcDriverNotFoundException ex) {
				log.error("Consider manually adding JDBC Driver JAR to classpath.", ex);
			}
		}
	}

	private void afterLoad(final EventTraceDataSourceConfig config) {
		DataSource dataSource = DataSourceFactory.createDataSource(config);
		dynamicDataSource.addDataSource(config.getName(), dataSource);
		DynamicDSAutoConfiguration.DynamicDataSourceContextHolder.setDataSourceName(config.getName());
	}
}
