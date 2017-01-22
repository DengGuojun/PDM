package com.lpmas.pdm.factory;

import com.lpmas.pdm.business.PropertyInputDisplay;
import com.lpmas.pdm.business.PropertyInputVerify;
import com.lpmas.pdm.business.impl.PropertyInputDisplayCheckboxImpl;
import com.lpmas.pdm.business.impl.PropertyInputDisplayDateImpl;
import com.lpmas.pdm.business.impl.PropertyInputDisplaySelectImpl;
import com.lpmas.pdm.business.impl.PropertyInputDisplayTextImpl;
import com.lpmas.pdm.business.impl.PropertyInputDisplayTextareaImpl;
import com.lpmas.pdm.business.impl.PropertyInputVerifyBooleanImpl;
import com.lpmas.pdm.business.impl.PropertyInputVerifyDateImpl;
import com.lpmas.pdm.business.impl.PropertyInputVerifyNumberImpl;
import com.lpmas.pdm.config.MaterialPropertyConfig;

public class MaterialPropertyFactory {
	
	public PropertyInputDisplay getPropertyInputDisplay(int inputMethod) {
		if (inputMethod == MaterialPropertyConfig.INPUT_METHOD_TEXT) {
			return new PropertyInputDisplayTextImpl();
		} else if (inputMethod == MaterialPropertyConfig.INPUT_METHOD_SELECT) {
			return new PropertyInputDisplaySelectImpl();
		} else if (inputMethod == MaterialPropertyConfig.INPUT_METHOD_DATE) {
			return new PropertyInputDisplayDateImpl();
		} else if (inputMethod == MaterialPropertyConfig.INPUT_METHOD_CHECKBOX) {
			return new PropertyInputDisplayCheckboxImpl();
		} else if (inputMethod == MaterialPropertyConfig.INPUT_METHOD_TEXTAREA) {
			return new PropertyInputDisplayTextareaImpl();
		} 
		return null;
	}

	public PropertyInputVerify getPropertyInputVerify(int fieldType) {
		if (fieldType == MaterialPropertyConfig.FIELD_TYPE_BOOLEAN) {
			return new PropertyInputVerifyBooleanImpl();
		} else if (fieldType == MaterialPropertyConfig.FIELD_TYPE_NUMBER) {
			return new PropertyInputVerifyNumberImpl();
		} else if (fieldType == MaterialPropertyConfig.FIELD_TYPE_DATE) {
			return new PropertyInputVerifyDateImpl();
		}
		return null;
	}

}
