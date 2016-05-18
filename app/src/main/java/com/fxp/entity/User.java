package com.fxp.entity;

import java.io.Serializable;

import com.fxp.constants.ProviderConstant;

public class User implements Serializable{
	/**
	 *
	 */
	public static final int ISMALE = ProviderConstant.ISMALE;
	public static final int ISFEMALE = ProviderConstant.ISFEMALE;
	private static final long serialVersionUID = 1L;
	public static final int INVALID_VALUE=-1;
	private int id=INVALID_VALUE;
	private int accId=INVALID_VALUE;
	private String name=null;
	private int sex=INVALID_VALUE;
	private String phone=null;
	private String address=null;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccId() {
		return accId;
	}
	public void setAccId(int accId) {
		this.accId = accId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public static int getInvalidValue() {
		return INVALID_VALUE;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", accId=" + accId + ", name=" + name
				+ ", sex=" + sex + ", phone=" + phone + ", address=" + address
				+ "]";
	}


}
