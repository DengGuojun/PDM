package com.lpmas.pdm.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class ProductPropertyOptionBean {
	@FieldTag(name = "选项ID")
	private int optionId = 0;
	@FieldTag(name = "属性ID")
	private int propertyId = 0;
	@FieldTag(name = "选项值")
	private String optionValue = "";
	@FieldTag(name = "选项内容")
	private String optionContent = "";
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

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
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