package com.lpmas.pdm.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class MaterialInfoBean {
	@FieldTag(name = "物料ID")
	private int materialId = 0;
	@FieldTag(name = "物料名称")
	private String materialName = "";
	@FieldTag(name = "物料编码")
	private String materialNumber = "";
	@FieldTag(name = "物料分类1")
	private int typeId1 = 0;
	@FieldTag(name = "物料分类2")
	private int typeId2 = 0;
	@FieldTag(name = "物料分类3")
	private int typeId3 = 0;
	@FieldTag(name = "规格")
	private String specification = "";
	@FieldTag(name = "计量单位")
	private String unit = "";
	@FieldTag(name = "净重")
	private String netWeight = "";
	@FieldTag(name = "保质期，单位：天")
	private double guaranteePeriod = 0;
	@FieldTag(name = "条形码")
	private String barcode = "";
	@FieldTag(name = "使用状态")
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

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
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

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}

	public double getGuaranteePeriod() {
		return guaranteePeriod;
	}

	public void setGuaranteePeriod(double guaranteePeriod) {
		this.guaranteePeriod = guaranteePeriod;
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