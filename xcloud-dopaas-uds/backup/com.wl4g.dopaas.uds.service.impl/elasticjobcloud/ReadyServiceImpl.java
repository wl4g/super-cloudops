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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.shardingsphere.elasticjob.cloud.config.CloudJobExecutionType;
import org.apache.shardingsphere.elasticjob.cloud.config.pojo.CloudJobConfigurationPOJO;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.wl4g.dopaas.uds.service.elasticjobcloud.config.JobStateProperties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Ready service.
 */
@Slf4j
@Service
public class ReadyServiceImpl implements ReadyService {

	private @Autowired CoordinatorRegistryCenter regCenter;
	private @Autowired CloudJobConfigService cloudJobConfigService;
	private @Autowired JobStateProperties jobStateProperties;

	/**
	 * Add transient job to ready queue.
	 * 
	 * @param jobName
	 *            job name
	 */
	public void addTransient(final String jobName) {
		if (regCenter.getNumChildren(ReadyNode.ROOT) > jobStateProperties.getQueueSize()) {
			log.warn("Cannot add transient job, caused by read state queue size is larger than {}.",
					jobStateProperties.getQueueSize());
			return;
		}
		Optional<CloudJobConfigurationPOJO> cloudJobConfig = cloudJobConfigService.load(jobName);
		if (!cloudJobConfig.isPresent() || CloudJobExecutionType.TRANSIENT != cloudJobConfig.get().getJobExecutionType()) {
			return;
		}
		String readyJobNode = ReadyNode.getReadyJobNodePath(jobName);
		String times = regCenter.getDirectly(readyJobNode);
		if (cloudJobConfig.get().isMisfire()) {
			regCenter.persist(readyJobNode, Integer.toString(null == times ? 1 : Integer.parseInt(times) + 1));
		} else {
			regCenter.persist(ReadyNode.getReadyJobNodePath(jobName), "1");
		}
	}

	/**
	 * Get all ready tasks.
	 * 
	 * @return all ready tasks
	 */
	public Map<String, Integer> getAllReadyTasks() {
		if (!regCenter.isExisted(ReadyNode.ROOT)) {
			return Collections.emptyMap();
		}
		List<String> jobNames = regCenter.getChildrenKeys(ReadyNode.ROOT);
		Map<String, Integer> result = new HashMap<>(jobNames.size(), 1);
		for (String each : jobNames) {
			String times = regCenter.get(ReadyNode.getReadyJobNodePath(each));
			if (!Strings.isNullOrEmpty(times)) {
				result.put(each, Integer.parseInt(times));
			}
		}
		return result;
	}

	/**
	 * Ready node.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static final class ReadyNode {

		static final String ROOT = StateNode.ROOT + "/ready";

		private static final String READY_JOB = ROOT + "/%s";

		static String getReadyJobNodePath(final String jobName) {
			return String.format(READY_JOB, jobName);
		}
	}

}
