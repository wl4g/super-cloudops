/*
 * Copyright (C) 2017 ~ 2025 the original author or authors.
 * <Wanglsir@gmail.com, 983708408@qq.com> Technology CO.LTD.
 * All rights reserved.
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
 * 
 * Reference to website: http://wl4g.com
 */
package com.wl4g.dopaas.udm.service.conversion;

import static com.wl4g.component.common.log.SmartLoggerFactory.getLogger;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.wl4g.component.common.log.SmartLogger;
import com.wl4g.dopaas.common.bean.udm.EnterpriseApiProperties;

/**
 * {@link AbstractDocumentConverter}
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2020-12-16
 * @sine v1.0
 * @see
 */
public abstract class AbstractDocumentConverter<T> implements DocumentConverter<T> {
	protected final SmartLogger log = getLogger(getClass());

	protected final static String REQUEST = "Request";
	protected final static String RESPONSE = "Response";

	protected List<EnterpriseApiProperties> getPropertiesByScope(List<EnterpriseApiProperties> enterpriseApiProperties,
			String scope) {
		if (CollectionUtils.isEmpty(enterpriseApiProperties)) {
			return Collections.emptyList();
		}
		List<EnterpriseApiProperties> result = enterpriseApiProperties.stream()
				.filter((EnterpriseApiProperties e) -> (e.getScope().equals(scope))).collect(Collectors.toList());
		return result;
	}

}
