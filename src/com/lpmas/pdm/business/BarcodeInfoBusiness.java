package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.BarcodeInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.BarcodeInfoDao;

public class BarcodeInfoBusiness {
	public int addBarcodeInfo(BarcodeInfoBean bean) {
		BarcodeInfoDao dao = new BarcodeInfoDao();
		int result = dao.insertBarcodeInfo(bean);
		if (result > 0) {
			bean.setBarcodeId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_BARCODE, bean.getBarcodeId(),
					LogInfoConfig.LOG_BARCODE_INFO);
		}
		return result;
	}

	public int updateBarcodeInfo(BarcodeInfoBean bean) {
		BarcodeInfoDao dao = new BarcodeInfoDao();
		BarcodeInfoBean originalBean = dao.getBarcodeInfoByKey(bean.getBarcodeId());
		int result = dao.updateBarcodeInfo(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_BARCODE, bean.getBarcodeId(),
					LogInfoConfig.LOG_BARCODE_INFO);
		}
		return result;
	}

	public BarcodeInfoBean getBarcodeInfoByKey(int barcodeId) {
		BarcodeInfoDao dao = new BarcodeInfoDao();
		return dao.getBarcodeInfoByKey(barcodeId);
	}

	public PageResultBean<BarcodeInfoBean> getBarcodeInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		BarcodeInfoDao dao = new BarcodeInfoDao();
		return dao.getBarcodeInfoPageListByMap(condMap, pageBean);
	}

	public ReturnMessageBean verifyBarcodeProperty(BarcodeInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getBarcode())) {
			result.setMessage("条形码必须填写");
		} else if (bean.getBarcode().length() > 50) {
			result.setMessage("条形码不能大于50字");
		}
		if (bean.getBarcodeId() > 0 && bean.getStatus() == Constants.STATUS_NOT_VALID) {
			ProductItemBusiness productItemBusiness = new ProductItemBusiness();
			List<ProductItemBean> itemList = productItemBusiness.getProductItemListByBarcode(bean.getBarcode());
			if (!itemList.isEmpty()) {
				result.setMessage("该条形码下包含商品项属性，不能设置为无效");
			}
		}
		return result;
	}

}