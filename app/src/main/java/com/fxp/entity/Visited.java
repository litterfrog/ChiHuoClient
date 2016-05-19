package com.fxp.entity;

/**
 * Created by fuxinpeng on 2016/5/19.
 */
public class Visited {
    private int id;
    private int accId;
    private int foodId;
    private Food food;
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

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "Visited{" +
                "id=" + id +
                ", accId=" + accId +
                ", foodId=" + foodId +
                '}';
    }
}
