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
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class MaterialPropertyDao {
	private static Logger log = LoggerFactory.getLogger(MaterialPropertyDao.class);

	public int insertMaterialProperty(MaterialPropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into material_property ( material_id, property_code, property_value_1, property_value_2, property_value_3, create_time, create_user) value( ?, ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getMaterialId());
			ps.setString(2, bean.getPropertyCode());
			ps.setString(3, bean.getPropertyValue1());
			ps.setString(4, bean.getPropertyValue2());
			ps.setString(5, bean.getPropertyValue3());
			ps.setInt(6, bean.getCreateUser());

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

	public int updateMaterialProperty(MaterialPropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update material_property set property_value_1 = ?, property_value_2 = ?, property_value_3 = ?, modify_time = now(), modify_user = ? where material_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyValue1());
			ps.setString(2, bean.getPropertyValue2());
			ps.setString(3, bean.getPropertyValue3());
			ps.setInt(4, bean.getModifyUser());

			ps.setInt(5, bean.getMaterialId());
			ps.setString(6, bean.getPropertyCode());

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

	public MaterialPropertyBean getMaterialPropertyByKey(int materialId, String propertyCode) {
		MaterialPropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property where material_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, materialId);
			ps.setString(2, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialPropertyBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialPropertyBean.class);
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

	public PageResultBean<MaterialPropertyBean> getMaterialPropertyPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<MaterialPropertyBean> result = new PageResultBean<MaterialPropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by property_code,material_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, MaterialPropertyBean.class,
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

	public List<MaterialPropertyBean> getMaterialPropertyListByMaterialId(int materialId) {
		List<MaterialPropertyBean> result = new ArrayList<MaterialPropertyBean>();
		MaterialPropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property where material_id=?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, materialId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, MaterialPropertyBean.class);
				result.add(bean);
			}
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
	
	public List<Integer> getMaterialPropertyIdListByPropertyCode(String propertyCode){
		List<Integer> result = new ArrayList<Integer>();
		Integer bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select distinct material_id from material_property where property_code=?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = rs.getInt("material_id");
				result.add(bean);
			}
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
