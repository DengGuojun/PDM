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
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class MaterialInfoDao {
	private static Logger log = LoggerFactory.getLogger(MaterialInfoDao.class);

	public int insertMaterialInfo(MaterialInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into material_info ( material_name, material_number, type_id_1, type_id_2, type_id_3, specification, unit, net_weight, guarantee_period, barcode, use_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getMaterialName());
			ps.setString(2, bean.getMaterialNumber());
			ps.setInt(3, bean.getTypeId1());
			ps.setInt(4, bean.getTypeId2());
			ps.setInt(5, bean.getTypeId3());
			ps.setString(6, bean.getSpecification());
			ps.setString(7, bean.getUnit());
			ps.setString(8, bean.getNetWeight());
			ps.setDouble(9, bean.getGuaranteePeriod());
			ps.setString(10, bean.getBarcode());
			ps.setString(11, bean.getUseStatus());
			ps.setInt(12, bean.getStatus());
			ps.setInt(13, bean.getCreateUser());
			ps.setString(14, bean.getMemo());

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

	public int updateMaterialInfo(MaterialInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update material_info set material_name = ?, material_number = ?, type_id_1 = ?, type_id_2 = ?, type_id_3 = ?, specification = ?, unit = ?, net_weight = ?, guarantee_period = ?, barcode = ?, use_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where material_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getMaterialName());
			ps.setString(2, bean.getMaterialNumber());
			ps.setInt(3, bean.getTypeId1());
			ps.setInt(4, bean.getTypeId2());
			ps.setInt(5, bean.getTypeId3());
			ps.setString(6, bean.getSpecification());
			ps.setString(7, bean.getUnit());
			ps.setString(8, bean.getNetWeight());
			ps.setDouble(9, bean.getGuaranteePeriod());
			ps.setString(10, bean.getBarcode());
			ps.setString(11, bean.getUseStatus());
			ps.setInt(12, bean.getStatus());
			ps.setInt(13, bean.getModifyUser());
			ps.setString(14, bean.getMemo());

			ps.setInt(15, bean.getMaterialId());

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

	public MaterialInfoBean getMaterialInfoByKey(int materialId) {
		MaterialInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_info where material_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, materialId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialInfoBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialInfoBean.class);
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
	
	public MaterialInfoBean getMaterialInfoByNumber(String materialNumber) {
		MaterialInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_info where material_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, materialNumber);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialInfoBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialInfoBean.class);
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

	public PageResultBean<MaterialInfoBean> getMaterialInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<MaterialInfoBean> result = new PageResultBean<MaterialInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String materialNumber = condMap.get("materialNumber");
			if (StringKit.isValid(materialNumber)) {
				condList.add("material_number like ?");
				paramList.add("%" + materialNumber + "%");
			}
			String materialName = condMap.get("materialName");
			if (StringKit.isValid(materialName)) {
				condList.add("material_name like ?");
				paramList.add("%" + materialName + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			// 条件处理结束
			String orderQuery = "order by material_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, MaterialInfoBean.class, pageBean,
					db);
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

	public int getMaxMaterialInfoId() {
		int result = 0;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "SELECT MAX(material_id) as maxId from material_info";

			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				result = rs.getInt("maxId");
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

	public int checkStatus(String inStr, int status) {

		int result = 0;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "";
			if (inStr != null) {
				sql = "SELECT count(1) as total from material_info where status=" + status + " and material_id in ("
						+ inStr + ")";
			} else {
				sql = "SELECT count(1) as total from material_info where status=" + status;
			}

			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				result = rs.getInt("total");
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

	public List<MaterialInfoBean> getMaterialInfoListByTypeId(int typeId) {
		List<MaterialInfoBean> result = new ArrayList<MaterialInfoBean>();
		MaterialInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_info where type_id_1=?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, typeId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, MaterialInfoBean.class);
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
