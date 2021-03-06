/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
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
package com.wl4g.devops.uos.server.handler;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;

import com.wl4g.devops.uos.common.exception.CossException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * {@link DispatchSecCossChannelHandler}
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020年6月17日
 * @since
 */
public class DispatchSecCossChannelHandler extends GenericCossChannelHandler {

private @Autowired  CossActionProvider actionProvider;

	@Override
	protected void doChannelRead(ChannelHandlerContext ctx, FullHttpRequest req) {
		// Gets handler action.
		CossAction action = getAction(ctx, req);
		GenericCossChannelHandler handler = actionProvider.getHandler(action);

		// Check access control
		checkAccessControl(ctx, req, action, handler);

		// Dispatching channel handling
		handler.doChannelRead(ctx, req);

	}

	/**
	 * Gets uos action.
	 * 
	 * @param ctx
	 * @param req
	 * @return
	 */
	protected CossAction getAction(ChannelHandlerContext ctx, FullHttpRequest req) {
		return CossAction.of(req.uri());
	}

	/**
	 * Check access control
	 * 
	 * @param ctx
	 * @param req
	 * @param action
	 * @param handler
	 */
	protected void checkAccessControl(ChannelHandlerContext ctx, FullHttpRequest req, CossAction action,
			GenericCossChannelHandler handler) {

		// Action method valid.
		if (action.getMethod() != req.method()) {
			throw new CossException(format("Unsupported action method: %s", req.method()));
		}

	}

}