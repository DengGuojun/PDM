package com.lpmas.pdm.business.impl;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.business.PropertyInputVerify;

public class PropertyInputVerifyBooleanImpl extends PropertyInputVerify {

	public ReturnMessageBean verifyProperty(String value) {
		ReturnMessageBean result = new ReturnMessageBean();
		for (StatusBean<Integer, String> select : Constants.SELECT_LIST) {
			if (value.equals(select.getValue())) {
				value = String.valueOf(select.getStatus());
				break;
			}
		}
		if (value.equals(String.valueOf(Constants.STATUS_VALID))
				|| value.equals(String.valueOf(Constants.STATUS_NOT_VALID))) {
		} else {
			result.setMessage("该字段必须是布尔值;");
		}
		return result;
	}

}
