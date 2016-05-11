package com.fxp.manager;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.text.TextUtils;

import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Food;

public class FoodManager {
	private ContentResolver contentResolver;
	private int groupSize;
	private String[] foodColums=new String[] {
    		ProviderConstant.TFOOD_ID,
            ProviderConstant.TFOOD_NAME,
            ProviderConstant.TFOOD_PICTRUE_PATH,
            ProviderConstant.TFOOD_PHONE,
            ProviderConstant.TFOOD_ADDRESS,
            ProviderConstant.TFOOD_LABEL,
            ProviderConstant.TFOOD_SUMMARY,
  };
	public FoodManager(ContentResolver contentResolver){
		this.contentResolver=contentResolver;
		this.groupSize=10;
	};	
	public ArrayList<Food> getFoodListAll(){
		Cursor cursor = contentResolver.query(
		          ProviderConstant.FOOD_URI, 
		          foodColums,
		          null, 
		          null, 
		          null);
		return getFoodFromCursor(cursor);
	}
	public ArrayList<Food> getFoodListFragmentByName(int groupcount,String name){
		if(TextUtils.isEmpty(name)){
			name="%";
		}else{
			name="%"+name+"%";
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.FOOD_URI, 
		          foodColums,
		          ProviderConstant.TFOOD_NAME+" like ?", 
		          new String[]{name}, 
		          ProviderConstant.TFOOD_NAME+" asc limit "+(groupcount*groupSize)+","+groupSize);

		
		return getFoodFromCursor(cursor);
	}
	public ArrayList<Food> getFoodListFragmentByAddress(int groupcount,String address){
		if(TextUtils.isEmpty(address)){
			address="%";
		}else{
			address="%"+address+"%";
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.FOOD_URI, 
		          foodColums,
		          ProviderConstant.TFOOD_ADDRESS+" like ?", 
		          new String[]{address}, 
		          ProviderConstant.TFOOD_NAME+" asc limit "+(groupcount*groupSize)+","+groupSize);

		
		return getFoodFromCursor(cursor);
	} 
	public ArrayList<Food> getFoodListFragmentByLabel(int groupcount,String label){
		if(TextUtils.isEmpty(label)){
			label="%";
		}else{
			label="%"+label+"%";
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.FOOD_URI, 
		          foodColums,
		          ProviderConstant.TFOOD_LABEL+" like ?", 
		          new String[]{label}, 
		          ProviderConstant.TFOOD_NAME+" asc limit "+(groupcount*groupSize)+","+groupSize);

		
		return getFoodFromCursor(cursor);
	}

	public ArrayList<Food> getFoodListFragment(int groupcount){
		Cursor cursor = contentResolver.query(
		          ProviderConstant.FOOD_URI, 
		          foodColums,
		          null, 
		          null, 
		          ProviderConstant.TFOOD_NAME+" asc limit "+(groupcount*groupSize)+","+groupSize);

		
		return getFoodFromCursor(cursor);
	}
	public Food getFoodById(int foodId){
		if(foodId<=0){
			return null;
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.FOOD_URI, 
		          foodColums,
		          ProviderConstant.TFOOD_ID+"="+foodId, 
		          null, 
		          null);
		ArrayList<Food> foodFromCursor = getFoodFromCursor(cursor);
		if(foodFromCursor.size()==0){
			return null;
		}
		return foodFromCursor.get(0);
	}
	private ArrayList<Food> getFoodFromCursor(Cursor cursor){
		ArrayList<Food> foodList=new ArrayList<Food>();
		while(cursor.moveToNext()){
			Food food =new Food();
			food.setId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TFOOD_ID)));
			food.setName(cursor.getString(cursor.getColumnIndex(ProviderConstant.TFOOD_NAME)));
			food.setPictruepath(cursor.getString(cursor.getColumnIndex(ProviderConstant.TFOOD_PICTRUE_PATH)));
			food.setPhone(cursor.getString(cursor.getColumnIndex(ProviderConstant.TFOOD_PHONE)));
			food.setAddress(cursor.getString(cursor.getColumnIndex(ProviderConstant.TFOOD_ADDRESS)));
			food.setLabel(cursor.getString(cursor.getColumnIndex(ProviderConstant.TFOOD_LABEL)));
			food.setSummary(cursor.getString(cursor.getColumnIndex(ProviderConstant.TFOOD_SUMMARY)));
			foodList.add(food);
		}
		cursor.close();
		return foodList;
	}
	public void setGroupSize(int groupSize){
		this.groupSize=groupSize;
	}
}
