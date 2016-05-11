package com.fxp.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.fxp.constants.ProviderConstant;
import com.fxp.entity.UserAccount;

public class UserAccountManager {

	private ContentResolver contentResolver;
	private String[] userAccColums=new String[] {
    		ProviderConstant.TACCOUNT_ID,
            ProviderConstant.TACCOUNT_EMAIL,
            ProviderConstant.TACCOUNT_PASSWORD,
            
  };
	public UserAccountManager(ContentResolver contentResolver){
		this.contentResolver=contentResolver;
	}
	public Boolean setUsrAccount(UserAccount userAccount){
		ContentValues content=new ContentValues();
		content.put(ProviderConstant.TACCOUNT_EMAIL, userAccount.getEmail());
		content.put(ProviderConstant.TACCOUNT_PASSWORD, userAccount.getPassword());
		Uri insert = contentResolver.insert(ProviderConstant.ACCOUNT_URI, content);
		if(Integer.valueOf(insert.getPathSegments().get(1))>0){
			return true;
		}
		return false;
	}
	public ArrayList<UserAccount> getUserAccListAll(){
		Cursor cursor = contentResolver.query(
		          ProviderConstant.ACCOUNT_URI, 
		          userAccColums,
		          null, 
		          null, 
		          null);
		return getUserAccFromCursor(cursor);
	}
	public UserAccount getUserAccByAccId(int accId){
		if(accId<=0){
			return null;
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.ACCOUNT_URI, 
		          userAccColums,
		          ProviderConstant.TACCOUNT_ID+"="+accId, 
		          null, 
		          null);
		ArrayList<UserAccount> userAccFromCursor = getUserAccFromCursor(cursor);
		if(0==userAccFromCursor.size()){
			return null;
		}
		return userAccFromCursor.get(0);
//		return getUserInfoFromCursor(cursor).get(0);会get不到为什么？
	}
	public UserAccount getUserAccByEmail(String email){
		if(TextUtils.isEmpty(email)){
			return null;
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.ACCOUNT_URI, 
		          userAccColums,
		          ProviderConstant.TACCOUNT_EMAIL+"=?", 
		          new String[]{email}, 
		          null);
		ArrayList<UserAccount> userAccFromCursor = getUserAccFromCursor(cursor);
		if(0==userAccFromCursor.size()){
			return null;
		}
		return userAccFromCursor.get(0);
//		return getUserInfoFromCursor(cursor).get(0);会get不到为什么？
	}
	private ArrayList<UserAccount> getUserAccFromCursor(Cursor cursor){
		ArrayList<UserAccount> userAccList=new ArrayList<UserAccount>();
		while(cursor.moveToNext()){
			UserAccount userAcc =new UserAccount();
			userAcc.setAccId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TACCOUNT_ID)));
			userAcc.setEmail(cursor.getString(cursor.getColumnIndex(ProviderConstant.TACCOUNT_EMAIL)));
			userAcc.setPassword(cursor.getString(cursor.getColumnIndex(ProviderConstant.TACCOUNT_PASSWORD)));
			userAccList.add(userAcc);
		}
		cursor.close();
		return userAccList;
	}
}
