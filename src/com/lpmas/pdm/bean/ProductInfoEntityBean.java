package com.lpmas.pdm.bean;

import java.sql.Timestamp;

import com.lpmas.file.client.FileServiceClient;
import com.lpmas.file.config.FileClientConfig;

/**
 * 包装ProductInfoBean以及其关联的实体类
 * 
 */
public class ProductInfoEntityBean {

	private ProductInfoBean productInfoBean;
	private ProductImageBean productImageBean;

	public ProductInfoEntityBean(ProductInfoBean productInfoBean, ProductImageBean productImageBean) {
		super();
		this.productInfoBean = productInfoBean;
		this.productImageBean = productImageBean;
	}

	public ProductInfoBean getProductInfoBean() {
		return productInfoBean;
	}

	public void setProductInfoBean(ProductInfoBean productInfoBean) {
		this.productInfoBean = productInfoBean;
	}

	public ProductImageBean getProductImageBean() {
		return productImageBean;
	}

	public void setProductImageBean(ProductImageBean productImageBean) {
		this.productImageBean = productImageBean;
	}

	public int getProductId() {
		return productInfoBean.getProductId();
	}

	public String getProductName() {
		return productInfoBean.getProductName();
	}

	public String getProductNumber() {
		return productInfoBean.getProductNumber();
	}

	public int getBrandId() {
		return productInfoBean.getBrandId();
	}

	public int getTypeId1() {
		return productInfoBean.getTypeId1();
	}

	public int getTypeId2() {
		return productInfoBean.getTypeId2();
	}

	public int getTypeId3() {
		return productInfoBean.getTypeId3();
	}

	public String getQualityLevel() {
		return productInfoBean.getQualityLevel();
	}

	public int getStatus() {
		return productInfoBean.getStatus();
	}

	public Timestamp getCreateTime() {
		return productInfoBean.getCreateTime();
	}

	public int getCreateUser() {
		return productInfoBean.getCreateUser();
	}

	public Timestamp getModifyTime() {
		return productInfoBean.getModifyTime();
	}

	public int getModifyUser() {
		return productInfoBean.getModifyUser();
	}

	public String getMemo() {
		return productInfoBean.getMemo();
	}

	public String getFileUrl() {
		String filePath = "";
		if (productImageBean != null) {
			FileServiceClient client = new FileServiceClient();
			filePath = client.getFileUrlByKey(productImageBean.getFileId());
		}
		return filePath;
	}

	public String getImageThumbnailUrl() {
		String filePath = "";
		FileServiceClient client = new FileServiceClient();
		if (productImageBean != null) {
			filePath = client.getImageThumbnailUrlByKey(productImageBean.getFileId());
		} else {
			filePath = FileClientConfig.DEFAULT_IMG_URL;
		}
		return filePath;
	}

}
