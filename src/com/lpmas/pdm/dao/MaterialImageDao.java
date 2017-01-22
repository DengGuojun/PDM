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
import com.lpmas.pdm.bean.MaterialImageBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class MaterialImageDao {
	private static Logger log = LoggerFactory.getLogger(MaterialImageDao.class);

	public int insertMaterialImage(MaterialImageBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into material_image ( material_id, image_type, file_id, create_time, create_user) value( ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getMaterialId());
			ps.setString(2, bean.getImageType());
			ps.setString(3, bean.getFileId());
			ps.setInt(4, bean.getCreateUser());

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

	public int updateMaterialImage(MaterialImageBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update material_image set file_id = ?, modify_time = now(), modify_user = ? where material_id = ? and image_type = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getFileId());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getMaterialId());
			ps.setString(4, bean.getImageType());

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

	public MaterialImageBean getMaterialImageByKey(int materialId, String imageType) {
		MaterialImageBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_image where material_id = ? and image_type = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, materialId);
			ps.setString(2, imageType);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new MaterialImageBean();
				bean = BeanKit.resultSet2Bean(rs, MaterialImageBean.class);
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

	public PageResultBean<MaterialImageBean> getMaterialImagePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<MaterialImageBean> result = new PageResultBean<MaterialImageBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from material_image";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by material_id,image_type desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, MaterialImageBean.class, pageBean,
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
