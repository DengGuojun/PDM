package com.lpmas.pdm.business;

import java.util.List;

import com.lpmas.pdm.bean.ProductMultiplePropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;

public abstract class MultiplePropertyInputDisplay {
	public abstract String getMultiplePropertyInputStr(ProductPropertyTypeBean productPropertyTypeBean,
			List<ProductMultiplePropertyBean> list);
}
