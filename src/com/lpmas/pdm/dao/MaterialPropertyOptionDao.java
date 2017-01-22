package com.lpmas.pdm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.pdm.bean.MaterialPropertyOptionBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class MaterialPropertyOptionDao {
	private static Logger log = LoggerFactory.getLogger(MaterialPropertyOptionDao.class);

	public int insertMaterialPropertyOption(MaterialPropertyOptionBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into material_property_option ( property_id, option_value, option_content, status, create_time, create_user, memo) value( ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getPropertyId());
			ps.setString(2, bean.getOptionValue());
			ps.setString(3, bean.getOptionContent());
			ps.setInt(4, bean.getStatus());
			ps.setInt(5, bean.getCreateUser());
			ps.setString(6, bean.getMemo());

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

	public int updateMaterialPropertyOption(MaterialPropertyOptionBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update material_property_option set property_id = ?, option_value = ?, option_content = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where option_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getPropertyId());
			ps.setString(2, bean.getOptionValue());
			ps.setString(3, bean.getOptionContent());
			ps.setInt(4, bean.getStatus());
			ps.setInt(5, bean.getModifyUser());
			ps.setString(6, bean.getMemo());

			ps.setInt(7, bean.getOptionId());

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

	public MaterialPropertyOptionBean getMaterialPropertyOptionByKey(int optionId) {
		MaterialPropertyOptionBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property_option where option_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, optionId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialPropertyOptionBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialPropertyOptionBean.class);
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

	public PageResultBean<MaterialPropertyOptionBean> getMaterialPropertyOptionPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<MaterialPropertyOptionBean> result = new PageResultBean<MaterialPropertyOptionBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property_option";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String propertyId = condMap.get("propertyId");
			if (StringKit.isValid(propertyId)) {
				condList.add("property_id like ?");
				paramList.add("%" + propertyId + "%");
			}
			String optionValue = condMap.get("optionValue");
			if (StringKit.isValid(optionValue)) {
				condList.add("option_value like ?");
				paramList.add("%" + optionValue + "%");
			}
			String optionContent = condMap.get("optionContent");
			if (StringKit.isValid(optionContent)) {
				condList.add("option_content like ?");
				paramList.add("%" + optionContent + "%");
			}

			String orderQuery = "order by option_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, MaterialPropertyOptionBean.class,
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

	public List<MaterialPropertyOptionBean> getMaterialPropertyOptionListByPropertyId(int propertyId) {
		List<MaterialPropertyOptionBean> result = new ArrayList<MaterialPropertyOptionBean>();
		MaterialPropertyOptionBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_property_option where property_id = ? and status = ? order by option_id asc";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, propertyId);
			ps.setInt(2, Constants.STATUS_VALID);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, MaterialPropertyOptionBean.class);
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
