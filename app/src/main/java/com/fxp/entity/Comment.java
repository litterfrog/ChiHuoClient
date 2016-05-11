package com.fxp.entity;

public class Comment {
	private User user;
	private Food food;
	private int id;
	private int accId;
	private int foodId;
	private String text;
	private String createdAt;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
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
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	@Override
	public String toString() {
		return "Comment [user=" + user + ", food=" + food + ", id=" + id
				+ ", accId=" + accId + ", foodId=" + foodId + ", text=" + text
				+ ", createdAt=" + createdAt + "]";
	}
	

}
