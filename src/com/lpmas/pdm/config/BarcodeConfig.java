package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class BarcodeConfig {

	public static final String USE_STATUS_DISABLE = "DISABLE";
	public static final String USE_STATUS_ENABLE = "ENABLE";
	public static List<StatusBean<String, String>> BARCODE_USE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> BARCODE_USE_STATUS_MAP = new HashMap<String, String>();

	static {
		initUseStatusList();
		initUseStatusMap();
	}

	private static void initUseStatusList() {
		BARCODE_USE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		BARCODE_USE_STATUS_LIST.add(new StatusBean<String, String>(USE_STATUS_DISABLE, "禁用"));
		BARCODE_USE_STATUS_LIST.add(new StatusBean<String, String>(USE_STATUS_ENABLE, "启用"));
	}

	private static void initUseStatusMap() {
		BARCODE_USE_STATUS_MAP = StatusKit.toMap(BARCODE_USE_STATUS_LIST);
	}
}
