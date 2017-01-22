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
import com.lpmas.pdm.bean.ProductPropertyOptionBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductPropertyOptionDao {
	private static Logger log = LoggerFactory.getLogger(ProductPropertyOptionDao.class);

	public int insertProductPropertyOption(ProductPropertyOptionBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_property_option ( property_id, option_value, option_content, status, create_time, create_user, memo) value( ?, ?, ?, ?, now(), ?, ?)";
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

	public int updateProductPropertyOption(ProductPropertyOptionBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_property_option set property_id = ?, option_value = ?, option_content = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where option_id = ?";
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

	public ProductPropertyOptionBean getProductPropertyOptionByKey(int optionId) {
		ProductPropertyOptionBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_option where option_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, optionId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductPropertyOptionBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyOptionBean.class);
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
	
	public List<ProductPropertyOptionBean> getProductPropertyOptionListByPropertyId(int propertyId) {
		List<ProductPropertyOptionBean> list = new ArrayList<ProductPropertyOptionBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_option where property_id = ? and status = ? order by option_id asc";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, propertyId);
			ps.setInt(2, Constants.STATUS_VALID);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductPropertyOptionBean bean = new ProductPropertyOptionBean();
				bean = BeanKit.resultSet2Bean(rs, ProductPropertyOptionBean.class);
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

	public PageResultBean<ProductPropertyOptionBean> getProductPropertyOptionPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<ProductPropertyOptionBean> result = new PageResultBean<ProductPropertyOptionBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_property_option";

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
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductPropertyOptionBean.class,
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
