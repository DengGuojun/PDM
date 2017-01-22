package com.lpmas.pdm.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.pdm.config.PropertyDispalyConfig;

public abstract class PropertyInputDisplay {
	
	private static Logger log = LoggerFactory.getLogger(PropertyInputDisplay.class);
	
	public abstract String getPropertyInputStr(Object PropertyTypeBean,
			Object PropertyBean, boolean isSubType);
	
	protected Boolean isSupportedBeanType(Object bean)
	{
		List<String> supprotedPropertyBeanType = PropertyDispalyConfig.SUPPORTED_PROPERTY_BEAN_TYPE;
		String ClassName = bean.getClass().getName();
		String[] fullClassNameArray = ClassName.split("[.]");
		String beanClassName = fullClassNameArray[fullClassNameArray.length-1];
		
		return supprotedPropertyBeanType.contains(beanClassName);
	}
	
	protected Object invoke(Object bean,String methodName)
	{
		Object result = "";
		try {
			Method method = bean.getClass().getMethod(methodName);
			result = method.invoke(bean);
		} catch(Exception e){
			log.error("",e);
			return result;
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> List<T> getPropertyOptions(String name,Object propertyTypeBean,Class<T> optionBeanClazz,Class businessClazz) throws Exception
	{
		List<T> result = new ArrayList<T>();
		Object business = businessClazz.newInstance();
		Method getPropertyOptionListByPropertyId = businessClazz.getMethod("get"+name+"PropertyOptionListByPropertyId", int.class);
		int propertyId = (int)invoke(propertyTypeBean,"getPropertyId");
		result = (List<T>) getPropertyOptionListByPropertyId.invoke(business, propertyId);
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> List<T> getPropertyOptions(Object propertyTypeBean)
	{
		String name = propertyTypeBean.getClass().getName().trim().replace("PropertyTypeBean", "");
		String[] tempArray = name.split("[.]");
		name = tempArray[tempArray.length-1];
		try {
			Class businessClazz = Class.forName(("com.lpmas.pdm.business."+name+"PropertyOptionBusiness").trim());
			Class optionBeanClazz = Class.forName(("com.lpmas.pdm.bean."+name+"PropertyOptionBean").trim());
			return getPropertyOptions(name,propertyTypeBean,optionBeanClazz,businessClazz);
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}
}
