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
package com.wl4g.devops.common.utils.bean;

import static com.wl4g.devops.components.tools.common.serialize.JacksonUtils.toJSONString;
import static java.lang.System.out;

import org.springframework.cglib.core.Converter;

public class BeanCopierUtilsTests {

	public static void main(String[] args) {
		//
		// 注：bean的setter方法必须是标准的，如：setter方法有返回值也会导致无法复制
		//
		SimpleUserPrincipal p1 = new SimpleUserPrincipal();
		p1.setPrincipalId("001");
		p1.setPrincipal("zs");
		p1.getAttributes().put("aa", "11");
		out.println("source p1 object: " + toJSONString(p1) + ", hashCode: " + p1.hashCode());

		SimpleUserPrincipal p2 = new SimpleUserPrincipal();
		BeanCopierUtils.mapper(p1, p2, new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Object fieldValue, Class fieldClass, Object setterMethod) {
				// System.out.println(arg0);
				return fieldValue;
			}
		});
		out.println("clone p2 object: " + toJSONString(p2) + ", hashCode: " + p2.hashCode());

	}

}
