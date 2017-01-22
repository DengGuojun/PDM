package com.lpmas.pdm.factory;

import com.lpmas.pdm.business.MultiplePropertyInputDisplay;
import com.lpmas.pdm.business.impl.MultiplePropertyInputDisplayOutsourceImpl;
import com.lpmas.pdm.config.ProductPropertyConfig;

public class ProductMultiplePropertyFactory extends ProductPropertyFactory {
	public MultiplePropertyInputDisplay getMultiplePropertyInputDisplay(int inputMethod) {
		if (inputMethod == ProductPropertyConfig.INPUT_METHOD_BOX) {
			return new MultiplePropertyInputDisplayOutsourceImpl();
		}
		return null;
	}

}
