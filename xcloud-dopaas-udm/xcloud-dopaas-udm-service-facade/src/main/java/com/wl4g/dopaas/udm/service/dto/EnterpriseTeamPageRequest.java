package com.wl4g.dopaas.udm.service.dto;

import com.wl4g.component.core.page.PageHolder;
import com.wl4g.dopaas.common.bean.udm.EnterpriseTeam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vjay
 * @date 2021-01-08 16:30:00
 */
@Getter
@Setter
public class EnterpriseTeamPageRequest extends EnterpriseTeam {

    private PageHolder<EnterpriseTeam> pm;



}