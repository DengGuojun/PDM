package com.lpmas.pdm.bean;

import java.sql.Timestamp;

import com.lpmas.file.client.FileServiceClient;
import com.lpmas.file.config.FileClientConfig;

/**
 * 包装MaterialInfoBean以及其关联的实体类
 * 
 */
public class MaterialInfoEntityBean {

	private MaterialInfoBean materialInfoBean;
	private MaterialImageBean materialImageBean;

	public MaterialInfoEntityBean(MaterialInfoBean materialInfoBean, MaterialImageBean materialImageBean) {
		super();
		this.materialInfoBean = materialInfoBean;
		this.materialImageBean = materialImageBean;
	}

	public MaterialInfoBean getMaterialInfoBean() {
		return materialInfoBean;
	}

	public void setMaterialInfoBean(MaterialInfoBean materialInfoBean) {
		this.materialInfoBean = materialInfoBean;
	}

	public MaterialImageBean getMaterialImageBean() {
		return materialImageBean;
	}

	public void setMaterialImageBean(MaterialImageBean materialImageBean) {
		this.materialImageBean = materialImageBean;
	}

	public int getMaterialId() {
		return materialInfoBean.getMaterialId();
	}

	public String getMaterialName() {
		return materialInfoBean.getMaterialName();
	}

	public String getMaterialNumber() {
		return materialInfoBean.getMaterialNumber();
	}

	public int getTypeId1() {
		return materialInfoBean.getTypeId1();
	}

	public int getTypeId2() {
		return materialInfoBean.getTypeId2();
	}

	public int getTypeId3() {
		return materialInfoBean.getTypeId3();
	}

	public int getStatus() {
		return materialInfoBean.getStatus();
	}

	public Timestamp getCreateTime() {
		return materialInfoBean.getCreateTime();
	}

	public int getCreateUser() {
		return materialInfoBean.getCreateUser();
	}

	public Timestamp getModifyTime() {
		return materialInfoBean.getModifyTime();
	}

	public int getModifyUser() {
		return materialInfoBean.getModifyUser();
	}

	public String getMemo() {
		return materialInfoBean.getMemo();
	}

	public String getFileUrl() {
		String filePath = "";
		if (materialImageBean != null) {
			FileServiceClient client = new FileServiceClient();
			filePath = client.getFileUrlByKey(materialImageBean.getFileId());
		}
		return filePath;
	}

	public String getImageThumbnailUrl() {
		String filePath = "";
		FileServiceClient client = new FileServiceClient();
		if (materialImageBean != null) {
			filePath = client.getImageThumbnailUrlByKey(materialImageBean.getFileId());
		} else {
			filePath = FileClientConfig.DEFAULT_IMG_URL;
		}
		return filePath;
	}

}
