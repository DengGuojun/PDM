package com.lpmas.pdm.bean;

public class WareInfoBean {

	private int wareType = 0;
	private int wareId = 0;
	private int wareInfoId = 0;//对于商品项有用
	private String wareName = "";
	private String wareNumber = "";
	private String typeName = "";
	private String specification = "";
	private String unitName = "";
	private String unitCode = "";
	private String unitTypeName = "";
	private String barcode = "";

	public int getWareType() {
		return wareType;
	}

	public void setWareType(int wareType) {
		this.wareType = wareType;
	}

	public int getWareId() {
		return wareId;
	}

	public void setWareId(int wareId) {
		this.wareId = wareId;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public String getWareNumber() {
		return wareNumber;
	}

	public void setWareNumber(String wareNumber) {
		this.wareNumber = wareNumber;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unit) {
		this.unitName = unit;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getWareInfoId() {
		return wareInfoId;
	}

	public void setWareInfoId(int wareInfoId) {
		this.wareInfoId = wareInfoId;
	}

	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}
	
	

}
