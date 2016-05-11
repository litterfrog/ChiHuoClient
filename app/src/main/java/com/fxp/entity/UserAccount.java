package com.fxp.entity;

public class UserAccount {
	private int accId;
	private String email;
	private String password;
	public UserAccount(){}
	public UserAccount(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	public int getAccId() {
		return accId;
	}
	public void setAccId(int accId) {
		this.accId = accId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserAccount [accId=" + accId + ", email=" + email
				+ ", password=" + password + "]";
	}

}
