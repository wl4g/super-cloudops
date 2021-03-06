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
package com.wl4g.dopaas.common.bean.umc;

import com.wl4g.component.core.bean.BaseBean;

public class CustomAlarmEvent extends BaseBean {
	private static final long serialVersionUID = 381411777614066880L;

	private Long customEngineId;

	private String notifyGroupIds;

	private String message;

	private String engineName;

	public Long getCustomEngineId() {
		return customEngineId;
	}

	public void setCustomEngineId(Long customEngineId) {
		this.customEngineId = customEngineId;
	}

	public String getNotifyGroupIds() {
		return notifyGroupIds;
	}

	public void setNotifyGroupIds(String notifyGroupIds) {
		this.notifyGroupIds = notifyGroupIds == null ? null : notifyGroupIds.trim();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message == null ? null : message.trim();
	}

	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}
}