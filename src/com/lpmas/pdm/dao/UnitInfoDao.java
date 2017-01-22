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
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class UnitInfoDao {
	private static Logger log = LoggerFactory.getLogger(UnitInfoDao.class);

	public int insertUnitInfo(UnitInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into unit_info ( unit_name, unit_code, type_id, priority, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getUnitName());
			ps.setString(2, bean.getUnitCode());
			ps.setInt(3, bean.getTypeId());
			ps.setInt(4, bean.getPriority());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getCreateUser());
			ps.setString(7, bean.getMemo());

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

	public int updateUnitInfo(UnitInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update unit_info set unit_name = ?, unit_code = ?, type_id = ?, priority = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where unit_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getUnitName());
			ps.setString(2, bean.getUnitCode());
			ps.setInt(3, bean.getTypeId());
			ps.setInt(4, bean.getPriority());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getModifyUser());
			ps.setString(7, bean.getMemo());

			ps.setInt(8, bean.getUnitId());

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

	public UnitInfoBean getUnitInfoByKey(int unitId) {
		UnitInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_info where unit_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, unitId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new UnitInfoBean();
				bean = BeanKit.resultSet2Bean(rs, UnitInfoBean.class);
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

	public UnitInfoBean getUnitInfoByCode(String unitCode) {
		UnitInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_info where unit_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, unitCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new UnitInfoBean();
				bean = BeanKit.resultSet2Bean(rs, UnitInfoBean.class);
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

	public List<UnitInfoBean> getUnitInfoByMap(HashMap<String, String> condMap) {
		List<UnitInfoBean> list = new ArrayList<UnitInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
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
			String orderQuery = "order by priority";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, UnitInfoBean.class, db);
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

	public List<UnitInfoBean> getUnitInfoListByMap(HashMap<String, String> condMap) {
		List<UnitInfoBean> list = new ArrayList<UnitInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
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
			String orderQuery = "order by unit_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, UnitInfoBean.class, db);
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

	public PageResultBean<UnitInfoBean> getUnitInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<UnitInfoBean> result = new PageResultBean<UnitInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from unit_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String typeId = condMap.get("typeId");
			if (StringKit.isValid(typeId)) {
				condList.add("type_id = ?");
				paramList.add(typeId);
			}
			String unitCode = condMap.get("unitCode");
			if (StringKit.isValid(unitCode)) {
				condList.add("unit_code like ?");
				paramList.add("%" + unitCode + "%");
			}
			String unitName = condMap.get("unitName");
			if (StringKit.isValid(unitName)) {
				condList.add("unit_name like ?");
				paramList.add("%" + unitName + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by unit_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, UnitInfoBean.class, pageBean, db);
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
