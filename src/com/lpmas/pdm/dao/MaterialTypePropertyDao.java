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
import com.lpmas.pdm.bean.MaterialTypePropertyBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class MaterialTypePropertyDao {
	private static Logger log = LoggerFactory.getLogger(MaterialTypePropertyDao.class);

	public int insertMaterialTypeProperty(MaterialTypePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into material_type_property ( type_id, property_code, property_value, create_time, create_user) value( ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getTypeId());
			ps.setString(2, bean.getPropertyCode());
			ps.setString(3, bean.getPropertyValue());
			ps.setInt(4, bean.getCreateUser());

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

	public int updateMaterialTypeProperty(MaterialTypePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update material_type_property set property_value = ?, modify_time = now(), modify_user = ? where type_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyValue());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getTypeId());
			ps.setString(4, bean.getPropertyCode());

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

	public MaterialTypePropertyBean getMaterialTypePropertyByKey(int typeId, String propertyCode) {
		MaterialTypePropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_type_property where type_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, typeId);
			ps.setString(2, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialTypePropertyBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialTypePropertyBean.class);
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

	public PageResultBean<MaterialTypePropertyBean> getMaterialTypePropertyPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<MaterialTypePropertyBean> result = new PageResultBean<MaterialTypePropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_type_property";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by property_code,type_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, MaterialTypePropertyBean.class,
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

	public List<MaterialTypePropertyBean> getMaterialTypePropertyListByTypeId(int materialTypeId) {
		List<MaterialTypePropertyBean> reuslt = new ArrayList<MaterialTypePropertyBean>();
		MaterialTypePropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_type_property where type_id=?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, materialTypeId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, MaterialTypePropertyBean.class);
				reuslt.add(bean);
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

		return reuslt;
	}

}
