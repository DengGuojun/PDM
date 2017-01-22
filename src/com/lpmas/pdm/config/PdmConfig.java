package com.lpmas.pdm.config;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.PropertiesKit;

public class PdmConfig {

	// appId
	public static final String APP_ID = "PDM";

	// 根节点父级ID
	public static final int ROOT_PARENT_ID = 0;

	public static final String PDM_PROP_FILE_NAME = Constants.PROP_FILE_PATH + "/pdm_config";

	public static final Integer DEFAULT_PAGE_NUM = 1;
	public static final Integer DEFAULT_PAGE_SIZE = 20;

	public static final String ERROR_PAGE = Constants.PAGE_PATH + "common/error_page.jsp";
	public static final String PAGE_PATH = Constants.PAGE_PATH + "pdm/";

	public static final String TEMPLATE_PATH = PropertiesKit.getBundleProperties(PDM_PROP_FILE_NAME, "TEMPLATE_PATH");

}
