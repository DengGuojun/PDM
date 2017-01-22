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
import com.lpmas.pdm.bean.ProductDescriptionBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductDescriptionDao {
	private static Logger log = LoggerFactory.getLogger(ProductDescriptionDao.class);

	public int insertProductDescription(ProductDescriptionBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_description ( info_id, info_type, desc_code, desc_value, create_time, create_user) value( ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getInfoId());
			ps.setInt(2, bean.getInfoType());
			ps.setString(3, bean.getDescCode());
			ps.setString(4, bean.getDescValue());
			ps.setInt(5, bean.getCreateUser());

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

	public int updateProductDescription(ProductDescriptionBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_description set desc_value = ?, modify_time = now(), modify_user = ? where info_id = ? and info_type = ? and desc_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getDescValue());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getInfoId());
			ps.setInt(4, bean.getInfoType());
			ps.setString(5, bean.getDescCode());

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

	public ProductDescriptionBean getProductDescriptionByKey(int infoId, int infoType, String descCode) {
		ProductDescriptionBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_description where info_id = ? and info_type = ? and desc_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, infoId);
			ps.setInt(2, infoType);
			ps.setString(3, descCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductDescriptionBean();
				bean = BeanKit.resultSet2Bean(rs, ProductDescriptionBean.class);
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
	
	public List<ProductDescriptionBean> getProductDescriptionListByInfoIdAndInfoType(int infoId, int infoType) {
		List<ProductDescriptionBean> list = new ArrayList<ProductDescriptionBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_description where info_id = ? and info_type = ? order by info_id desc";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, infoId);
			ps.setInt(2, infoType);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductDescriptionBean bean = new ProductDescriptionBean();
				bean = BeanKit.resultSet2Bean(rs, ProductDescriptionBean.class);
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

	public PageResultBean<ProductDescriptionBean> getProductDescriptionPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ProductDescriptionBean> result = new PageResultBean<ProductDescriptionBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_description";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by info_type,info_id,desc_code desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductDescriptionBean.class,
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
