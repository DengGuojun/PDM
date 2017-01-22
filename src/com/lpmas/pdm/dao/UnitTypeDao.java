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
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class UnitTypeDao {
	private static Logger log = LoggerFactory.getLogger(UnitTypeDao.class);

	public int insertUnitType(UnitTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into unit_type ( type_name, type_code, status, create_time, create_user, memo) value( ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getTypeName());
			ps.setString(2, bean.getTypeCode());
			ps.setInt(3, bean.getStatus());
			ps.setInt(4, bean.getCreateUser());
			ps.setString(5, bean.getMemo());

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

	public int updateUnitType(UnitTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update unit_type set type_name = ?, type_code = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where type_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getTypeName());
			ps.setString(2, bean.getTypeCode());
			ps.setInt(3, bean.getStatus());
			ps.setInt(4, bean.getModifyUser());
			ps.setString(5, bean.getMemo());

			ps.setInt(6, bean.getTypeId());

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

	public UnitTypeBean getUnitTypeByKey(int typeId) {
		UnitTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_type where type_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, typeId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new UnitTypeBean();
				bean = BeanKit.resultSet2Bean(rs, UnitTypeBean.class);
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
	
	public UnitTypeBean getUnitTypeByCode(String typeCode) {
		UnitTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_type where type_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, typeCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new UnitTypeBean();
				bean = BeanKit.resultSet2Bean(rs, UnitTypeBean.class);
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

	public PageResultBean<UnitTypeBean> getUnitTypePageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<UnitTypeBean> result = new PageResultBean<UnitTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_type";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String typeName = condMap.get("typeName");
			if (StringKit.isValid(typeName)) {
				condList.add("type_name like ?");
				paramList.add("%" + typeName + "%");
			}
			String typeCode = condMap.get("typeCode");
			if (StringKit.isValid(typeCode)) {
				condList.add("type_code like ?");
				paramList.add("%" + typeCode + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by type_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, UnitTypeBean.class, pageBean, db);
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
	
	public List<UnitTypeBean> getUnitTypeListByMap(HashMap<String, String> condMap) {
		List<UnitTypeBean> list = new ArrayList<UnitTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_type";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by type_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, UnitTypeBean.class, db);
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

}
