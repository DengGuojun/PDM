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
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class MaterialPropertyTypeDao {
	private static Logger log = LoggerFactory.getLogger(MaterialPropertyTypeDao.class);

	public int insertMaterialPropertyType(MaterialPropertyTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into material_property_type ( property_name, property_code, material_type_id, parent_property_id, property_type, input_method, input_style, input_desc, field_type, field_format, field_source, default_value, is_required, is_modifiable, priority, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyName());
			ps.setString(2, bean.getPropertyCode());
			ps.setInt(3, bean.getMaterialTypeId());
			ps.setInt(4, bean.getParentPropertyId());
			ps.setInt(5, bean.getPropertyType());
			ps.setInt(6, bean.getInputMethod());
			ps.setString(7, bean.getInputStyle());
			ps.setString(8, bean.getInputDesc());
			ps.setInt(9, bean.getFieldType());
			ps.setString(10, bean.getFieldFormat());
			ps.setString(11, bean.getFieldSource());
			ps.setString(12, bean.getDefaultValue());
			ps.setInt(13, bean.getIsRequired());
			ps.setInt(14, bean.getIsModifiable());
			ps.setInt(15, bean.getPriority());
			ps.setInt(16, bean.getStatus());
			ps.setInt(17, bean.getCreateUser());
			ps.setString(18, bean.getMemo());

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

	public int updateMaterialPropertyType(MaterialPropertyTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update material_property_type set property_name = ?, property_code = ?, material_type_id = ?, parent_property_id = ?, property_type = ?, input_method = ?, input_style = ?, input_desc = ?, field_type = ?, field_format = ?, field_source = ?, default_value = ?, is_required = ?, is_modifiable = ?, priority = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where property_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyName());
			ps.setString(2, bean.getPropertyCode());
			ps.setInt(3, bean.getMaterialTypeId());
			ps.setInt(4, bean.getParentPropertyId());
			ps.setInt(5, bean.getPropertyType());
			ps.setInt(6, bean.getInputMethod());
			ps.setString(7, bean.getInputStyle());
			ps.setString(8, bean.getInputDesc());
			ps.setInt(9, bean.getFieldType());
			ps.setString(10, bean.getFieldFormat());
			ps.setString(11, bean.getFieldSource());
			ps.setString(12, bean.getDefaultValue());
			ps.setInt(13, bean.getIsRequired());
			ps.setInt(14, bean.getIsModifiable());
			ps.setInt(15, bean.getPriority());
			ps.setInt(16, bean.getStatus());
			ps.setInt(17, bean.getModifyUser());
			ps.setString(18, bean.getMemo());

			ps.setInt(19, bean.getPropertyId());

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

	public MaterialPropertyTypeBean getMaterialPropertyTypeByKey(int propertyId) {
		MaterialPropertyTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property_type where property_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, propertyId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialPropertyTypeBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialPropertyTypeBean.class);
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

	public PageResultBean<MaterialPropertyTypeBean> getMaterialPropertyTypePageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<MaterialPropertyTypeBean> result = new PageResultBean<MaterialPropertyTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property_type";

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
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String materialType = condMap.get("materialType");
			if (StringKit.isValid(materialType)) {
				condList.add("material_type_id = ?");
				paramList.add(materialType);
			}

			String orderQuery = "order by property_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, MaterialPropertyTypeBean.class,
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
	
	public List<MaterialPropertyTypeBean> getMaterialPropertyTypeListByMap(HashMap<String, String> condMap) {
		List<MaterialPropertyTypeBean> result = new ArrayList<MaterialPropertyTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property_type";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String materialTypeId = condMap.get("materialTypeId");
			if (StringKit.isValid(materialTypeId)) {
				condList.add("material_type_id = ?");
				paramList.add(materialTypeId);
			}
			String propertyType = condMap.get("propertyType");
			if (StringKit.isValid(propertyType)) {
				condList.add("property_type = ?");
				paramList.add(propertyType);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by priority asc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, MaterialPropertyTypeBean.class, db);
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

	public MaterialPropertyTypeBean getMaterialPropertyTypeByParentId(int parentId, int status) {
		MaterialPropertyTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property_type where parent_property_id = ? and status = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, parentId);
			ps.setInt(2, status);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialPropertyTypeBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialPropertyTypeBean.class);
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

}
