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
package com.wl4g.dopaas.uci.service;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wl4g.component.common.io.FileIOUtils.ReadTailFrame;
import com.wl4g.component.integration.feign.core.annotation.FeignConsumer;
import com.wl4g.dopaas.uci.utils.HookCommandHolder.HookCommand;
import com.wl4g.dopaas.common.bean.uci.param.RollbackParameter;
import com.wl4g.dopaas.common.bean.uci.param.RunParameter;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * CICD pipeline entry management.
 * 
 * @author Wangl.sir <983708408@qq.com>
 * @author vjay
 * @date 2019-08-01 14:45:00
 */
@FeignConsumer(name = "${provider.serviceId.uci-facade:uci-facade}")
@RequestMapping("/pipelineManager-service")
public interface PipelineManagerAdapter {

	/**
	 * New create pipeline task job.
	 * 
	 * @param runParam
	 */
	@RequestMapping(method = POST, path = "runPipeline")
	void runPipeline(@RequestBody RunParameter runParam) throws Exception;

	/**
	 * Roll-back pipeline task job.
	 * 
	 * @param rollback
	 */
	@RequestMapping(method = POST, path = "rollbackPipeline")
	void rollbackPipeline(@RequestBody RollbackParameter rollback);

	/**
	 * Hook pipeline task job.
	 * 
	 * @param param
	 */
	@RequestMapping(method = GET, path = "hookPipeline")
	void hookPipeline(@RequestBody HookCommand hook) throws Exception;

	/**
	 * Reader pipeline task building logs.
	 * 
	 * @param taskHisId
	 * @param startPos
	 * @param size
	 * @return
	 */
	@RequestMapping(method = GET, path = "logfile")
	ReadTailFrame logfile(@RequestParam(name = "taskHisId", required = false) Long taskHisId,
			@RequestParam(name = "startPos", required = false) Long startPos,
			@RequestParam(name = "size", required = false) Integer size);

	/**
	 * Reader pipeline task detail deploying logs.
	 *
	 * @param taskHisId
	 * @param taskHisDetailId
	 * @param startPos
	 * @param size
	 * @return
	 */
	@RequestMapping(method = GET, path = "logDetailFile")
	ReadTailFrame logDetailFile(@RequestParam(name = "taskHisId", required = false) Long taskHisId,
			@RequestParam(name = "instanceId", required = false) Long instanceId,
			@RequestParam(name = "startPos", required = false) Long startPos,
			@RequestParam(name = "size", required = false) Integer size);

}