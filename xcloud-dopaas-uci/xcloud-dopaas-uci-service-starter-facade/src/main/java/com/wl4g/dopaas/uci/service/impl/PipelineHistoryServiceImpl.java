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
package com.wl4g.dopaas.uci.service.impl;

import static com.wl4g.component.common.lang.Assert2.notNullOf;
import static com.wl4g.dopaas.common.constant.UciConstants.TASK_STATUS_CREATE;
import static com.wl4g.dopaas.common.constant.UciConstants.TASK_STATUS_STOPING;
import static com.wl4g.iam.common.utils.IamOrganizationUtils.getRequestOrganizationCode;
import static com.wl4g.iam.common.utils.IamOrganizationUtils.getRequestOrganizationCodes;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.wl4g.component.common.lang.Assert2;
import com.wl4g.component.core.page.PageHolder;
import com.wl4g.component.support.cli.DestroableProcessManager;
import com.wl4g.component.support.cli.destroy.DestroySignal;
import com.wl4g.dopaas.cmdb.service.AppClusterService;
import com.wl4g.dopaas.cmdb.service.AppInstanceService;
import com.wl4g.dopaas.common.bean.cmdb.AppCluster;
import com.wl4g.dopaas.common.bean.cmdb.AppInstance;
import com.wl4g.dopaas.common.bean.uci.Pipeline;
import com.wl4g.dopaas.common.bean.uci.PipelineHistory;
import com.wl4g.dopaas.common.bean.uci.PipelineHistoryInstance;
import com.wl4g.dopaas.common.bean.uci.PipelineInstance;
import com.wl4g.dopaas.common.bean.uci.param.HookParameter;
import com.wl4g.dopaas.common.bean.uci.param.RollbackParameter;
import com.wl4g.dopaas.common.bean.uci.param.RunParameter;
import com.wl4g.dopaas.uci.data.PipelineDao;
import com.wl4g.dopaas.uci.data.PipelineHistoryDao;
import com.wl4g.dopaas.uci.data.PipelineHistoryInstanceDao;
import com.wl4g.dopaas.uci.data.PipelineInstanceDao;
import com.wl4g.dopaas.uci.service.PipelineHistoryService;

/**
 * @author vjay
 * @date 2020-04-27 17:25:00
 */
@Service
public class PipelineHistoryServiceImpl implements PipelineHistoryService {

	private @Autowired PipelineDao pipelineDao;
	private @Autowired PipelineHistoryDao pipelineHistoryDao;
	private @Autowired PipelineHistoryInstanceDao pipeHistoryInstanceDao;
	private @Autowired PipelineInstanceDao pipelineInstanceDao;
	private @Autowired AppClusterService appClusterService;
	private @Autowired AppInstanceService appInstanceService;
	private @Autowired DestroableProcessManager pm;

	// --- Create Pipe Runner. ---

	@Override
	public PipelineHistory createRunnerPipeline(RunParameter runParam) {
		Assert2.notNullOf(runParam, "runParam");
		Long pipeId = notNullOf(runParam.getPipeId(), "pipeId");
		String traceId = runParam.getTrackId();
		String traceType = runParam.getTrackType();
		String remark = runParam.getRemark();
		String annex = runParam.getAnnex();

		Pipeline pipeline = pipelineDao.selectByPrimaryKey(pipeId);
		Assert2.notNullOf(pipeline, "pipeline");

		PipelineHistory pipeHistory = new PipelineHistory();
		pipeHistory.preInsert(pipeline.getOrganizationCode());
		pipeHistory.setPipeId(pipeId);
		pipeHistory.setProviderKind(pipeline.getProviderKind());
		pipeHistory.setAnnex(annex);
		pipeHistory.setStatus(TASK_STATUS_CREATE);
		pipeHistory.setTrackId(traceId);
		pipeHistory.setTrackType(traceType);
		pipeHistory.setRemark(remark);

		pipeHistory.setOrchestrationType(runParam.getOrchestrationType());
		pipeHistory.setOrchestrationId(runParam.getOrchestrationId());

		pipelineHistoryDao.insertSelective(pipeHistory);
		createPipeHistoryInstance(pipeline.getId(), pipeHistory.getId());
		return pipeHistory;

	}

	@Override
	public PipelineHistory createHookPipeline(HookParameter param) {
		Long pipeId = notNullOf(param.getPipeId(), "pipeId");
		String remark = param.getRemark();
		RunParameter runParam = new RunParameter(pipeId, remark, null, null, null, null);

		Pipeline pipeline = notNullOf(pipelineDao.selectByPrimaryKey(pipeId), "pipeline");

		PipelineHistory pipeHistory = new PipelineHistory();
		pipeHistory.preInsert(getRequestOrganizationCode());
		pipeHistory.setPipeId(pipeId);
		pipeHistory.setProviderKind(pipeline.getProviderKind());
		pipeHistory.setAnnex(null);
		pipeHistory.setStatus(TASK_STATUS_CREATE);
		pipeHistory.setTrackId(null);
		pipeHistory.setTrackType(null);
		pipeHistory.setRemark(remark);

		pipelineHistoryDao.insertSelective(pipeHistory);
		createPipeHistoryInstance(pipeline.getId(), pipeHistory.getId());

		return createRunnerPipeline(runParam);
	}

	@Override
	public PipelineHistory createRollbackPipeline(RollbackParameter param) {
		Assert2.notNullOf(param, "rollbackParameter");
		Long pipeId = param.getPipeId();
		Assert2.notNullOf(pipeId, "pipeId");
		String remark = param.getRemark();

		PipelineHistory oldPipelineHistory = selectByPrimaryKey(pipeId);
		Assert2.notNullOf(oldPipelineHistory, "pipelineHistory");

		Long oldPipeId = oldPipelineHistory.getPipeId();
		Pipeline pipeline = pipelineDao.selectByPrimaryKey(oldPipeId);
		Assert2.notNullOf(pipeline, "pipeline");

		PipelineHistory pipelineHistory = new PipelineHistory();
		BeanUtils.copyProperties(oldPipelineHistory, pipelineHistory);
		pipelineHistory.preInsert(getRequestOrganizationCode());
		pipelineHistory.setPipeId(oldPipeId);
		pipelineHistory.setProviderKind(pipeline.getProviderKind());
		pipelineHistory.setStatus(TASK_STATUS_CREATE);
		pipelineHistory.setRemark(remark);
		pipelineHistory.setRefId(pipeId);

		pipelineHistoryDao.insertSelective(pipelineHistory);
		createPipeHistoryInstance(pipeline.getId(), pipelineHistory.getId());
		return pipelineHistory;

	}

	/**
	 * fix Cross DB
	 */
	private PipelineHistory selectByPrimaryKey(Long id) {
		PipelineHistory oldPipelineHistory = pipelineHistoryDao.selectByPrimaryKey(id);
		AppCluster appCluster = appClusterService.getById(oldPipelineHistory.getClusterId());
		oldPipelineHistory.setClusterName(appCluster.getName());
		return oldPipelineHistory;
	}

	// --- CRUD. ---

	private void createPipeHistoryInstance(Long pipeId, Long pipeHisId) {
		List<PipelineInstance> pipelineInstances = pipelineInstanceDao.selectByPipeId(pipeId);
		if (!CollectionUtils.isEmpty(pipelineInstances)) {
			for (PipelineInstance pipelineInstance : pipelineInstances) {
				PipelineHistoryInstance pipelineHistoryInstance = new PipelineHistoryInstance();
				pipelineHistoryInstance.preInsert();
				pipelineHistoryInstance.setStatus(TASK_STATUS_CREATE);
				pipelineHistoryInstance.setInstanceId(pipelineInstance.getInstanceId());
				pipelineHistoryInstance.setPipeHistoryId(pipeHisId);
				pipeHistoryInstanceDao.insertSelective(pipelineHistoryInstance);
			}
		}
	}

	@Override
	public void updateStatus(Long pipeId, int status) {
		PipelineHistory pipelineHistory = new PipelineHistory();
		pipelineHistory.preUpdate();
		pipelineHistory.setId(pipeId);
		pipelineHistory.setStatus(status);
		pipelineHistoryDao.updateByPrimaryKeySelective(pipelineHistory);
	}

	@Override
	public void updateStatusAndResultAndSha(Long pipeId, int status, String sha) {
		PipelineHistory pipelineHistory = new PipelineHistory();
		pipelineHistory.preUpdate();
		pipelineHistory.setId(pipeId);
		pipelineHistory.setStatus(status);
		pipelineHistory.setShaLocal(sha);
		pipelineHistoryDao.updateByPrimaryKeySelective(pipelineHistory);
	}

	@Override
	public void stopByPipeHisId(Long pipeHisId) {
		PipelineHistory pipelineHistory = new PipelineHistory();
		pipelineHistory.preUpdate();
		pipelineHistory.setId(pipeHisId);
		pipelineHistory.setStatus(TASK_STATUS_STOPING);
		pipelineHistoryDao.updateByPrimaryKeySelective(pipelineHistory);

		// TODO timeoutMs?
		pm.destroyForComplete(new DestroySignal(String.valueOf(pipeHisId), 5000L));
	}

	@Override
	public void updateCostTime(Long taskId, long costTime) {
		PipelineHistory pipelineHistory = new PipelineHistory();
		pipelineHistory.preUpdate();
		pipelineHistory.setId(taskId);
		pipelineHistory.setCostTime(costTime);
		pipelineHistoryDao.updateByPrimaryKeySelective(pipelineHistory);
	}

	@Override
	public PageHolder<PipelineHistory> list(PageHolder<PipelineHistory> pm, String pipeName, String clusterName,
			String environment, String startDate, String endDate, String providerKind) {
		listWithoutPage(pm, pipeName, clusterName, environment, startDate, endDate, providerKind, null, null);
		return pm;
	}

	// fix cross db
	@Override
	public List<PipelineHistory> listWithoutPage(PageHolder<PipelineHistory> pm, String pipeName, String clusterName,
			String environment, String startDate, String endDate, String providerKind, Integer orchestrationType,
			Long orchestrationId) {
		List<Long> clusterIds = null;
		if (StringUtils.isNotBlank(clusterName)) {
			clusterIds = appClusterService.getIdsByLikeName(clusterName);
		}
		if (pm != null) {
			pm.useCount().bind();
		}
		List<PipelineHistory> list = pipelineHistoryDao.list(getRequestOrganizationCodes(), pipeName, clusterIds, environment,
				startDate, endDate, providerKind, orchestrationType, orchestrationId);
		if (pm != null) {
			pm.setRecords(list);
		}
		for (PipelineHistory pipelineHistory : list) {
			AppCluster appCluster = appClusterService.getById(pipelineHistory.getClusterId());
			if (appCluster != null) {
				pipelineHistory.setClusterName(appCluster.getName());
			}
		}
		return list;
	}

	@Override
	public List<PipelineHistoryInstance> getPipeHisInstanceByPipeId(Long pipeId) {
		List<PipelineHistoryInstance> pipelineHistoryInstances = pipeHistoryInstanceDao.selectByPipeHistoryId(pipeId);

		for (PipelineHistoryInstance pipelineHistoryInstance : pipelineHistoryInstances) {
			AppInstance appInstance = appInstanceService.getById(pipelineHistoryInstance.getInstanceId());
			pipelineHistoryInstance.setInstanceName(appInstance.getName());
		}

		return pipelineHistoryInstances;
	}

	@Override
	public PipelineHistory detail(Long pipeId) {
		PipelineHistory pipeHistory = selectByPrimaryKey(pipeId);
		pipeHistory.setPipelineHistoryInstances(getPipeHisInstanceByPipeId(pipeId));
		return pipeHistory;
	}

	@Override
	public PipelineHistory getById(Long pipeHisId) {
		return selectByPrimaryKey(pipeHisId);
	}

	@Override
	public void updatePipeHisInstanceStatus(Long pipeInstanceId, int status) {
		PipelineHistoryInstance pipelineHistoryInstance = new PipelineHistoryInstance();
		pipelineHistoryInstance.preUpdate();
		pipelineHistoryInstance.setId(pipeInstanceId);
		pipelineHistoryInstance.setStatus(status);
		pipeHistoryInstanceDao.updateByPrimaryKeySelective(pipelineHistoryInstance);
	}

}