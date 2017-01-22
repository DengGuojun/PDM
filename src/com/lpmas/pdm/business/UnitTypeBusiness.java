package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.cache.UnitTypeCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.UnitTypeDao;

public class UnitTypeBusiness {
	public int addUnitType(UnitTypeBean bean) {
		UnitTypeDao dao = new UnitTypeDao();		
		int result = dao.insertUnitType(bean);
		if (result > 0) {
			bean.setTypeId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_UNIT, bean.getTypeId(), LogInfoConfig.LOG_UNIT_TYPE);
			refreshCache();
		}
		return result;
	}

	public int updateUnitType(UnitTypeBean bean) {
		UnitTypeDao dao = new UnitTypeDao();
		UnitTypeBean originalBean = dao.getUnitTypeByKey(bean.getTypeId());		
		int result = dao.updateUnitType(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_UNIT, bean.getTypeId(),
					LogInfoConfig.LOG_UNIT_TYPE);
			refreshCache();
		}
		return result;
	}

	public Boolean refreshCache() {
		UnitTypeCache cache = new UnitTypeCache();
		return cache.refreshUnitTypeAllListCache();
	}

	public UnitTypeBean getUnitTypeByKey(int typeId) {
		UnitTypeDao dao = new UnitTypeDao();
		return dao.getUnitTypeByKey(typeId);
	}

	public UnitTypeBean getUnitTypeByCode(String typeCode) {
		UnitTypeDao dao = new UnitTypeDao();
		return dao.getUnitTypeByCode(typeCode);
	}

	public PageResultBean<UnitTypeBean> getUnitTypePageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		UnitTypeDao dao = new UnitTypeDao();
		return dao.getUnitTypePageListByMap(condMap, pageBean);
	}

	public List<UnitTypeBean> getUnitTypeAllList() {
		UnitTypeDao dao = new UnitTypeDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getUnitTypeListByMap(condMap);
	}

	public ReturnMessageBean verifyUnitTypeProperty(UnitTypeBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getTypeCode())) {
			result.setMessage("单位类型代码必须填写");
		} else if (isDuplicateTypeCode(bean.getTypeId(), bean.getTypeCode())) {
			result.setMessage("已存在相同的单位类型代码");
		} else if (!isValidTypeCode(bean.getTypeCode())) {
			result.setMessage("单位类型代码格式不符合规范");
		} else if (!StringKit.isValid(bean.getTypeName())) {
			result.setMessage("单位类型名称必须填写");
		} else if (bean.getTypeName().length() > 30) {
			result.setMessage("单位类型名称不能大于30字");
		}
		return result;
	}

	public boolean isDuplicateTypeCode(int typeId, String typeCode) {
		UnitTypeBean bean = getUnitTypeByCode(typeCode);
		if (bean == null) {
			return false;
		} else {
			if (typeId > 0 && typeId == bean.getTypeId()) {
				return false;
			}
		}
		return true;
	}

	public boolean isValidTypeCode(String typeCode) {
		if (!typeCode.matches("^[a-zA-Z0-9_]+$")) {
			return false;
		}
		if (typeCode.length() > 10) {
			return false;
		}
		return true;
	}

}