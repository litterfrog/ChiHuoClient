package com.fxp.manager;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.fxp.constants.ProviderConstant;
import com.fxp.entity.User;

public class UserInfoManager {

	private ContentResolver contentResolver;
	private String[] userInfoColums=new String[] {
    		ProviderConstant.TUSERINFO_ID,
            ProviderConstant.TUSERINFO_NAME,
            ProviderConstant.TUSERINFO_ACCID,
            ProviderConstant.TUSERINFO_ADDRESS,
            ProviderConstant.TUSERINFO_PHONE,
            ProviderConstant.TUSERINFO_SEX,
            
  };
	public UserInfoManager(ContentResolver contentResolver){
		this.contentResolver=contentResolver;
	};	
	public ArrayList<User> getUserListAll(){
		Cursor cursor = contentResolver.query(
		          ProviderConstant.USER_URI, 
		          userInfoColums,
		          null, 
		          null, 
		          null);
		return getUserInfoFromCursor(cursor);
	}
	public User getUserByAccId(int accId){
		if(accId<=0){
			return null;
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.USER_URI, 
		          userInfoColums,
		          ProviderConstant.TUSERINFO_ACCID+"="+accId, 
		          null, 
		          null);
		ArrayList<User> userInfoFromCursor = getUserInfoFromCursor(cursor);
		if(0==userInfoFromCursor.size()){
			return null;
		}
		return userInfoFromCursor.get(0);
//		return getUserInfoFromCursor(cursor).get(0);会get不到为什么？
	}	
	private ArrayList<User> getUserInfoFromCursor(Cursor cursor){
		ArrayList<User> userList=new ArrayList<User>();
		while(cursor.moveToNext()){
			User user =new User();
			user.setId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TUSERINFO_ID)));
			user.setName(cursor.getString(cursor.getColumnIndex(ProviderConstant.TUSERINFO_NAME)));
			user.setAccId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TUSERINFO_ACCID)));
			user.setPhone(cursor.getString(cursor.getColumnIndex(ProviderConstant.TUSERINFO_PHONE)));
			user.setAddress(cursor.getString(cursor.getColumnIndex(ProviderConstant.TUSERINFO_ADDRESS)));
			user.setSex(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TUSERINFO_SEX)));
			userList.add(user);
		}
		cursor.close();
		return userList;
	}


}
