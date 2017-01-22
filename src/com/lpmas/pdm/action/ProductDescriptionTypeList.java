package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductDescriptionTypeBean;
import com.lpmas.pdm.business.ProductDescriptionTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;


@WebServlet("/pdm/ProductDescriptionTypeList.do")
public class ProductDescriptionTypeList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductDescriptionTypeList() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminHelper = new AdminUserHelper(request, response);
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		// 处理查询条件
		HashMap<String, String> condMap = new HashMap<String, String>();
		String typeId = ParamKit.getParameter(request, "typeId","").trim();
		if (StringKit.isValid(typeId)) {
			condMap.put("typeId", typeId);
		}
		String typeCode = ParamKit.getParameter(request, "typeCode","").trim();
		if (StringKit.isValid(typeCode)) {
			condMap.put("typeCode", typeCode);
		}
		String typeName = ParamKit.getParameter(request, "typeName","").trim();
		if (StringKit.isValid(typeName)) {
			condMap.put("typeName", typeName);
		}
		String infoType = ParamKit.getParameter(request, "infoType","").trim();
		if (StringKit.isValid(infoType)) {
			condMap.put("infoType", infoType);
		}
		String status = ParamKit.getParameter(request, "status",Constants.STATUS_VALID+"");
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}
		ProductDescriptionTypeBusiness business = new ProductDescriptionTypeBusiness();
		PageResultBean<ProductDescriptionTypeBean> result = business.getProductDescriptionTypePageListByMap(condMap,
				pageBean);

		request.setAttribute("AdminUserHelper", adminHelper);
		request.setAttribute("ProductDescriptionTypeList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/ProductDescriptionTypeList.jsp");
		rd.forward(request, response);
	}

}
