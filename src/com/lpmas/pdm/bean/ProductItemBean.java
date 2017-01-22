package com.lpmas.pdm.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class ProductItemBean {
	@FieldTag(name = "商品项ID")
	private int itemId = 0;
	@FieldTag(name = "商品项名称")
	private String itemName = "";
	@FieldTag(name = "商品项编码")
	private String itemNumber = "";
	@FieldTag(name = "商品ID")
	private int productId = 0;
	@FieldTag(name = "规格")
	private String specification = "";
	@FieldTag(name = "规格描述")
	private String specificationDesc = "";
	@FieldTag(name = "计量单位")
	private String unit = "";
	@FieldTag(name = "净重")
	private double netWeight = 0;
	@FieldTag(name = "保质期，单位：天")
	private double guaranteePeriod = 0;
	@FieldTag(name = "列表价")
	private double listedPrice = 0;
	@FieldTag(name = "条形码")
	private String barcode = "";
	@FieldTag(name = "是否可销售")
	private String useStatus = "";
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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getSpecificationDesc() {
		return specificationDesc;
	}

	public void setSpecificationDesc(String specificationDesc) {
		this.specificationDesc = specificationDesc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}

	public double getGuaranteePeriod() {
		return guaranteePeriod;
	}

	public void setGuaranteePeriod(double guaranteePeriod) {
		this.guaranteePeriod = guaranteePeriod;
	}

	public double getListedPrice() {
		return listedPrice;
	}

	public void setListedPrice(double listedPrice) {
		this.listedPrice = listedPrice;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
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