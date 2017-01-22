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

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialImageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialInfoEntityBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.business.MaterialImageBusiness;
import com.lpmas.pdm.business.MaterialInfoBusiness;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.business.UnitInfoBusiness;
import com.lpmas.pdm.config.MaterialImageConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialInfoList
 */
@WebServlet("/pdm/MaterialInfoList.do")
public class MaterialInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialInfoList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.SEARCH)) {
			return;
		}
		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		// 获取页面请求参数
		// 插入查询参数MAP
		HashMap<String, String> condMap = new HashMap<String, String>();
		String materialCode = ParamKit.getParameter(request, "materialNumber", "").trim();
		if (StringKit.isValid(materialCode)) {
			condMap.put("materialNumber", materialCode);
		}
		String materialName = ParamKit.getParameter(request, "materialName", "").trim();
		if (StringKit.isValid(materialName)) {
			condMap.put("materialName", materialName);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		// 从数据库中获取相应的数据
		MaterialInfoBusiness business = new MaterialInfoBusiness();
		PageResultBean<MaterialInfoBean> result = business.getMaterialInfoPageListByMap(condMap, pageBean);
		// 遍历放到放到图片LSIT里
		MaterialImageBusiness materialImageBusiness = new MaterialImageBusiness();
		List<MaterialInfoEntityBean> materialInfoEntityList = new ArrayList<MaterialInfoEntityBean>();
		for (MaterialInfoBean materialInfoBean : result.getRecordList()) {
			MaterialImageBean materialImageBean = materialImageBusiness
					.getMaterialImageByKey(materialInfoBean.getMaterialId(), MaterialImageConfig.IMAGE_TYPE_PIC_1);
			MaterialInfoEntityBean entityBean = new MaterialInfoEntityBean(materialInfoBean, materialImageBean);
			materialInfoEntityList.add(entityBean);
		}
		// 获得物料类型Map
		MaterialTypeBusiness materialInfoBusines = new MaterialTypeBusiness();
		List<MaterialTypeBean> materialTypeList = materialInfoBusines.getMaterialTypeAllList();
		Map<Integer, MaterialTypeBean> materialTypeMap = ListKit.list2Map(materialTypeList, "typeId");
		// 获得单位MAP
		UnitInfoBusiness unitInfoBusiness = new UnitInfoBusiness();
		List<UnitInfoBean> unitInfoList = unitInfoBusiness.getUnitInfoLsit();
		Map<Integer, UnitInfoBean> unitInfoMap = ListKit.list2Map(unitInfoList, "unitCode");

		// 把数据放到页面
		request.setAttribute("UnitInfoMap", unitInfoMap);
		request.setAttribute("MaterialTypeMap", materialTypeMap);
		request.setAttribute("MaterialList", result.getRecordList());
		request.setAttribute("materialInfoEntityList", materialInfoEntityList);
		// 初始化分页数据
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "MaterialInfoList.jsp");
		rd.forward(request, response);

	}

}
