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
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductItemDao {
	private static Logger log = LoggerFactory.getLogger(ProductItemDao.class);

	public int insertProductItem(ProductItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_item ( item_name, item_number, product_id, specification, specification_desc, unit, net_weight, guarantee_period, listed_price, barcode, use_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getItemName());
			ps.setString(2, bean.getItemNumber());
			ps.setInt(3, bean.getProductId());
			ps.setString(4, bean.getSpecification());
			ps.setString(5, bean.getSpecificationDesc());
			ps.setString(6, bean.getUnit());
			ps.setDouble(7, bean.getNetWeight());
			ps.setDouble(8, bean.getGuaranteePeriod());
			ps.setDouble(9, bean.getListedPrice());
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

	public int updateProductItem(ProductItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_item set item_name = ?, item_number = ?, product_id = ?, specification = ?, specification_desc = ?, unit = ?, net_weight = ?, guarantee_period = ?, listed_price = ?, barcode = ?, use_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getItemName());
			ps.setString(2, bean.getItemNumber());
			ps.setInt(3, bean.getProductId());
			ps.setString(4, bean.getSpecification());
			ps.setString(5, bean.getSpecificationDesc());
			ps.setString(6, bean.getUnit());
			ps.setDouble(7, bean.getNetWeight());
			ps.setDouble(8, bean.getGuaranteePeriod());
			ps.setDouble(9, bean.getListedPrice());
			ps.setString(10, bean.getBarcode());
			ps.setString(11, bean.getUseStatus());
			ps.setInt(12, bean.getStatus());
			ps.setInt(13, bean.getModifyUser());
			ps.setString(14, bean.getMemo());

			ps.setInt(15, bean.getItemId());

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

	public ProductItemBean getProductItemByKey(int itemId) {
		ProductItemBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_item where item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, itemId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductItemBean();
				bean = BeanKit.resultSet2Bean(rs, ProductItemBean.class);
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

	public ProductItemBean getProductItemByNumber(String itemNumber) {
		ProductItemBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_item where item_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, itemNumber);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductItemBean();
				bean = BeanKit.resultSet2Bean(rs, ProductItemBean.class);
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

	public PageResultBean<ProductItemBean> getProductItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ProductItemBean> result = new PageResultBean<ProductItemBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String itemNumber = condMap.get("itemNumber");
			if (StringKit.isValid(itemNumber)) {
				condList.add("item_number like ?");
				paramList.add("%" + itemNumber + "%");
			}
			String itemName = condMap.get("itemName");
			if (StringKit.isValid(itemName)) {
				condList.add("item_name like ?");
				paramList.add("%" + itemName + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor
					.getPageResult(sql, orderQuery, condList, paramList, ProductItemBean.class, pageBean, db);
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

	public List<ProductItemBean> getProductItemListByMap(HashMap<String, String> condMap) {
		List<ProductItemBean> result = new ArrayList<ProductItemBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_item";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String productId = condMap.get("productId");
			if (StringKit.isValid(productId)) {
				condList.add("product_id = ?");
				paramList.add(productId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String barcode = condMap.get("barcode");
			if (StringKit.isValid(barcode)) {
				condList.add("barcode = ?");
				paramList.add(barcode);
			}
			String orderQuery = "order by item_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, ProductItemBean.class, db);
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
