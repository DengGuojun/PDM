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
import com.lpmas.pdm.bean.BarcodeInfoBean;
import com.lpmas.pdm.factory.PdmDBFactory;

public class BarcodeInfoDao {
	private static Logger log = LoggerFactory.getLogger(BarcodeInfoDao.class);

	public int insertBarcodeInfo(BarcodeInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into barcode_info ( barcode, use_status, status, create_time, create_user, memo) value( ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getBarcode());
			ps.setString(2, bean.getUseStatus());
			ps.setInt(3, bean.getStatus());
			ps.setInt(4, bean.getCreateUser());
			ps.setString(5, bean.getMemo());

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

	public int updateBarcodeInfo(BarcodeInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update barcode_info set barcode = ?, use_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where barcode_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getBarcode());
			ps.setString(2, bean.getUseStatus());
			ps.setInt(3, bean.getStatus());
			ps.setInt(4, bean.getModifyUser());
			ps.setString(5, bean.getMemo());

			ps.setInt(6, bean.getBarcodeId());

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

	public BarcodeInfoBean getBarcodeInfoByKey(int barcodeId) {
		BarcodeInfoBean bean = null;
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from barcode_info where barcode_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, barcodeId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new BarcodeInfoBean();
				bean = BeanKit.resultSet2Bean(rs, BarcodeInfoBean.class);
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

	public PageResultBean<BarcodeInfoBean> getBarcodeInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<BarcodeInfoBean> result = new PageResultBean<BarcodeInfoBean>();
		DBFactory dbFactory = new PdmDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from barcode_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String barcode = condMap.get("barcode");
			if (StringKit.isValid(barcode)) {
				condList.add("barcode like ?");
				paramList.add("%" + barcode + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by barcode_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, BarcodeInfoBean.class, pageBean,
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
