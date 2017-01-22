package com.lpmas.pdm.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.ReflectKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.bean.MaterialTypePropertyBean;
import com.lpmas.pdm.cache.MaterialInfoCache;
import com.lpmas.pdm.cache.WareInfoCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.config.MaterialConfig;
import com.lpmas.pdm.config.MaterialTypeConfig;
import com.lpmas.pdm.dao.MaterialInfoDao;

import freemarker.template.utility.StringUtil;

public class MaterialInfoBusiness {
	public int addMaterialInfo(MaterialInfoBean bean) {
		MaterialInfoDao dao = new MaterialInfoDao();
		int result = dao.insertMaterialInfo(bean);
		if (result > 0) {
			bean.setMaterialId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialId(),
					LogInfoConfig.LOG_MATERIAL_INFO);
			refreshCache(bean);
		}
		return result;
	}

	public int updateMaterialInfo(MaterialInfoBean bean) {
		MaterialInfoDao dao = new MaterialInfoDao();
		MaterialInfoBean originalBean = dao.getMaterialInfoByKey(bean.getMaterialId());
		int result = dao.updateMaterialInfo(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialId(),
					LogInfoConfig.LOG_MATERIAL_INFO);
			refreshCache(bean);
		}
		return result;
	}

	public Boolean refreshCache(MaterialInfoBean bean) {
		MaterialInfoCache cache = new MaterialInfoCache();
		WareInfoCache wareInfoCache = new WareInfoCache();
		wareInfoCache.refresWareInfoCacheByKey(InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialId());
		wareInfoCache.refresWareInfoCacheByKey(InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialNumber());
		return cache.refreshMaterialInfoCacheByKey(bean.getMaterialId());
	}

	public MaterialInfoBean getMaterialInfoByKey(int materialId) {
		MaterialInfoDao dao = new MaterialInfoDao();
		return dao.getMaterialInfoByKey(materialId);
	}

	public PageResultBean<MaterialInfoBean> getMaterialInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		MaterialInfoDao dao = new MaterialInfoDao();
		return dao.getMaterialInfoPageListByMap(condMap, pageBean);
	}

	public int getMaxMaterialInfoId() throws Exception {
		MaterialInfoDao dao = new MaterialInfoDao();
		return dao.getMaxMaterialInfoId();
	}

	public int checkStatus(String inStr, int status) {
		MaterialInfoDao dao = new MaterialInfoDao();
		return dao.checkStatus(inStr, status);
	}

	public List<MaterialInfoBean> getMaterialInfoListByTypeId(int typeId) {
		MaterialInfoDao dao = new MaterialInfoDao();
		return dao.getMaterialInfoListByTypeId(typeId);
	}

	public MaterialTypeBean getMaterialTypeCode(MaterialInfoBean bean) {
		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		return materialTypeBusiness.getMaterialTypeByKey(bean.getTypeId1());
	}

	public ReturnMessageBean verifyMaterialInfo(MaterialInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getMaterialName())) {
			result.setMessage("物料名称必须填写");
		} else if (bean.getTypeId1() == 0) {
			result.setMessage("物料类型缺失");
		} else if (!StringKit.isValid(bean.getUnit())) {
			result.setMessage("计量单位必须填写");
		} else if (bean.getGuaranteePeriod() <= 0f && bean.getGuaranteePeriod() != -1f) {
			result.setMessage("保质期必须大于0");
		}
		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		MaterialTypeBean typeBean = materialTypeBusiness.getMaterialTypeByKey(bean.getTypeId1());
		if (typeBean == null || typeBean.getStatus() == Constants.STATUS_NOT_VALID) {
			result.setMessage("物料类型无效");
		}
		return result;
	}

	public String generateMarterialNumber(MaterialInfoBean bean) {
		String result = "";
		// 查询数据库看对应的规则是否存在
		// 如果存在则使用来生成编码
		int typeId = bean.getTypeId1();
		MaterialTypePropertyBusiness materialTypePropertyBusiness = new MaterialTypePropertyBusiness();
		List<MaterialTypePropertyBean> materialTypePropertyList = materialTypePropertyBusiness
				.getMaterialTypePropertyListByTypeId(typeId);
		Map<String, MaterialTypePropertyBean> materialTypePropertyMap = ListKit.list2Map(materialTypePropertyList,
				"propertyCode");
		String numberRule = "";
		String serialNumberLength = "";
		if (materialTypePropertyList.size() > 0) {
			// 规则存在
			numberRule = materialTypePropertyMap.get(MaterialTypeConfig.NUMBER_RULE).getPropertyValue();
			serialNumberLength = materialTypePropertyMap.get(MaterialTypeConfig.SERIAL_NUMBER_LENGTH)
					.getPropertyValue();
		}
		result = generateMarterialNumber(numberRule, serialNumberLength, bean);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getValueByGetMethodName(String methodName, Object obj) throws Exception {
		Object result = new Object();
		Class clazz = obj.getClass();

		try {
			Method getMethod = clazz.getMethod("get" + methodName);
			result = getMethod.invoke(obj);
		} catch (Exception e) {
			throw new Exception("不正确的属性getter方法名");
		}

		return result;
	}

	public String generateMarterialNumber(String numberRule, String serialNumberLength, MaterialInfoBean bean) {
		StringBuffer stringBuffer = new StringBuffer();
		// 获取默认生成规则
		String defaultNumberRule = MaterialConfig.NUMBER_RULE_DEFAULT_VALUE;
		int numberLength = MaterialConfig.SERIAL_NUMBER_LENGTH;
		if (StringKit.isValid(numberRule) && StringKit.isValid(serialNumberLength)) {
			// 更新生成规则
			defaultNumberRule = numberRule;
			numberLength = Integer.valueOf(serialNumberLength);
		}

		// 拆成List
		List<String> propertyCodeList = separatePropertyCode(defaultNumberRule);
		// 获得分隔符
		String temp = propertyCodeList.get(0);
		String separateStr = temp.substring(temp.length() - 1);
		try {
			// 循环拼接
			for (String str : propertyCodeList) {
				if (!str.equals(MaterialConfig.SERIAL_NUMBER_NAME)) {
					Object reflectResult = ReflectKit.getPropertyValue(getMaterialTypeCode(bean),
							fieldName2MethodName(str));
					stringBuffer.append(reflectResult != null ? reflectResult : str.substring(0, str.length() - 1));
				} else {
					// 获得最大的物料ID
					int maxId = getMaxMaterialInfoId() + 1;
					// 生成流水号
					String serialNumber = "";
					// N位，左边补0
					serialNumber = StringUtil.leftPad(maxId + "", numberLength, "0");
					stringBuffer.append(serialNumber);
				}
				stringBuffer.append(separateStr);
			}
			// 去掉末尾分隔符
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stringBuffer.toString();
	}

	protected List<String> separatePropertyCode(String org) {
		List<String> result = new ArrayList<String>();
		// 去掉}
		org = org.replace("}", "");
		// 去掉首个{
		org = org.replaceFirst("[{]", "");
		// 拆成数组
		result = ListKit.string2List(org, "[{]");
		return result;
	}

	protected String fieldName2MethodName(String fieldName) {
		String result = "";
		String[] temp = fieldName.split("[_]");
		for (String s : temp) {
			result += StringKit.upperFirstChar(s.toLowerCase());
		}
		return StringKit.lowerFirstChar(result);
	}

	public MaterialInfoBean getMaterialInfoByNumber(String materialNumber) {
		MaterialInfoDao dao = new MaterialInfoDao();
		return dao.getMaterialInfoByNumber(materialNumber);
	}

}