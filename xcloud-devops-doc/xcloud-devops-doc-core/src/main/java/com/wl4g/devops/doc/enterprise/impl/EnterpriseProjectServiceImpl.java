// Generated by XCloud DevOps for Codegen, refer: http://dts.devops.wl4g.com

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

package com.wl4g.devops.doc.enterprise.impl;

import static com.wl4g.components.common.lang.Assert2.notNullOf;
import com.wl4g.components.data.page.PageModel;
import com.github.pagehelper.PageHelper;
import com.wl4g.components.core.bean.BaseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl4g.components.core.bean.doc.EnterpriseProject;
import com.wl4g.devops.dao.doc.EnterpriseProjectDao;
import com.wl4g.devops.doc.enterprise.EnterpriseProjectService;

import static java.util.Objects.isNull;

/**
 *  service implements of {@link EnterpriseProject}
 *
 * @author root
 * @version master
 * @Date 
 * @since v1.0
 */
@Service
public class EnterpriseProjectServiceImpl implements EnterpriseProjectService {

    @Autowired
    private EnterpriseProjectDao enterpriseProjectDao;

    @Override
    public PageModel<EnterpriseProject> page(PageModel<EnterpriseProject> pm, EnterpriseProject enterpriseProject) {
        pm.page(PageHelper.startPage(pm.getPageNum(), pm.getPageSize(), true));
        pm.setRecords(enterpriseProjectDao.list(enterpriseProject));
        return pm;
    }

    @Override
    public int save(EnterpriseProject enterpriseProject) {
        if (isNull(enterpriseProject.getId())) {
        	enterpriseProject.preInsert();
            return enterpriseProjectDao.insertSelective(enterpriseProject);
        } else {
        	enterpriseProject.preUpdate();
            return enterpriseProjectDao.updateByPrimaryKeySelective(enterpriseProject);
        }
    }

    @Override
    public EnterpriseProject detail(Long id) {
        notNullOf(id, "id");
        return enterpriseProjectDao.selectByPrimaryKey(id);
    }

    @Override
    public int del(Long id) {
        notNullOf(id, "id");
        EnterpriseProject enterpriseProject = new EnterpriseProject();
        enterpriseProject.setId(id);
        enterpriseProject.setDelFlag(BaseBean.DEL_FLAG_DELETE);
        return enterpriseProjectDao.updateByPrimaryKeySelective(enterpriseProject);
    }

}
