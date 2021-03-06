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
package com.wl4g.dopaas.scm.handler;

import org.springframework.http.ResponseEntity;

import com.google.common.annotations.Beta;
import com.wl4g.dopaas.scm.common.model.*;
import com.wl4g.dopaas.scm.publish.WatchDeferredResult;

/**
 * Config soruce context handler interface
 * 
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年5月27日
 * @since
 */
@Beta
public interface CentralConfigServerHandler {

	/**
	 * Watching long-polling
	 * 
	 * @param watch
	 * @return
	 */
	WatchDeferredResult<ResponseEntity<?>> watch(FetchReleaseConfigRequest watch);

	/**
	 * Release configuration property-sources.
	 * 
	 * @param result
	 *            request parameter.
	 */
	void release(ReleaseConfigInfo result);

	/**
	 * Access configuration client report configure result.
	 * 
	 * @param report
	 *            request parameter.
	 * @param resp
	 *            response parameter.
	 * @return
	 */
	void report(ReportChangedRequest report);

}