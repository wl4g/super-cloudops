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
package com.wl4g.dopaas.udm.web;

import com.wl4g.component.common.web.rest.RespBase;
import com.wl4g.component.core.web.BaseController;
import com.wl4g.component.core.page.PageHolder;
import com.wl4g.dopaas.common.bean.udm.Share;
import com.wl4g.dopaas.udm.service.ShareService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.shiro.authz.annotation.Logical.AND;

/**
 * @author vjay
 * @date 2019-11-12 11:03:00
 */
@RestController
@RequestMapping("/share")
public class ShareController extends BaseController {

	private @Autowired ShareService shareService;

	@RequestMapping("/list")
	@RequiresPermissions(value = { "udm:share" }, logical = AND)
	public RespBase<?> list(PageHolder<Share> pm, String name) {
		RespBase<Object> resp = RespBase.create();
		resp.setData(shareService.list(pm));
		return resp;
	}

	@RequestMapping("/cancelShare")
	@RequiresPermissions(value = { "udm:share" }, logical = AND)
	public RespBase<?> del(Long id) {
		RespBase<Object> resp = RespBase.create();
		shareService.cancelShare(id);
		return resp;
	}

}