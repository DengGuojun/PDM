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
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductInfoDao {
	private static Logger log = LoggerFactory.getLogger(ProductInfoDao.class);

	public int insertProductInfo(ProductInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_info ( product_name, product_number, brand_id, type_id_1, type_id_2, type_id_3, quality_level, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getProductName());
			ps.setString(2, bean.getProductNumber());
			ps.setInt(3, bean.getBrandId());
			ps.setInt(4, bean.getTypeId1());
			ps.setInt(5, bean.getTypeId2());
			ps.setInt(6, bean.getTypeId3());
			ps.setString(7, bean.getQualityLevel());
			ps.setInt(8, bean.getStatus());
			ps.setInt(9, bean.getCreateUser());
			ps.setString(10, bean.getMemo());

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

	public int updateProductInfo(ProductInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_info set product_name = ?, product_number = ?, brand_id = ?, type_id_1 = ?, type_id_2 = ?, type_id_3 = ?, quality_level = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where product_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getProductName());
			ps.setString(2, bean.getProductNumber());
			ps.setInt(3, bean.getBrandId());
			ps.setInt(4, bean.getTypeId1());
			ps.setInt(5, bean.getTypeId2());
			ps.setInt(6, bean.getTypeId3());
			ps.setString(7, bean.getQualityLevel());
			ps.setInt(8, bean.getStatus());
			ps.setInt(9, bean.getModifyUser());
			ps.setString(10, bean.getMemo());

			ps.setInt(11, bean.getProductId());

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

	public ProductInfoBean getProductInfoByKey(int productId) {
		ProductInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_info where product_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, productId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductInfoBean();
				bean = BeanKit.resultSet2Bean(rs, ProductInfoBean.class);
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

	public ProductInfoBean getProductInfoByNumber(String productNumber) {
		ProductInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_info where product_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, productNumber);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductInfoBean();
				bean = BeanKit.resultSet2Bean(rs, ProductInfoBean.class);
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


	public PageResultBean<ProductInfoBean> getProductInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ProductInfoBean> result = new PageResultBean<ProductInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String productNumber = condMap.get("productNumber");
			if (StringKit.isValid(productNumber)) {
				condList.add("product_number like ?");
				paramList.add("%" + productNumber + "%");
			}
			String productName = condMap.get("productName");
			if (StringKit.isValid(productName)) {
				condList.add("product_name like ?");
				paramList.add("%" + productName + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by product_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductInfoBean.class, pageBean,
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

	public List<ProductInfoBean> getProductInfoListByMap(HashMap<String, String> condMap) {
		List<ProductInfoBean> result = new ArrayList<ProductInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String brandId = condMap.get("brandId");
			if (StringKit.isValid(brandId)) {
				condList.add("brand_id = ?");
				paramList.add(brandId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String typeId = condMap.get("typeId");
			if (StringKit.isValid(typeId)) {
				condList.add("type_id_2 = ?");
				paramList.add(typeId);
			}
			String orderQuery = "order by product_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, ProductInfoBean.class, db);
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
