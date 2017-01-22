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
import com.lpmas.pdm.bean.ProductDescriptionTypeBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductDescriptionTypeDao {
	private static Logger log = LoggerFactory.getLogger(ProductDescriptionTypeDao.class);

	public int insertProductDescriptionType(ProductDescriptionTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_description_type ( type_name, type_code, info_type, priority, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getTypeName());
			ps.setString(2, bean.getTypeCode());
			ps.setInt(3, bean.getInfoType());
			ps.setInt(4, bean.getPriority());
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

	public int updateProductDescriptionType(ProductDescriptionTypeBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_description_type set type_name = ?, type_code = ?, info_type = ?, priority = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where type_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getTypeName());
			ps.setString(2, bean.getTypeCode());
			ps.setInt(3, bean.getInfoType());
			ps.setInt(4, bean.getPriority());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getModifyUser());
			ps.setString(7, bean.getMemo());

			ps.setInt(8, bean.getTypeId());

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

	public ProductDescriptionTypeBean getProductDescriptionTypeByKey(int typeId) {
		ProductDescriptionTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_description_type where type_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, typeId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductDescriptionTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductDescriptionTypeBean.class);
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
	
	public ProductDescriptionTypeBean getProductDescriptionTypeByCode(String typeCode) {
		ProductDescriptionTypeBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_description_type where type_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, typeCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductDescriptionTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductDescriptionTypeBean.class);
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
	
	public List<ProductDescriptionTypeBean> getProductDescriptionTypeListByInfoType(int infoType) {
		List<ProductDescriptionTypeBean> list = new ArrayList<ProductDescriptionTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_description_type where info_type = ? and status = ? order by priority asc";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, infoType);
			ps.setInt(2, Constants.STATUS_VALID);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				ProductDescriptionTypeBean bean = new ProductDescriptionTypeBean();
				bean = BeanKit.resultSet2Bean(rs, ProductDescriptionTypeBean.class);
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

	public PageResultBean<ProductDescriptionTypeBean> getProductDescriptionTypePageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<ProductDescriptionTypeBean> result = new PageResultBean<ProductDescriptionTypeBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_description_type";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String typeId = condMap.get("typeId");
			if (StringKit.isValid(typeId)) {
				condList.add("type_id like ?");
				paramList.add("%" + typeId + "%");
			}
			String typeCode = condMap.get("typeCode");
			if (StringKit.isValid(typeCode)) {
				condList.add("type_code like ?");
				paramList.add("%" + typeCode + "%");
			}
			String typeName = condMap.get("typeName");
			if (StringKit.isValid(typeName)) {
				condList.add("type_name = ?");
				paramList.add(typeName);
			}
			String infoType = condMap.get("infoType");
			if (StringKit.isValid(infoType)) {
				condList.add("info_type =?");
				paramList.add(infoType);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by type_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductDescriptionTypeBean.class,
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
