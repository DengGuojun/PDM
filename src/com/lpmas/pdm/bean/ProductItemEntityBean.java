package com.lpmas.pdm.bean;

import java.sql.Timestamp;

import com.lpmas.file.client.FileServiceClient;
import com.lpmas.file.config.FileClientConfig;

/**
 * 包装ProductItemBean以及其关联的实体类
 * 
 */
public class ProductItemEntityBean {

	private ProductItemBean productItemBean;
	private ProductImageBean productImageBean;

	public ProductItemEntityBean(ProductItemBean productItemBean, ProductImageBean productImageBean) {
		super();
		this.productItemBean = productItemBean;
		this.productImageBean = productImageBean;
	}

	public ProductItemBean getProductItemBean() {
		return productItemBean;
	}

	public void setProductItemBean(ProductItemBean productItemBean) {
		this.productItemBean = productItemBean;
	}

	public ProductImageBean getProductImageBean() {
		return productImageBean;
	}

	public void setProductImageBean(ProductImageBean productImageBean) {
		this.productImageBean = productImageBean;
	}

	public int getItemId() {
		return productItemBean.getItemId();
	}

	public String getItemName() {
		return productItemBean.getItemName();
	}

	public String getItemNumber() {
		return productItemBean.getItemNumber();
	}

	public int getProductId() {
		return productItemBean.getProductId();
	}

	public String getSpecification() {
		return productItemBean.getSpecification();
	}

	public String getSpecificationDesc() {
		return productItemBean.getSpecificationDesc();
	}

	public String getUnit() {
		return productItemBean.getUnit();
	}

	public double getNetWeight() {
		return productItemBean.getNetWeight();
	}

	public double getListedPrice() {
		return productItemBean.getListedPrice();
	}

	public String getBarcode() {
		return productItemBean.getBarcode();
	}

	public String getUseStatus() {
		return productItemBean.getUseStatus();
	}

	public int getStatus() {
		return productItemBean.getStatus();
	}

	public Timestamp getCreateTime() {
		return productItemBean.getCreateTime();
	}

	public int getCreateUser() {
		return productItemBean.getCreateUser();
	}

	public Timestamp getModifyTime() {
		return productItemBean.getModifyTime();
	}

	public int getModifyUser() {
		return productItemBean.getModifyUser();
	}

	public String getMemo() {
		return productItemBean.getMemo();
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
