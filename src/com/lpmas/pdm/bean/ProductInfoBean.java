package com.lpmas.pdm.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class ProductInfoBean {
	@FieldTag(name = "商品ID")
	private int productId = 0;
	@FieldTag(name = "商品名称")
	private String productName = "";
	@FieldTag(name = "商品编码")
	private String productNumber = "";
	@FieldTag(name = "品牌ID")
	private int brandId = 0;
	@FieldTag(name = "商品类型1")
	private int typeId1 = 0;
	@FieldTag(name = "商品类型2")
	private int typeId2 = 0;
	@FieldTag(name = "商品类型3")
	private int typeId3 = 0;
	@FieldTag(name = "质量等级")
	private String qualityLevel = "";
	@FieldTag(name = "状态")
	private int status = 0;
	@FieldTag(name = "创建时间")
	private Timestamp createTime = null;
	@FieldTag(name = "创建用户")
	private int createUser = 0;
	@FieldTag(name = "修改时间")
	private Timestamp modifyTime = null;
	@FieldTag(name = "修改用户")
	private int modifyUser = 0;
	@FieldTag(name = "备注")
	private String memo = "";

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public int getTypeId1() {
		return typeId1;
	}

	public void setTypeId1(int typeId1) {
		this.typeId1 = typeId1;
	}

	public int getTypeId2() {
		return typeId2;
	}

	public void setTypeId2(int typeId2) {
		this.typeId2 = typeId2;
	}

	public int getTypeId3() {
		return typeId3;
	}

	public void setTypeId3(int typeId3) {
		this.typeId3 = typeId3;
	}

	public String getQualityLevel() {
		return qualityLevel;
	}

	public void setQualityLevel(String qualityLevel) {
		this.qualityLevel = qualityLevel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(int modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}