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
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.BarcodeInfoBean;
import com.lpmas.pdm.business.BarcodeInfoBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

@WebServlet("/pdm/BarcodeInfoSelect.do")
public class BarcodeInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BarcodeInfoSelect() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.BARCODE_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		HashMap<String, String> condMap = new HashMap<String, String>();
		String barcode = ParamKit.getParameter(request, "barcode", "").trim();
		if (StringKit.isValid(barcode)) {
			condMap.put("barcode", barcode);
		}
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		int barcodeId = ParamKit.getIntParameter(request, "barcodeId", 0);
		BarcodeInfoBusiness barcodeInfoBusiness = new BarcodeInfoBusiness();
		PageResultBean<BarcodeInfoBean> result = barcodeInfoBusiness.getBarcodeInfoPageListByMap(condMap, pageBean);
		request.setAttribute("BarcodeList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("barcodeId", barcodeId);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "BarcodeInfoSelect.jsp");
		rd.forward(request, response);
	}

}
