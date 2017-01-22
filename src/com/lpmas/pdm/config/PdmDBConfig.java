package com.lpmas.pdm.config;

import com.lpmas.framework.util.PropertiesKit;

public class PdmDBConfig {

	public static String DB_LINK_PDM_W = PropertiesKit.getBundleProperties(PdmConfig.PDM_PROP_FILE_NAME,
			"DB_LINK_PDM_W");

	public static String DB_LINK_PDM_R = PropertiesKit.getBundleProperties(PdmConfig.PDM_PROP_FILE_NAME,
			"DB_LINK_PDM_R");
}
