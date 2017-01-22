package com.lpmas.pdm.business;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.cache.MaterialTypeCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.MaterialTypeDao;

public class MaterialTypeBusiness {
	public int addMaterialType(MaterialTypeBean bean) {
		MaterialTypeDao dao = new MaterialTypeDao();
		int result = dao.insertMaterialType(bean);
		if (result > 0) {
			bean.setTypeId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getTypeId(),
					LogInfoConfig.LOG_MATERIAL_TYPE);
			refreshCache(bean);
		}
		return result;
	}

	public int updateMaterialType(MaterialTypeBean bean) {
		MaterialTypeDao dao = new MaterialTypeDao();
		MaterialTypeBean originalBean = dao.getMaterialTypeByKey(bean.getTypeId());		
		int result = dao.updateMaterialType(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getTypeId(),
					LogInfoConfig.LOG_MATERIAL_TYPE);
			refreshCache(bean);
		}
		return result;
	}

	private Boolean refreshCache(MaterialTypeBean bean) {
		MaterialTypeCache cache = new MaterialTypeCache();
		return cache.refreshMaterialTypeCacheByKey(bean.getTypeId());
	}

	public MaterialTypeBean getMaterialTypeByKey(int typeId) {
		MaterialTypeDao dao = new MaterialTypeDao();
		return dao.getMaterialTypeByKey(typeId);
	}

	public PageResultBean<MaterialTypeBean> getMaterialTypePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		MaterialTypeDao dao = new MaterialTypeDao();
		return dao.getMaterialTypePageListByMap(condMap, pageBean);
	}

	public MaterialTypeBean getMarterialTypeByCode(String typeCode) {
		MaterialTypeDao dao = new MaterialTypeDao();
		return dao.getMaterialTypeByCondition("type_code", typeCode);
	}

	public List<MaterialTypeBean> getMaterialTypeAllList() {
		MaterialTypeDao dao = new MaterialTypeDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getMaterialTypeListByMap(condMap);
	}

	/**
	 * 根据传入的类型ID获得父到子的链表
	 * 
	 * @param typeId
	 * @return
	 */
	public LinkedList<MaterialTypeBean> getParentTreeListByKey(int typeId) {
		LinkedList<MaterialTypeBean> result = new LinkedList<MaterialTypeBean>();
		MaterialTypeBean bean = getMaterialTypeByKey(typeId);

		while (bean != null) {
			result.add(bean);
			bean = getMaterialTypeByKey(bean.getParentTypeId());
		}
		Collections.reverse(result);
		return result;
	}

	public ReturnMessageBean verifyMaterialType(MaterialTypeBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getTypeCode())) {
			result.setMessage("分类代码必须填写");
		} else if (isDuplicateTypeCode(bean.getTypeId(), bean.getTypeCode())) {
			result.setMessage("已存在相同的分类代码");
		} else if (!isValidTypeCode(bean.getTypeCode())) {
			result.setMessage("分类代码格式不符合规范");
		} else if (!StringKit.isValid(bean.getTypeName())) {
			result.setMessage("分类名称必须填写");
		} else if (bean.getTypeName().length() > 30) {
			result.setMessage("品牌名称不能大于30字");
		}

		if (bean.getStatus() == Constants.STATUS_NOT_VALID) {
			// 检查该类型下有无商品
			MaterialInfoBusiness materialInfoBusiness = new MaterialInfoBusiness();
			List<MaterialInfoBean> list = materialInfoBusiness.getMaterialInfoListByTypeId(bean.getTypeId());
			if (list.size() > 0) {
				result.setMessage("该物料类型下包含物料，不能设置为无效");
			} else {
				MaterialPropertyTypeBusiness materialPropertyTypeBusiness = new MaterialPropertyTypeBusiness();
				if (materialPropertyTypeBusiness.getMaterialPropertyTypeListByMaterialTypeId(bean.getTypeId())
						.size() > 0
						|| materialPropertyTypeBusiness.getMaterialPropertyTypeListByMaterialTypeId(bean.getTypeId())
								.size() > 0) {
					result.setMessage("该物料类型下包含物料属性，不能设置为无效");
				}
			}
		}
		return result;
	}

	public boolean isDuplicateTypeCode(int typeId, String typeCode) {
		MaterialTypeBean bean = getMarterialTypeByCode(typeCode);
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