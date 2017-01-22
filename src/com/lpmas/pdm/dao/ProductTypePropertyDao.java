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
import com.lpmas.pdm.bean.ProductTypePropertyBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductTypePropertyDao {
	private static Logger log = LoggerFactory.getLogger(ProductTypePropertyDao.class);

	public int insertProductTypeProperty(ProductTypePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_type_property (type_id, property_code, property_value, create_time, create_user) value( ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getTypeId());
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

	public int updateProductTypeProperty(ProductTypePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_type_property set property_value = ?, modify_time = now(), modify_user = ? where type_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyValue());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getTypeId());
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

	public ProductTypePropertyBean getProductTypePropertyByKey(int typeId, String propertyCode) {
		ProductTypePropertyBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_type_property where type_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, typeId);
			ps.setString(2, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductTypePropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductTypePropertyBean.class);
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

	public List<ProductTypePropertyBean> getProductTypePropertyListByTypeId(int typeId) {
		List<ProductTypePropertyBean> list = new ArrayList<ProductTypePropertyBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_type_property where type_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, typeId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductTypePropertyBean bean = new ProductTypePropertyBean();
				bean = BeanKit.resultSet2Bean(rs, ProductTypePropertyBean.class);
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
