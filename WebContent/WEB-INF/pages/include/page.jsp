<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form name="formPage" method="post" action="">
<input type="hidden" name="pageNum" id="pageNum" value=""/>
<%for(String[] condArray :COND_LIST){ %>
<input type="hidden" name="<%=condArray[0] %>" id="<%=condArray[0] %>" value="<%=condArray[1] %>"/><%} %>
</form>
<li class="page_num">
  <%if(PAGE_BEAN.getCurrentPageNumber()>1){ %><em class="first" onclick="javascript:goPage('formPage',1);">&nbsp;</em>
  <em class="pre" onclick="javascript:goPage('formPage',<%=PAGE_BEAN.getPrePageNumber() %>);"> &nbsp;</em><%} %>
       总记录<b><%=PAGE_BEAN.getTotalRecordNumber() %></b>条  每页显示<b><%=PAGE_BEAN.getPageSize() %></b>条 当前第<b><%=PAGE_BEAN.getCurrentPageNumber() %></b>/<%=PAGE_BEAN.getTotalPageNumber() %>页 
  <%if(PAGE_BEAN.getCurrentPageNumber() < PAGE_BEAN.getTotalPageNumber()){ %><em class="next" onclick="javascript:goPage('formPage',<%=PAGE_BEAN.getNextPageNumber() %>);">&nbsp;</em>
  <em class="last" onclick="javascript:goPage('formPage',<%=PAGE_BEAN.getTotalPageNumber() %>);">&nbsp;</em><%} %>
  <input name="goPageNum" id="goPageNum" type="text" class="page_int_txt"/><input name="" type="button" class="page_btn_go" value="GO" onclick="javascript:goInputPage('formPage',<%=PAGE_BEAN.getTotalPageNumber() %>);"/> 
</li>