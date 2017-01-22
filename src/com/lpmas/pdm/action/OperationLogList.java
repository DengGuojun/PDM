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
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.log.bean.DataLogBean;
import com.lpmas.log.client.LogServiceClient;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.system.client.cache.SysApplicationInfoClientCache;

@WebServlet("/pdm/OperationLogList.do")
public class OperationLogList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OperationLogList() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.LOG_INFO, OperationConfig.SEARCH)) {
			return;
		}
		SysApplicationInfoClientCache sysCache = new SysApplicationInfoClientCache();
		HashMap<String, String> condMap = new HashMap<String, String>();
		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		condMap = ParamKit.getParameterMap(request, "infoType,field1,begDateTime,endDateTime", "");
		// 获取页面请求参数
		condMap.put("infoId1", "");
		condMap.put("appId", sysCache.getSysApplicationIdByCode(PdmConfig.APP_ID) + "");

		// 查询
		LogServiceClient client = new LogServiceClient();
		PageResultBean<DataLogBean> result = client.getDataLogPageListByMap(condMap, pageBean);
		// 装载参数
		request.setAttribute("DataLogList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "OperationLogList.jsp");
		rd.forward(request, response);
	}

}
