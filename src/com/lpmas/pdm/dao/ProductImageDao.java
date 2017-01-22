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
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class ProductImageDao {
	private static Logger log = LoggerFactory.getLogger(ProductImageDao.class);

	public int insertProductImage(ProductImageBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into product_image ( info_id, info_type, image_type, file_id, create_time, create_user) value( ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getInfoId());
			ps.setInt(2, bean.getInfoType());
			ps.setString(3, bean.getImageType());
			ps.setString(4, bean.getFileId());
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

	public int updateProductImage(ProductImageBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update product_image set file_id = ?, modify_time = now(), modify_user = ? where info_id = ? and info_type = ? and image_type = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getFileId());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getInfoId());
			ps.setInt(4, bean.getInfoType());
			ps.setString(5, bean.getImageType());

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

	public ProductImageBean getProductImageByKey(int infoId, int infoType, String imageType) {
		ProductImageBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_image where info_id = ? and info_type = ? and image_type = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, infoId);
			ps.setInt(2, infoType);
			ps.setString(3, imageType);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ProductImageBean();
				bean = BeanKit.resultSet2Bean(rs, ProductImageBean.class);
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

	public PageResultBean<ProductImageBean> getProductImagePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ProductImageBean> result = new PageResultBean<ProductImageBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from product_image";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by info_type,info_id,image_type desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ProductImageBean.class, pageBean,
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

}
