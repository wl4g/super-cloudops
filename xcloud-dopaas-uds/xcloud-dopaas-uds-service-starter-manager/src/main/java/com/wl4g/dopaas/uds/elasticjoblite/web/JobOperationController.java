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

package com.wl4g.dopaas.uds.elasticjoblite.web;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

import java.util.Collection;

import org.apache.shardingsphere.elasticjob.lite.lifecycle.domain.JobBriefInfo;
import org.apache.shardingsphere.elasticjob.lite.lifecycle.domain.ShardingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wl4g.component.common.web.rest.RespBase;
import com.wl4g.component.core.web.BaseController;
import com.wl4g.dopaas.uds.service.elasticjoblite.JobAPIService;

/**
 * Job operation RESTful API.
 */
@RestController
@RequestMapping("/api/jobs")
public class JobOperationController extends BaseController {

	private @Autowired JobAPIService jobAPIService;

	/**
	 * Get jobs total count.
	 * 
	 * @return jobs total count
	 */
	@GetMapping("/count")
	public int getJobsTotalCount() {
		return jobAPIService.getJobsTotalCount();
	}

	/**
	 * Get all jobs brief info.
	 * 
	 * @return all jobs brief info
	 */
	@GetMapping("/getAllJobsBriefInfo")
	public RespBase<Collection<JobBriefInfo>> getAllJobsBriefInfo() {
		Collection<JobBriefInfo> data = nonNull(SessionRegistryCenterFactory.getRegistryCenterConfiguration())
				? jobAPIService.getAllJobsBriefInfo()
				: emptyList();
		return RespBase.<Collection<JobBriefInfo>> create().withData(data);
	}

	/**
	 * Trigger job.
	 * 
	 * @param jobName
	 *            job name
	 */
	@PostMapping("/{jobName}/trigger")
	public RespBase<Boolean> triggerJob(@PathVariable("jobName") final String jobName) {
		jobAPIService.triggerJob(jobName);
		return RespBase.<Boolean> create().withData(Boolean.TRUE);
	}

	/**
	 * Disable job.
	 * 
	 * @param jobName
	 *            job name
	 */
	@PostMapping(value = "/{jobName}/disable")
	public RespBase<Boolean> disableJob(@PathVariable("jobName") final String jobName) {
		jobAPIService.disableJob(jobName, null);
		return RespBase.<Boolean> create().withData(Boolean.TRUE);
	}

	/**
	 * Enable job.
	 *
	 * @param jobName
	 *            job name
	 */
	@PostMapping(value = "/{jobName}/enable")
	public RespBase<Boolean> enableJob(@PathVariable("jobName") final String jobName) {
		jobAPIService.enableJob(jobName, null);
		return RespBase.<Boolean> create().withData(Boolean.TRUE);
	}

	/**
	 * Shutdown job.
	 * 
	 * @param jobName
	 *            job name
	 */
	@PostMapping(value = "/{jobName}/shutdown")
	public RespBase<Boolean> shutdownJob(@PathVariable("jobName") final String jobName) {
		jobAPIService.shutdownJob(jobName, null);
		return RespBase.<Boolean> create().withData(Boolean.TRUE);
	}

	/**
	 * Get sharding info.
	 * 
	 * @param jobName
	 *            job name
	 * @return sharding info
	 */
	@GetMapping(value = "/{jobName}/sharding")
	public RespBase<Collection<ShardingInfo>> getShardingInfo(@PathVariable("jobName") final String jobName) {
		Collection<ShardingInfo> data = jobAPIService.getShardingInfo(jobName);
		return RespBase.<Collection<ShardingInfo>> create().withData(data);
	}

	/**
	 * Disable sharding.
	 *
	 * @param jobName
	 *            job name
	 * @param item
	 *            sharding item
	 */
	@PostMapping(value = "/{jobName}/sharding/{item}/disable")
	public RespBase<Boolean> disableSharding(@PathVariable("jobName") final String jobName,
			@PathVariable("item") final String item) {
		jobAPIService.disableJob(jobName, item);
		return RespBase.<Boolean> create().withData(Boolean.TRUE);
	}

	/**
	 * Enable sharding.
	 *
	 * @param jobName
	 *            job name
	 * @param item
	 *            sharding item
	 */
	@PostMapping(value = "/{jobName}/sharding/{item}/enable")
	public RespBase<Boolean> enableSharding(@PathVariable("jobName") final String jobName,
			@PathVariable("item") final String item) {
		jobAPIService.enableJob(jobName, item);
		return RespBase.<Boolean> create().withData(Boolean.TRUE);
	}
}
