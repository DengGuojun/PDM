package com.lpmas.pdm.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class MaterialPropertyBean {
	@FieldTag(name = "物料ID")
	private int materialId = 0;
	@FieldTag(name = "属性代码")
	private String propertyCode = "";
	@FieldTag(name = "属性值")
	private String propertyValue1 = "";
	@FieldTag(name = "属性值2")
	private String propertyValue2 = "";
	@FieldTag(name = "属性值3")
	private String propertyValue3 = "";
	@FieldTag(name = "创建时间")
	private Timestamp createTime = null;
	@FieldTag(name = "创建用户")
	private int createUser = 0;
	@FieldTag(name = "修改时间")
	private Timestamp modifyTime = null;
	@FieldTag(name = "修改用户")
	private int modifyUser = 0;

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public String getPropertyValue1() {
		return propertyValue1;
	}

	public void setPropertyValue1(String propertyValue1) {
		this.propertyValue1 = propertyValue1;
	}

	public String getPropertyValue2() {
		return propertyValue2;
	}

	public void setPropertyValue2(String propertyValue2) {
		this.propertyValue2 = propertyValue2;
	}

	public String getPropertyValue3() {
		return propertyValue3;
	}

	public void setPropertyValue3(String propertyValue3) {
		this.propertyValue3 = propertyValue3;
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
}