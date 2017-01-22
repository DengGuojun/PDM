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
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductPropertyDao {
	private static Logger log = LoggerFactory.getLogger(ProductPropertyDao.class);

	public int insertProductProperty(ProductPropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_property ( info_id, info_type, property_code, property_value_1, property_value_2, property_value_3, create_time, create_user) value( ?, ?, ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getInfoId());
			ps.setInt(2, bean.getInfoType());
			ps.setString(3, bean.getPropertyCode());
			ps.setString(4, bean.getPropertyValue1());
			ps.setString(5, bean.getPropertyValue2());
			ps.setString(6, bean.getPropertyValue3());
			ps.setInt(7, bean.getCreateUser());

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

	public int updateProductProperty(ProductPropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_property set property_value_1 = ?, property_value_2 = ?, property_value_3 = ?, modify_time = now(), modify_user = ? where info_id = ? and info_type = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyValue1());
			ps.setString(2, bean.getPropertyValue2());
			ps.setString(3, bean.getPropertyValue3());
			ps.setInt(4, bean.getModifyUser());

			ps.setInt(5, bean.getInfoId());
			ps.setInt(6, bean.getInfoType());
			ps.setString(7, bean.getPropertyCode());

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
	
	public ProductPropertyBean getProductPropertyByKey(int infoId, int infoType, String propertyCode) {
		ProductPropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property where info_id = ? and info_type = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, infoId);
			ps.setInt(2, infoType);
			ps.setString(3, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductPropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyBean.class);
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
	
	public List<ProductPropertyBean> getProductPropertyListByPropertyCode(String propertyCode){
		List<ProductPropertyBean> list = new ArrayList<ProductPropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property where property_code = ? ";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductPropertyBean bean = new ProductPropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyBean.class);
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
	
	public List<ProductPropertyBean> getProductPropertyListByInfoIdAndInfoType(int infoId, int infoType){
		List<ProductPropertyBean> list = new ArrayList<ProductPropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property where info_id = ? and info_type = ? order by info_id desc";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, infoId);
			ps.setInt(2, infoType);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductPropertyBean bean = new ProductPropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyBean.class);
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

	
	public PageResultBean<ProductPropertyBean> getProductPropertyPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ProductPropertyBean> result = new PageResultBean<ProductPropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by info_type,property_code,info_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductPropertyBean.class,
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
