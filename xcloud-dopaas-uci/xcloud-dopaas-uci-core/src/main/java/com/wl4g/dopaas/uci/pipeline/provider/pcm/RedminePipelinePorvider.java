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
package com.wl4g.dopaas.uci.pipeline.provider.pcm;

import com.wl4g.dopaas.uci.service.PcmService;
import com.wl4g.dopaas.common.bean.uci.PipeHistoryPcm;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vjay
 * @date 2020-04-27 09:29:00
 */
public class RedminePipelinePorvider implements PcmPipelineProvider {

	private @Autowired  PcmService pcmService;

	@Override
	public void createIssues(Long pcmId, PipeHistoryPcm pipeHistoryPcm) {
		pcmService.createIssues(pcmId, pipeHistoryPcm);
	}

}