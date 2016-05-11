package com.fxp.entity;

public class Food {
    private int id;
    private String name;
    private String summary;
    private String phone;
    private String pictruepath;
    private String address;
    private String label;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPictruepath() {
		return pictruepath;
	}
	public void setPictruepath(String pictruepath) {
		this.pictruepath = pictruepath;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "Food [id=" + id + ", name=" + name + ", summary=" + summary
				+ ", phone=" + phone + ", pictruepath=" + pictruepath
				+ ", address=" + address + ", label=" + label + "]";
	}
	
}
