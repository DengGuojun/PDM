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
import com.lpmas.pdm.bean.BrandInfoBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class BrandInfoDao {
	private static Logger log = LoggerFactory.getLogger(BrandInfoDao.class);

	public int insertBrandInfo(BrandInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into brand_info ( brand_code, brand_name, brand_logo, group_id, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getBrandCode());
			ps.setString(2, bean.getBrandName());
			ps.setString(3, bean.getBrandLogo());
			ps.setInt(4, bean.getGroupId());
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

	public int updateBrandInfo(BrandInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update brand_info set brand_code = ?, brand_name = ?, brand_logo = ?, group_id = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where brand_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getBrandCode());
			ps.setString(2, bean.getBrandName());
			ps.setString(3, bean.getBrandLogo());
			ps.setInt(4, bean.getGroupId());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getModifyUser());
			ps.setString(7, bean.getMemo());

			ps.setInt(8, bean.getBrandId());

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

	public BrandInfoBean getBrandInfoByKey(int brandId) {
		BrandInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from brand_info where brand_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, brandId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new BrandInfoBean();
				bean = BeanKit.resultSet2Bean(rs, BrandInfoBean.class);
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
	
	public BrandInfoBean getBrandInfoByCode(String brandCode) {
		BrandInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from brand_info where brand_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, brandCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new BrandInfoBean();
				bean = BeanKit.resultSet2Bean(rs, BrandInfoBean.class);
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
	
	public PageResultBean<BrandInfoBean> getBrandInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<BrandInfoBean> result = new PageResultBean<BrandInfoBean>();

		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from brand_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String brandName = condMap.get("brandName");
			if (StringKit.isValid(brandName)) {
				condList.add("brand_name like ?");
				paramList.add("%" + brandName + "%");
			}
			String brandCode = condMap.get("brandCode");
			if (StringKit.isValid(brandCode)) {
				condList.add("brand_code like ?");
				paramList.add("%" + brandCode + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by brand_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, BrandInfoBean.class, pageBean, db);
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
	
	public List<BrandInfoBean> getBrandInfoListByMap(HashMap<String, String> condMap) {
		List<BrandInfoBean> list = new ArrayList<BrandInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql =  "select * from brand_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by brand_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, BrandInfoBean.class, db);
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
