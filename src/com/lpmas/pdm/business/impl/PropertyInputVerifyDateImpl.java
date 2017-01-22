package com.lpmas.pdm.business.impl;

import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.business.PropertyInputVerify;

public class PropertyInputVerifyDateImpl extends PropertyInputVerify {

	public ReturnMessageBean verifyProperty(String value) {
		ReturnMessageBean result = new ReturnMessageBean();
		try {
			if(StringKit.isValid(value)){
				value = value.replaceAll("/", "-");
			}
			DateKit.str2Date(value, DateKit.DEFAULT_DATE_FORMAT);
		} catch (Exception e) {
			result.setMessage("该字段必须符合日期格式;");
		}

		return result;
	}

}
