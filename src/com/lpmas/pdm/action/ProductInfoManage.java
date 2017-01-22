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
import com.lpmas.pdm.bean.BrandInfoBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductMultiplePropertyBean;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.BrandInfoBusiness;
import com.lpmas.pdm.business.ProductInfoBusiness;
import com.lpmas.pdm.business.ProductMultiplePropertyBusiness;
import com.lpmas.pdm.business.ProductPropertyBusiness;
import com.lpmas.pdm.business.ProductPropertyTypeBusiness;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductConfig;
import com.lpmas.pdm.config.ProductPropertyConfig;

/**
 * Servlet implementation class ProductInfoManage
 */
@WebServlet("/pdm/ProductInfoManage.do")
public class ProductInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductInfoManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int productId = ParamKit.getIntParameter(request, "productId", 0);
		int typeId1 = ParamKit.getIntParameter(request, "typeId1", 0);
		int typeId2 = ParamKit.getIntParameter(request, "typeId2", 0);
		String page = "";
		ProductInfoBusiness business = new ProductInfoBusiness();
		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		if (typeId2 <= 0 && productId == 0) {
			HttpResponseKit.alertMessage(response, "商品类型选择有误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} else {
			ProductInfoBean bean = new ProductInfoBean();
			if (productId > 0) {
				if (readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_INFO, OperationConfig.SEARCH)) {
					return;
				}
				if (!readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean = business.getProductInfoByKey(productId);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setTypeId1(typeId1);
				bean.setTypeId2(typeId2);
			}
			// 获取品牌类型分类
			BrandInfoBusiness brandInfoBusiness = new BrandInfoBusiness();
			List<BrandInfoBean> brandList = brandInfoBusiness.getBrandInfoAllList();
			request.setAttribute("BrandList", brandList);
			BrandInfoBean brandInfoBean = new BrandInfoBean();
			// 获取质量等级列表
			List<StatusBean<String, String>> qualityLevelList = ProductConfig.PRODUCT_QUALITY_LEVEL_LIST;
			Map<String, String> qualityLevelMap = ProductConfig.PRODUCT_QUALITY_LEVEL_MAP;
			// 获取商品动态基本属性类型
			ProductPropertyTypeBusiness propertyTypeBusiness = new ProductPropertyTypeBusiness();
			List<ProductPropertyTypeBean> propertyTypeList = propertyTypeBusiness.getProductPropertyTypeListByType(
					InfoTypeConfig.INFO_TYPE_PRODUCT, ProductPropertyConfig.PROP_TYPE_MAIN, bean.getTypeId2());
			Map<String, ProductPropertyTypeBean> subPropertyTypeMap = new HashMap<String, ProductPropertyTypeBean>();
			for (ProductPropertyTypeBean typeBean : propertyTypeList) {
				ProductPropertyTypeBean subPropertyBean = propertyTypeBusiness
						.getProductPropertyTypeByParentId(typeBean.getPropertyId());
				if (subPropertyBean != null) {
					subPropertyTypeMap.put(typeBean.getPropertyCode(), subPropertyBean);
				}
			}
			List<ProductPropertyBean> propertyList = new ArrayList<ProductPropertyBean>();
			Map<String, ProductPropertyBean> productPropertyMap = new HashMap<String, ProductPropertyBean>();
			List<ProductMultiplePropertyBean> multiplePropertyList = new ArrayList<ProductMultiplePropertyBean>();
			Map<String, List<ProductMultiplePropertyBean>> productMultiptlePropertyMap = new HashMap<String, List<ProductMultiplePropertyBean>>();
			if (productId > 0) {
				// 获取品牌名称
				brandInfoBean = brandInfoBusiness.getBrandInfoByKey(bean.getBrandId());
				// 获取商品动态基本属性
				ProductPropertyBusiness productPropertyBusiness = new ProductPropertyBusiness();
				propertyList = productPropertyBusiness.getProductPropertyListByInfoIdAndInfoType(productId,
						InfoTypeConfig.INFO_TYPE_PRODUCT);
				if (!propertyList.isEmpty()) {
					productPropertyMap = ListKit.list2Map(propertyList, "propertyCode");
				}

				// 获取商品动态多重基本属性
				ProductMultiplePropertyBusiness productMultiplePropertyBusiness = new ProductMultiplePropertyBusiness();
				multiplePropertyList = productMultiplePropertyBusiness.getProductPropertyListByInfoIdAndInfoType(
						productId, InfoTypeConfig.INFO_TYPE_PRODUCT);
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
			ProductTypeBean typeBean1 = productTypeBusiness.getProductTypeByKey(bean.getTypeId1());
			ProductTypeBean typeBean2 = productTypeBusiness.getProductTypeByKey(bean.getTypeId2());
			if (typeBean1 == null || typeBean2 == null) {
				HttpResponseKit.alertMessage(response, "商品类型选择有误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			request.setAttribute("ProductInfo", bean);
			request.setAttribute("BrandInfoBean", brandInfoBean);
			request.setAttribute("ProductTypeBean1", typeBean1);
			request.setAttribute("ProductTypeBean2", typeBean2);
			request.setAttribute("QualityLevelList", qualityLevelList);
			request.setAttribute("QualityLevelMap", qualityLevelMap);
			request.setAttribute("ProductPropertyTypeList", propertyTypeList);
			request.setAttribute("SubPropertyTypeMap", subPropertyTypeMap);
			request.setAttribute("ProductPropertyList", propertyList);
			request.setAttribute("ProductPropertyMap", productPropertyMap);
			request.setAttribute("ProductMultiplePropertyList", multiplePropertyList);
			request.setAttribute("ProductMultiplePropertyMap", productMultiptlePropertyMap);
			page = "ProductInfoManage.jsp";
		}

		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(PdmConfig.PAGE_PATH + page);
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
		ProductInfoBean bean = new ProductInfoBean();
		try {
			bean = BeanKit.request2Bean(request, ProductInfoBean.class);
			ProductInfoBusiness business = new ProductInfoBusiness();
			// 生成商品编码
			if (bean.getProductId() == 0) {
				String productCode = ParamKit.getParameter(request, "PROPERTY_PRODUCT_CODE", "");
				String sellingType = ParamKit.getParameter(request, "PROPERTY_SELLING_TYPE", "");
				String version = ParamKit.getParameter(request, "PROPERTY_VERSION", "");
				if(!StringKit.isValid(productCode)){
					HttpResponseKit.alertMessage(response, "商品品种必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				if(!StringKit.isValid(sellingType)){
					HttpResponseKit.alertMessage(response, "销售类型必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				if(!StringKit.isValid(version)){
					HttpResponseKit.alertMessage(response, "版本必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				String numberRule = business.generateProductNumber(bean, productCode, sellingType, version);
				if (!StringKit.isValid(numberRule)) {
					HttpResponseKit.alertMessage(response, "商品编码规则缺失", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				bean.setProductNumber(numberRule);
			}

			// 判断商品属性值是否正确
			ReturnMessageBean messageBean = business.verifyProductInfo(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			ProductPropertyTypeBusiness propertyTypeBusiness = new ProductPropertyTypeBusiness();
			List<ProductPropertyTypeBean> propertyTypeList = propertyTypeBusiness.getProductPropertyTypeListByType(
					InfoTypeConfig.INFO_TYPE_PRODUCT, ProductPropertyConfig.PROP_TYPE_MAIN, bean.getTypeId2());
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
					multiParameters.put(propertyTypeBean.getPropertyId(), ParamKit.getParameter(request,
							"MULTIPLE_PROPERTY_" + propertyTypeBean.getPropertyCode(), ""));
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
			if (bean.getProductId() > 0) {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(userId);
				result = business.updateProductInfo(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(userId);
				result = business.addProductInfo(bean);
				bean.setProductId(result);// 获取新增后带ID的商品
			}

			if (result > 0) {
				// 更新商品动态基本属性类型
				for (ProductPropertyTypeBean propertyType : propertyTypeList) {
					if (!StringKit.isValid(propertyType.getFieldStorage())) {
						ProductPropertyBean propertyBean = new ProductPropertyBean();
						propertyBean.setInfoId(bean.getProductId());
						propertyBean.setInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT);
						propertyBean.setPropertyCode(propertyType.getPropertyCode());
						propertyBean.setPropertyValue1(ParamKit.getParameter(request,
								"PROPERTY_" + propertyType.getPropertyCode(), ""));
						propertyBean.setPropertyValue2(ParamKit.getParameter(request,
								"SUB_PROPERTY_" + propertyType.getPropertyCode(), ""));
						// 判断是否存在
						if (propertyBusiness.getProductPropertyByKey(bean.getProductId(),
								InfoTypeConfig.INFO_TYPE_PRODUCT, propertyType.getPropertyCode()) != null) {
							// 存在更新
							propertyBean.setModifyUser(userId);
							result = propertyBusiness.updateProductProperty(propertyBean);
						} else {
							// 新建
							// 新建前应该判断PROPERTYVALUE1是否为空串
							if (StringKit.isValid(propertyBean.getPropertyValue1().trim())) {
								// 只有当PROPERTYVALUE1不是空串时插入数据库
								propertyBean.setCreateUser(userId);
								result = propertyBusiness.addProductProperty(propertyBean);
							}
						}
					} else {
						if (result != -1) {
							String multipleValue = ParamKit.getParameter(request,
									"MULTIPLE_PROPERTY_" + propertyType.getPropertyCode(), "");
							List<ProductMultiplePropertyBean> existsMultiplePropertyList = multiplePropertyBusiness
									.getProductPropertyListByInfoIdAndInfoType(bean.getProductId(),
											InfoTypeConfig.INFO_TYPE_PRODUCT);
							// 按约定的固定格式分隔
							String[] multiValueArray = multipleValue.split(";");
							for (String multProperty : multiValueArray) {
								if (StringKit.isValid(multProperty)) {
									ProductMultiplePropertyBean multiplePropertyBean = new ProductMultiplePropertyBean();
									multiplePropertyBean.setInfoId(bean.getProductId());
									multiplePropertyBean.setInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT);
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
								multiplePropertyBusiness.deleteProductMultipleProperty(delBean);
							}
						} else {
							HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
							return;
						}
					}
				}
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/ProductInfoList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
