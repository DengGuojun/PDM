package com.lpmas.pdm.config;

import java.util.HashMap;
import java.util.Map;

public class LogInfoDisConfig {
	
	public static Map<String, Map<?, ?>> CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
	static {
		CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
		CONFIG_VALUE_MAP.put("质量等级",MaterialConfig.MATERIAL_QUALITY_LEVEL_MAP);
		CONFIG_VALUE_MAP.put("使用状态",MaterialConfig.MATERIAL_USE_STATUS_MAP);
		CONFIG_VALUE_MAP.put("属性类型，基本属性、仓储属性",MaterialPropertyConfig.PROP_TYPE_MAP);
		CONFIG_VALUE_MAP.put("信息类型",MaterialPropertyConfig.INFO_TYPE_MAP);
		CONFIG_VALUE_MAP.put("输入方式，文本框、选择框",MaterialPropertyConfig.INPUT_METHOD_MAP);
		CONFIG_VALUE_MAP.put("数据字段类型文本、数字",MaterialPropertyConfig.FIELD_TYPE_MAP);
		CONFIG_VALUE_MAP.put("是否可修改",MaterialPropertyConfig.PROPERTY_MODIFI_MAP);
		CONFIG_VALUE_MAP.put("是否可销售",ProductConfig.PRODUCT_USE_STATUS_MAP);
	}

}
