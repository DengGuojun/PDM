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
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductTypeDao {
	private static Logger log = LoggerFactory.getLogger(ProductTypeDao.class);

	public int insertProductType(ProductTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_type ( type_name, type_code, parent_type_id, status, create_time, create_user) value( ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getTypeName());
			ps.setString(2, bean.getTypeCode());
			ps.setInt(3, bean.getParentTypeId());
			ps.setInt(4, bean.getStatus());
			ps.setInt(5, bean.getCreateUser());

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

	public int updateProductType(ProductTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_type set type_name = ?, type_code = ?, parent_type_id = ?, status = ?, modify_time = now(), modify_user = ? where type_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getTypeName());
			ps.setString(2, bean.getTypeCode());
			ps.setInt(3, bean.getParentTypeId());
			ps.setInt(4, bean.getStatus());
			ps.setInt(5, bean.getModifyUser());

			ps.setInt(6, bean.getTypeId());

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

	public ProductTypeBean getProductTypeByKey(int typeId) {
		ProductTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_type where type_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, typeId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductTypeBean.class);
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

	public ProductTypeBean getProductTypeByCode(String typeCode) {
		ProductTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_type where type_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, typeCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductTypeBean.class);
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

	public PageResultBean<ProductTypeBean> getProductTypePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ProductTypeBean> result = new PageResultBean<ProductTypeBean>();

		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_type";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String typeName = condMap.get("typeName");
			if (StringKit.isValid(typeName)) {
				condList.add("type_name like ?");
				paramList.add("%" + typeName + "%");
			}
			String typeCode = condMap.get("typeCode");
			if (StringKit.isValid(typeCode)) {
				condList.add("type_code like ?");
				paramList.add("%" + typeCode + "%");
			}
			String parentTypeId = condMap.get("parentTypeId");
			if (StringKit.isValid(parentTypeId)) {
				condList.add("parent_type_id = ?");
				paramList.add(parentTypeId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by type_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor
					.getPageResult(sql, orderQuery, condList, paramList, ProductTypeBean.class, pageBean, db);
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

	public List<ProductTypeBean> getProductTypeListByMap(HashMap<String, String> condMap) {
		List<ProductTypeBean> result = new ArrayList<ProductTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_type";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String parentTypeId = condMap.get("parentTypeId");
			if (StringKit.isValid(parentTypeId)) {
				condList.add("parent_type_id = ?");
				paramList.add(parentTypeId);
			}
			String orderQuery = "order by type_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, ProductTypeBean.class, db);
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
