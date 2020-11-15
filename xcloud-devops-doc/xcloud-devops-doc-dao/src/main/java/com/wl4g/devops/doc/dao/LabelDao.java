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
package com.wl4g.devops.doc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wl4g.devops.common.bean.doc.Label;

public interface LabelDao {
	int deleteByPrimaryKey(Long id);

	int insert(Label record);

	int insertSelective(Label record);

	Label selectByPrimaryKey(Long id);

	List<Label> selectByFileId(Long fileId);

	List<Long> selectLabelIdsByFileId(Long fileId);

	List<Label> selectAll();

	List<Label> list(@Param("name") String name);

	int updateByPrimaryKeySelective(Label record);

	int updateByPrimaryKey(Label record);
}