package com.lpmas.pdm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductPropertyTypeDao {
	private static Logger log = LoggerFactory.getLogger(ProductPropertyTypeDao.class);

	public int insertProductPropertyType(ProductPropertyTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_property_type ( property_name, property_code, type_id, parent_property_id, info_type, property_type, input_method, input_style, input_desc, field_type, field_format, field_source, field_storage, default_value, is_required, is_modifiable, priority, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyName());
			ps.setString(2, bean.getPropertyCode());
			ps.setInt(3, bean.getTypeId());
			ps.setInt(4, bean.getParentPropertyId());
			ps.setInt(5, bean.getInfoType());
			ps.setInt(6, bean.getPropertyType());
			ps.setInt(7, bean.getInputMethod());
			ps.setString(8, bean.getInputStyle());
			ps.setString(9, bean.getInputDesc());
			ps.setInt(10, bean.getFieldType());
			ps.setString(11, bean.getFieldFormat());
			ps.setString(12, bean.getFieldSource());
			ps.setString(13, bean.getFieldStorage());
			ps.setString(14, bean.getDefaultValue());
			ps.setInt(15, bean.getIsRequired());
			ps.setInt(16, bean.getIsModifiable());
			ps.setInt(17, bean.getPriority());
			ps.setInt(18, bean.getStatus());
			ps.setInt(19, bean.getCreateUser());
			ps.setString(20, bean.getMemo());

			result = db.executePstmtInsert();
		} catch (Exception e) {
			log.error("", e);
			result = -1;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public int updateProductPropertyType(ProductPropertyTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_property_type set property_name = ?, property_code = ?, type_id = ?, parent_property_id = ?, info_type = ?, property_type = ?, input_method = ?, input_style = ?, input_desc = ?, field_type = ?, field_format = ?, field_source = ?, field_storage = ?, default_value = ?, is_required = ?, is_modifiable = ?, priority = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where property_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyName());
			ps.setString(2, bean.getPropertyCode());
			ps.setInt(3, bean.getTypeId());
			ps.setInt(4, bean.getParentPropertyId());
			ps.setInt(5, bean.getInfoType());
			ps.setInt(6, bean.getPropertyType());
			ps.setInt(7, bean.getInputMethod());
			ps.setString(8, bean.getInputStyle());
			ps.setString(9, bean.getInputDesc());
			ps.setInt(10, bean.getFieldType());
			ps.setString(11, bean.getFieldFormat());
			ps.setString(12, bean.getFieldSource());
			ps.setString(13, bean.getFieldStorage());
			ps.setString(14, bean.getDefaultValue());
			ps.setInt(15, bean.getIsRequired());
			ps.setInt(16, bean.getIsModifiable());
			ps.setInt(17, bean.getPriority());
			ps.setInt(18, bean.getStatus());
			ps.setInt(19, bean.getModifyUser());
			ps.setString(20, bean.getMemo());

			ps.setInt(21, bean.getPropertyId());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			result = -1;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public ProductPropertyTypeBean getProductPropertyTypeByKey(int propertyId) {
		ProductPropertyTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_type where property_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, propertyId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductPropertyTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyTypeBean.class);
			}
			rs.close();
		} catch (Exception e) {
			log.error("", e);
			bean = null;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return bean;
	}

	public ProductPropertyTypeBean getProductPropertyTypeByCode(String propertyCode) {
		ProductPropertyTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_type where property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductPropertyTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyTypeBean.class);
			}
			rs.close();
		} catch (Exception e) {
			log.error("", e);
			bean = null;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return bean;
	}
	
	public ProductPropertyTypeBean getProductPropertyTypeByParentId(int parentId){
		ProductPropertyTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_type where parent_property_id = ? and status =1";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, parentId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductPropertyTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyTypeBean.class);
			}
			rs.close();
		} catch (Exception e) {
			log.error("", e);
			bean = null;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return bean;
	}

	public List<ProductPropertyTypeBean> getProductPropertyTypeListByMap(HashMap<String, String> condMap) {
		List<ProductPropertyTypeBean> list = new ArrayList<ProductPropertyTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_type";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String infoType = condMap.get("infoType");
			if (StringKit.isValid(infoType)) {
				condList.add("info_type = ?");
				paramList.add(infoType);
			}
			String propertyType = condMap.get("propertyType");
			if (StringKit.isValid(propertyType)) {
				condList.add("property_type = ?");
				paramList.add(propertyType);
			}
			String typeId = condMap.get("typeId");
			if (StringKit.isValid(typeId)) {
				condList.add("type_id = ?");
				paramList.add(typeId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by priority asc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, ProductPropertyTypeBean.class, db);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return list;
	}
	
	public PageResultBean<ProductPropertyTypeBean> getProductPropertyTypePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ProductPropertyTypeBean> result = new PageResultBean<ProductPropertyTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_type";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String parentId = condMap.get("parentPropertyId");
			if (StringKit.isValid(parentId)) {
				condList.add("parent_property_id = ?");
				paramList.add(parentId);
			}
			String propertyCode = condMap.get("propertyCode");
			if (StringKit.isValid(propertyCode)) {
				condList.add("property_code like ?");
				paramList.add("%" + propertyCode + "%");
			}
			String propertyName = condMap.get("propertyName");
			if (StringKit.isValid(propertyName)) {
				condList.add("property_name like ?");
				paramList.add("%" + propertyName + "%");
			}
			String propertyType = condMap.get("propertyType");
			if (StringKit.isValid(propertyType)) {
				condList.add("property_type = ?");
				paramList.add(propertyType);
			}
			String infoType = condMap.get("infoType");
			if (StringKit.isValid(infoType)) {
				condList.add("info_type =?");
				paramList.add(infoType);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String productType = condMap.get("productType");
			if (StringKit.isValid(productType)) {
				condList.add("type_id = ?");
				paramList.add(productType);
			}

			String orderQuery = "order by property_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductPropertyTypeBean.class,
					pageBean, db);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

}
