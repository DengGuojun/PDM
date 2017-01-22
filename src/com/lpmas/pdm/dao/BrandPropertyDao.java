package com.lpmas.pdm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.pdm.bean.BrandPropertyBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class BrandPropertyDao {
	private static Logger log = LoggerFactory.getLogger(BrandPropertyDao.class);

	public int insertBrandProperty(BrandPropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into brand_property (brand_id, property_code, property_value, create_time, create_user) value( ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getBrandId());
			ps.setString(2, bean.getPropertyCode());
			ps.setString(3, bean.getPropertyValue());
			ps.setInt(4, bean.getCreateUser());

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

	public int updateBrandProperty(BrandPropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update brand_property set property_value = ?, modify_time = now(), modify_user = ? where brand_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyValue());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getBrandId());
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

	public BrandPropertyBean getBrandPropertyByKey(int brandId, String propertyCode) {
		BrandPropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from brand_property where brand_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, brandId);
			ps.setString(2, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new BrandPropertyBean();
				bean = BeanKit.resultSet2Bean(rs, BrandPropertyBean.class);
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
	
	public List<BrandPropertyBean> getBrandPropertyListByBrandId(int brandId) {
		List<BrandPropertyBean> list = new ArrayList<BrandPropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from brand_property where brand_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, brandId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				BrandPropertyBean bean = new BrandPropertyBean();
				bean = BeanKit.resultSet2Bean(rs, BrandPropertyBean.class);
				list.add(bean);
			}
			rs.close();
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
