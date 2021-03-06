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
package com.wl4g.dopaas.uci.data;

import org.apache.ibatis.annotations.Param;

import com.wl4g.dopaas.common.bean.uci.Project;

import java.util.List;

public interface ProjectDao {
	int deleteByPrimaryKey(Long id);

	int insert(Project record);

	int insertSelective(Project record);

	Project selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Project record);

	int updateByPrimaryKey(Project record);

	List<Project> list(@Param("organizationCodes") List<String> organizationCodes, @Param("clusterIds") List<Long> clusterIds,
			@Param("projectName") String projectName, @Param("isBoot") Integer isBoot);

	Project getByAppClusterId(Long appGrouPId);

}