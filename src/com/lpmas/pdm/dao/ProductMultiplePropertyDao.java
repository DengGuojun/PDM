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
import com.lpmas.pdm.bean.ProductMultiplePropertyBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductMultiplePropertyDao {
	private static Logger log = LoggerFactory.getLogger(ProductMultiplePropertyDao.class);

	public int insertProductMultipleProperty(ProductMultiplePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_multiple_property (property_id, info_id, info_type, property_code, property_value_1, property_value_2, property_value_3, create_time, create_user) value(?, ?, ?, ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyId());
			ps.setInt(2, bean.getInfoId());
			ps.setInt(3, bean.getInfoType());
			ps.setString(4, bean.getPropertyCode());
			ps.setString(5, bean.getPropertyValue1());
			ps.setString(6, bean.getPropertyValue2());
			ps.setString(7, bean.getPropertyValue3());
			ps.setInt(8, bean.getCreateUser());

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

	public int updateProductMultipleProperty(ProductMultiplePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_multiple_property set info_id = ?, info_type = ?, property_code = ?, property_value_1 = ?, property_value_2 = ?, property_value_3 = ?, modify_time = now(), modify_user = ? where property_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getInfoId());
			ps.setInt(2, bean.getInfoType());
			ps.setString(3, bean.getPropertyCode());
			ps.setString(4, bean.getPropertyValue1());
			ps.setString(5, bean.getPropertyValue2());
			ps.setString(6, bean.getPropertyValue3());
			ps.setInt(7, bean.getModifyUser());

			ps.setString(8, bean.getPropertyId());

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
	
	public int deleteProductMultipleProperty(String propertyId) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "delete from product_multiple_property where property_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, propertyId);
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

	public ProductMultiplePropertyBean getProductMultiplePropertyByKey(String propertyId) {
		ProductMultiplePropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_multiple_property where property_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, propertyId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductMultiplePropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductMultiplePropertyBean.class);
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
	
	public List<ProductMultiplePropertyBean> getProductMultiplePropertyListByPropertyCode(String propertyCode){
		List<ProductMultiplePropertyBean> list = new ArrayList<ProductMultiplePropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_multiple_property where property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductMultiplePropertyBean bean = new ProductMultiplePropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductMultiplePropertyBean.class);
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
	
	public List<ProductMultiplePropertyBean> getProductMultiplePropertyListByInfoIdAndInfoType(int infoId, int infoType){
		List<ProductMultiplePropertyBean> list = new ArrayList<ProductMultiplePropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_multiple_property where info_id = ? and info_type = ?  order by info_id desc";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, infoId);
			ps.setInt(2, infoType);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductMultiplePropertyBean bean = new ProductMultiplePropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductMultiplePropertyBean.class);
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

	public PageResultBean<ProductMultiplePropertyBean> getProductMultiplePropertyPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<ProductMultiplePropertyBean> result = new PageResultBean<ProductMultiplePropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_multiple_property";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by property_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductMultiplePropertyBean.class,
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
