package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductMultiplePropertyBean;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.business.ProductInfoBusiness;
import com.lpmas.pdm.business.ProductItemBusiness;
import com.lpmas.pdm.business.ProductMultiplePropertyBusiness;
import com.lpmas.pdm.business.ProductPropertyBusiness;
import com.lpmas.pdm.business.ProductPropertyTypeBusiness;
import com.lpmas.pdm.business.UnitInfoBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductConfig;
import com.lpmas.pdm.config.ProductPropertyConfig;

/**
 * Servlet implementation class ProductInfoManage
 */
@WebServlet("/pdm/ProductItemManage.do")
public class ProductItemManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductItemManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductItemManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int itemId = ParamKit.getIntParameter(request, "itemId", 0);
		int productId = ParamKit.getIntParameter(request, "productId", 0);
		ProductItemBusiness business = new ProductItemBusiness();
		ProductItemBean bean = new ProductItemBean();
		if (itemId > 0) {
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getProductItemByKey(itemId);
			productId = bean.getProductId();
		} else {
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.CREATE)) {
				return;
			}
			bean.setProductId(productId);
			bean.setStatus(Constants.STATUS_VALID);
		}
		ProductInfoBusiness productInfoBusiness = new ProductInfoBusiness();
		ProductInfoBean productInfoBean = productInfoBusiness.getProductInfoByKey(productId);
		if (productInfoBean == null) {
			HttpResponseKit.alertMessage(response, "商品信息ID错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		// 获取使用状态列表
		List<StatusBean<String, String>> productUseStatusList = ProductConfig.PRODUCT_USE_STATUS_LIST;
		// 获取计量单位
		if (itemId > 0) {
			UnitInfoBusiness unitInfoBusiness = new UnitInfoBusiness();
			UnitInfoBean unitInfoBean = unitInfoBusiness.getUnitInfoByCode(bean.getUnit());
			request.setAttribute("UnitInfoBean", unitInfoBean);
		}

		// 获取商品动态基本属性类型
		ProductPropertyTypeBusiness propertyTypeBusiness = new ProductPropertyTypeBusiness();
		List<ProductPropertyTypeBean> propertyTypeList = propertyTypeBusiness.getProductPropertyTypeListByType(
				InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, ProductPropertyConfig.PROP_TYPE_MAIN,
				productInfoBean.getTypeId2());
		Map<String, ProductPropertyTypeBean> subPropertyTypeMap = new HashMap<String, ProductPropertyTypeBean>();
		for (ProductPropertyTypeBean typeBean : propertyTypeList) {
			ProductPropertyTypeBean subPropertyBean = propertyTypeBusiness.getProductPropertyTypeByParentId(typeBean
					.getPropertyId());
			if (subPropertyBean != null) {
				subPropertyTypeMap.put(typeBean.getPropertyCode(), subPropertyBean);
			}
		}
		List<ProductPropertyBean> propertyList = new ArrayList<ProductPropertyBean>();
		Map<String, ProductPropertyBean> productPropertyMap = new HashMap<String, ProductPropertyBean>();
		List<ProductMultiplePropertyBean> multiplePropertyList = new ArrayList<ProductMultiplePropertyBean>();
		Map<String, List<ProductMultiplePropertyBean>> productMultiptlePropertyMap = new HashMap<String, List<ProductMultiplePropertyBean>>();
		if (productId > 0) {
			// 获取商品动态基本属性
			ProductPropertyBusiness productPropertyBusiness = new ProductPropertyBusiness();
			propertyList = productPropertyBusiness.getProductPropertyListByInfoIdAndInfoType(itemId,
					InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
			if (!propertyList.isEmpty()) {
				productPropertyMap = ListKit.list2Map(propertyList, "propertyCode");
			}

			// 获取商品动态多重基本属性
			ProductMultiplePropertyBusiness productMultiplePropertyBusiness = new ProductMultiplePropertyBusiness();
			multiplePropertyList = productMultiplePropertyBusiness.getProductPropertyListByInfoIdAndInfoType(itemId,
					InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
			if (!multiplePropertyList.isEmpty()) {
				for (ProductMultiplePropertyBean multiplePropertyBean : multiplePropertyList) {
					List<ProductMultiplePropertyBean> list = new ArrayList<ProductMultiplePropertyBean>();
					if (productMultiptlePropertyMap.containsKey(multiplePropertyBean.getPropertyCode())) {
						list = productMultiptlePropertyMap.get(multiplePropertyBean.getPropertyCode());
					}
					list.add(multiplePropertyBean);
					productMultiptlePropertyMap.put(multiplePropertyBean.getPropertyCode(), list);
				}
			}
		} else {
			bean.setStatus(Constants.STATUS_VALID);
		}

		request.setAttribute("ProductUseStatusList", productUseStatusList);
		request.setAttribute("ProductItem", bean);
		request.setAttribute("ProductInfo", productInfoBean);
		request.setAttribute("ProductPropertyTypeList", propertyTypeList);
		request.setAttribute("SubPropertyTypeMap", subPropertyTypeMap);
		request.setAttribute("ProductPropertyList", propertyList);
		request.setAttribute("ProductPropertyMap", productPropertyMap);
		request.setAttribute("ProductMultiplePropertyList", multiplePropertyList);
		request.setAttribute("ProductMultiplePropertyMap", productMultiptlePropertyMap);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(
				PdmConfig.PAGE_PATH + "ProductItemManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int userId = adminUserHelper.getAdminUserId();
		ProductItemBean bean = new ProductItemBean();
		try {
			bean = BeanKit.request2Bean(request, ProductItemBean.class);
			ProductItemBusiness business = new ProductItemBusiness();
			ProductInfoBusiness productInfoBusiness = new ProductInfoBusiness();
			ProductInfoBean productInfoBean = productInfoBusiness.getProductInfoByKey(bean.getProductId());
			// 生成商品项编码
			if (bean.getItemId() == 0) {
				String itemNumber = business.generateItemNumber(bean, productInfoBean);
				if (!StringKit.isValid(itemNumber)) {
					HttpResponseKit.alertMessage(response, "商品项编码规则缺失", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				ProductItemBean existsBean = business.getProductItemByNumber(itemNumber) ;
				if (existsBean != null && existsBean.getStatus() == Constants.STATUS_VALID) {
					HttpResponseKit.alertMessage(response, "已存在相同编码的商品项", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				bean.setItemNumber(itemNumber);
			}
			// 判断商品属性值是否正确
			ReturnMessageBean messageBean = business.verifyProductItem(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			ProductPropertyTypeBusiness propertyTypeBusiness = new ProductPropertyTypeBusiness();
			List<ProductPropertyTypeBean> propertyTypeList = propertyTypeBusiness.getProductPropertyTypeListByType(
					InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, ProductPropertyConfig.PROP_TYPE_MAIN,
					productInfoBean.getTypeId2());
			Map<Integer, String> parameters = new HashMap<Integer, String>();
			Map<Integer, String> subParameters = new HashMap<Integer, String>();
			Map<Integer, String> multiParameters = new HashMap<Integer, String>();
			for (ProductPropertyTypeBean propertyTypeBean : propertyTypeList) {
				if (!StringKit.isValid(propertyTypeBean.getFieldStorage())) {
					if (propertyTypeBean.getParentPropertyId() == PdmConfig.ROOT_PARENT_ID) {
						parameters.put(propertyTypeBean.getPropertyId(),
								ParamKit.getParameter(request, "PROPERTY_" + propertyTypeBean.getPropertyCode(), ""));
					} else {
						String subProperty = ParamKit.getParameter(request,
								"SUB_PROPERTY_" + propertyTypeBean.getPropertyCode());
						if (StringKit.isValid(subProperty)) {
							subParameters.put(propertyTypeBean.getPropertyId(), subProperty);
						}
					}
				} else {
					multiParameters.put(propertyTypeBean.getPropertyId(),
							ParamKit.getParameter(request, "MULTI_PROPERTY_" + propertyTypeBean.getPropertyCode(), ""));
				}

			}
			ProductPropertyBusiness propertyBusiness = new ProductPropertyBusiness();
			ProductMultiplePropertyBusiness multiplePropertyBusiness = new ProductMultiplePropertyBusiness();
			// 校验商品属性值格式
			messageBean = propertyBusiness.verifyProductInfoProperty(parameters);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 校验商品子属性值格式
			messageBean = propertyBusiness.verifyProductInfoProperty(subParameters);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 校验商品多重属性值格式
			messageBean = multiplePropertyBusiness.verifyProductInfoProperty(multiParameters);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			if (bean.getItemId() > 0) {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(userId);
				result = business.updateProductItem(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(userId);
				result = business.addProductItem(bean);
				bean.setItemId(result);// 获取新增后带ID的商品
			}

			if (result > 0) {
				// 更新商品动态基本属性类型
				for (ProductPropertyTypeBean propertyType : propertyTypeList) {
					if (!StringKit.isValid(propertyType.getFieldStorage())) {
						ProductPropertyBean propertyBean = new ProductPropertyBean();
						propertyBean.setInfoId(bean.getItemId());
						propertyBean.setInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
						propertyBean.setPropertyCode(propertyType.getPropertyCode());
						propertyBean.setPropertyValue1(ParamKit.getParameter(request,
								"PROPERTY_" + propertyType.getPropertyCode(), ""));
						propertyBean.setPropertyValue2(ParamKit.getParameter(request,
								"SUB_PROPERTY_" + propertyType.getPropertyCode(), ""));
						// 判断是否存在
						if (propertyBusiness.getProductPropertyByKey(bean.getItemId(),
								InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, propertyType.getPropertyCode()) != null) {
							// 存在，更新
							propertyBean.setModifyUser(userId);
							result = propertyBusiness.updateProductProperty(propertyBean);
						} else {
							// 不存在，创建
							if (StringKit.isValid(propertyBean.getPropertyValue1().trim())) {
								// 只有当PROPERTYVALUE1不是空串时插入数据库
								propertyBean.setCreateUser(userId);
								result = propertyBusiness.addProductProperty(propertyBean);
							}
						}
					} else {
						String multipleValue = ParamKit.getParameter(request,
								"MULTIPLE_PROPERTY_" + propertyType.getPropertyCode(), "");
						List<ProductMultiplePropertyBean> existsMultiplePropertyList = multiplePropertyBusiness
								.getProductPropertyListByInfoIdAndInfoType(bean.getItemId(),
										InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
						if (result != -1) {
							// 按约定的固定格式分隔
							String[] multiValueArray = multipleValue.split(";");
							for (String multProperty : multiValueArray) {
								if (StringKit.isValid(multProperty)) {
									ProductMultiplePropertyBean multiplePropertyBean = new ProductMultiplePropertyBean();
									multiplePropertyBean.setInfoId(bean.getItemId());
									multiplePropertyBean.setInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
									multiplePropertyBean.setPropertyCode(propertyType.getPropertyCode());
									String[] values = multProperty.split(",");
									int index = 1;
									String completeValue = "";
									for (String value : values) {
										if (index == 1) {
											multiplePropertyBean.setPropertyValue1(value);
										} else if (index == 2) {
											multiplePropertyBean.setPropertyValue2(value);
										} else {
											multiplePropertyBean.setPropertyValue3(value);
										}
										index++;
										completeValue += value;
									}
									// 检查是否新值，如果是新值才插入数据库
									boolean isNewValue = true;
									for (ProductMultiplePropertyBean existBean : existsMultiplePropertyList) {
										String existValue = existBean.getPropertyValue1()
												+ existBean.getPropertyValue2() + existBean.getPropertyValue3();
										if (existValue.equals(completeValue)) {
											isNewValue = false;
											existsMultiplePropertyList.remove(existBean);// 删除相同的，剩下的就是被前台删除的
											break;
										}
									}
									if (isNewValue) {
										multiplePropertyBean.setCreateUser(userId);
										if (result != -1) {
											result = multiplePropertyBusiness
													.addProductMultipleProperty(multiplePropertyBean);
										}
									}
								}
							}
							// 逻辑删除
							for (ProductMultiplePropertyBean delBean : existsMultiplePropertyList) {
								delBean.setModifyUser(userId);
								multiplePropertyBusiness.deleteProductMultipleProperty(delBean);
							}
						} else {
							HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
							return;
						}
					}
				}
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/ProductItemList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
