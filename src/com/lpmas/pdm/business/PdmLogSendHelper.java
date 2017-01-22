package com.lpmas.pdm.business;

import com.lpmas.log.business.DataLogSender;
import com.lpmas.pdm.config.PdmConfig;

public class PdmLogSendHelper extends DataLogSender {

	public void sendAddLog(Object currentBean, Integer infoType, int infoId1, String field1) {
		super.sendAddLog(currentBean, infoType, infoId1, 0, 0, field1, "", "", PdmConfig.APP_ID);
	}

	public void sendUpdateLog(Object originalBean, Object currentBean, Integer infoType, int infoId1, String field1) {
		super.sendUpdateLog(originalBean, currentBean, infoType, infoId1, 0, 0, field1, "", "", PdmConfig.APP_ID);
	}

	public void sendRemoveLog(Object originalBean, Integer infoType, int infoId1, String field1) {
		super.sendRemoveLog(originalBean, infoType, infoId1, 0, 0, field1, "", "", PdmConfig.APP_ID);
	}

	public void sendAddLog(Object currentBean, Integer infoType, int infoId1, int infoId2, int infoId3, String field1) {
		super.sendAddLog(currentBean, infoType, infoId1, infoId2, infoId3, field1, "", "", PdmConfig.APP_ID);
	}

	public void sendUpdateLog(Object originalBean, Object currentBean, Integer infoType, int infoId1, int infoId2, int infoId3,
			String field1) {
		super.sendUpdateLog(originalBean, currentBean, infoType, infoId1, infoId2, infoId3, field1, "", "", PdmConfig.APP_ID);
	}

	public void sendRemoveLog(Object originalBean, Integer infoType, int infoId1, int infoId2, int infoId3, String field1) {
		super.sendRemoveLog(originalBean, infoType, infoId1, infoId2, infoId3, field1, "", "", PdmConfig.APP_ID);
	}

	public void sendAddLog(Object bean, Integer infoType, int infoId1, int infoId2, int infoId3, String field1,
			String field2, String field3) {
		super.sendAddLog(bean, infoType, infoId1, infoId2, infoId3, field1, field2, field3, PdmConfig.APP_ID);
	}

	public void sendUpdateLog(Object originalBean, Object newBean, Integer infoType, int infoId1, int infoId2, int infoId3,
			String field1, String field2, String field3) {
		super.sendUpdateLog(originalBean, newBean, infoType, infoId1, infoId2, infoId3, field1, field2, field3,
				PdmConfig.APP_ID);
	}

	public void sendRemoveLog(Object originalBean, Integer infoType, int infoId1, int infoId2, int infoId3, String field1,
			String field2, String field3) {
		super.sendRemoveLog(originalBean, infoType, infoId1, infoId2, infoId3, field1, field2, field3, PdmConfig.APP_ID);
	}

}
