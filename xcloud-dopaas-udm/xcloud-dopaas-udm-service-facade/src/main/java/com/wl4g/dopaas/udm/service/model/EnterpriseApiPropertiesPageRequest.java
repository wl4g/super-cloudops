package com.wl4g.dopaas.udm.service.model;

import com.wl4g.component.core.page.PageHolder;
import com.wl4g.dopaas.common.bean.udm.EnterpriseApiProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vjay
 * @date 2021-01-08 16:30:00
 */
@Getter
@Setter
public class EnterpriseApiPropertiesPageRequest extends EnterpriseApiProperties {
	private static final long serialVersionUID = 1L;

	private PageHolder<EnterpriseApiProperties> pm;
}
