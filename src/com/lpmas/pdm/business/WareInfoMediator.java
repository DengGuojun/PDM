package com.lpmas.pdm.business;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.lpmas.framework.util.MapKit;
import com.lpmas.pdm.bean.WareInfoBean;
import com.lpmas.pdm.client.cache.WareInfoClientCache;

public class WareInfoMediator {

	private WareInfoClientCache wareInfoClientCache;
	private Map<String, String> wareNameMap;
	private Map<String, String> wareTypeNameMap;
	private Map<String, String> wareUnitNameMap;
	private Map<String, String> wareSpecificationMap;
	private Map<String, String> wareNumberMap;
	private static String MAP_KEY_PREFIX = "WARE_INFO";

	public WareInfoMediator() {
		wareInfoClientCache = new WareInfoClientCache();
		wareNameMap = new HashMap<String, String>();
		wareTypeNameMap = new HashMap<String, String>();
		wareUnitNameMap = new HashMap<String, String>();
		wareSpecificationMap = new HashMap<String, String>();
		wareNumberMap = new HashMap<String, String>();
	}

	public String getMapKey(int wareType, int wareId) {
		return MessageFormat.format("{0}_{1}_{2}", MAP_KEY_PREFIX, wareType, wareId);
	}

	public String getMapKey(int wareType, String wareNumber) {
		return MessageFormat.format("{0}_{1}_{2}", MAP_KEY_PREFIX, wareType, wareNumber);
	}

	public WareInfoClientCache getWareInfoClientCache() {
		return wareInfoClientCache;
	}

	public Map<String, String> getWareNameMap() {
		return wareNameMap;
	}

	public Map<String, String> getWareTypeNameMap() {
		return wareTypeNameMap;
	}

	public Map<String, String> getWareUnitNameMap() {
		return wareUnitNameMap;
	}

	public Map<String, String> getWareSpecificationMap() {
		return wareSpecificationMap;
	}

	public Map<String, String> getWareNumberMap() {
		return wareNumberMap;
	}

	public String getWareNameByKey(int wareType, int wareId) {
		return getValueFromMapByKey(wareNameMap, wareType, wareId);
	}

	public String getWareTypeNameByKey(int wareType, int wareId) {
		return getValueFromMapByKey(wareTypeNameMap, wareType, wareId);
	}

	public String getWareUnitNameByKey(int wareType, int wareId) {
		return getValueFromMapByKey(wareUnitNameMap, wareType, wareId);
	}

	public String getWareSpecificationByKey(int wareType, int wareId) {
		return getValueFromMapByKey(wareSpecificationMap, wareType, wareId);
	}

	public String getWareNumberByKey(int wareType, int wareId) {
		return getValueFromMapByKey(wareNumberMap, wareType, wareId);
	}

	public String getWareNameByNumber(int wareType, String wareNumber) {
		return getValueFromMapByNumber(wareNameMap, wareType, wareNumber);
	}

	public String getWareTypeNameByNumber(int wareType, String wareNumber) {
		return getValueFromMapByNumber(wareTypeNameMap, wareType, wareNumber);
	}

	public String getWareUnitNameByNumber(int wareType, String wareNumber) {
		return getValueFromMapByNumber(wareUnitNameMap, wareType, wareNumber);
	}

	public String getWareSpecificationByNumber(int wareType, String wareNumber) {
		return getValueFromMapByNumber(wareSpecificationMap, wareType, wareNumber);
	}

	public String getWareNumberByNumber(int wareType, String wareNumber) {
		return getValueFromMapByNumber(wareNumberMap, wareType, wareNumber);
	}

	private void setMapVaule(String key, WareInfoBean wareInfoBean) {
		wareNameMap.put(key, wareInfoBean.getWareName());
		wareTypeNameMap.put(key, wareInfoBean.getTypeName());
		wareUnitNameMap.put(key, wareInfoBean.getUnitName());
		wareSpecificationMap.put(key, wareInfoBean.getSpecification());
		wareNumberMap.put(key, wareInfoBean.getWareNumber());
	}

	private String getValueFromMapByKey(Map<String, String> map, int wareType, int wareId) {
		String key = getMapKey(wareType, wareId);
		String result = map.get(key);
		if (result == null) {
			WareInfoBean wareInfoBean = wareInfoClientCache.getWareInfoByKey(wareType, wareId);
			if (wareInfoBean != null) {
				setMapVaule(key, wareInfoBean);
			}
		}
		return MapKit.getValueFromMap(key, map);
	}

	private String getValueFromMapByNumber(Map<String, String> map, int wareType, String wareNumber) {
		String key = getMapKey(wareType, wareNumber);
		String result = map.get(key);
		if (result == null) {
			WareInfoBean wareInfoBean = wareInfoClientCache.getWareInfoByKey(wareType, wareNumber);
			if (wareInfoBean != null) {
				setMapVaule(key, wareInfoBean);
			}
		}
		return MapKit.getValueFromMap(key, map);
	}
}
