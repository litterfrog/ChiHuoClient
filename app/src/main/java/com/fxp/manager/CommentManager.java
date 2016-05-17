package com.fxp.manager;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Comment;
import com.fxp.entity.Food;
import com.fxp.entity.User;

public class CommentManager {

	private ContentResolver contentResolver;
	private int groupSize;
	private String[] commentColums=new String[] {
    		ProviderConstant.TCOMMENT_ID,
            ProviderConstant.TCOMMENT_ACCID,
            ProviderConstant.TCOMMENT_FOODID,
            ProviderConstant.TCOMMENT_COMMENT,
            ProviderConstant.TCOMMENT_TIME,
  };
	private FoodManager foodManager;
	private UserInfoManager userInfoManager;
	public CommentManager(ContentResolver contentResolver){
		this.contentResolver=contentResolver;
		foodManager=new FoodManager(contentResolver);
		userInfoManager=new UserInfoManager(contentResolver);
		this.groupSize=10;
	};	
	public ArrayList<Comment> getCommentListAll(){
		Cursor cursor = contentResolver.query(
		          ProviderConstant.COMMENT_URI, 
		          commentColums,
		          null, 
		          null, 
		          null);
		return getCommentFromCursor(cursor);
	}
	public void setComment(int foodId,int accId,String comment,String time){
		ContentValues content=new ContentValues();
		content.put(ProviderConstant.TCOMMENT_ACCID, accId);
		content.put(ProviderConstant.TCOMMENT_FOODID, foodId);
		content.put(ProviderConstant.TCOMMENT_COMMENT, comment);
		content.put(ProviderConstant.TCOMMENT_TIME, time);
		contentResolver.insert(ProviderConstant.COMMENT_URI, content);
	}
	public ArrayList<Comment> getCommentFoodIdAccIdTime(int foodId,int AccId,String time){
		if(foodId<=0||AccId<=0|| TextUtils.isEmpty(time)){
			return null;
		}
		Cursor cursor = contentResolver.query(
				ProviderConstant.COMMENT_URI,
				commentColums,
				ProviderConstant.TCOMMENT_FOODID + "=" + foodId +
						" AND " + ProviderConstant.TCOMMENT_ACCID+"="+AccId+
						" AND "+ProviderConstant.TCOMMENT_TIME+"= ? ",
				new String[]{time},
				null);
		return getCommentFromCursor(cursor);
	}
	public ArrayList<Comment> getCommentListAllByFoodId(int foodId){
		if(foodId<0){
			return null;
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.COMMENT_URI, 
		          commentColums,
		          ProviderConstant.TCOMMENT_FOODID+" = ?", 
		          new String[]{foodId+""}, 
		          null);

		
		return getCommentFromCursor(cursor);
	}
	public ArrayList<Comment> getCommentListFragmentByFoodId(int groupcount,int foodId){
		if(foodId<0){
			return null;
		}
		Cursor cursor = contentResolver.query(
		          ProviderConstant.COMMENT_URI, 
		          commentColums,
		          ProviderConstant.TCOMMENT_FOODID+" = ?", 
		          new String[]{foodId+""}, 
		          ProviderConstant.TCOMMENT_TIME+" asc limit "+(groupcount*groupSize)+","+groupSize);

		
		return getCommentFromCursor(cursor);
	}


	public ArrayList<Comment> getCommentListFragment(int groupcount){
		Cursor cursor = contentResolver.query(
		          ProviderConstant.COMMENT_URI, 
		          commentColums,
		          null, 
		          null, 
		          ProviderConstant.TCOMMENT_FOODID+" asc limit "+(groupcount*groupSize)+","+groupSize);

		
		return getCommentFromCursor(cursor);
	}
	
	private ArrayList<Comment> getCommentFromCursor(Cursor cursor){
		ArrayList<Comment> commentList=new ArrayList<Comment>();
		while(cursor.moveToNext()){
			Log.i("test","2222222222222222222222222222222");
			Comment comment =new Comment();
			comment.setId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TCOMMENT_ID)));
			comment.setAccId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TCOMMENT_ACCID)));
			comment.setFoodId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TCOMMENT_FOODID)));
			comment.setText(cursor.getString(cursor.getColumnIndex(ProviderConstant.TCOMMENT_COMMENT)));
			comment.setCreatedAt(cursor.getString(cursor.getColumnIndex(ProviderConstant.TCOMMENT_TIME)));
			commentList.add(comment);
		}
		cursor.close();
		fillFoodToComment(commentList);
		fillUserToComment(commentList);
		return commentList;
	}
	private void fillFoodToComment(ArrayList<Comment> commentList){
		for(int i=0;i<commentList.size();i++){
			Food food = foodManager.getFoodById(commentList.get(i).getFoodId());
			if(null!=food){
				commentList.get(i).setFood(food);
			}						
		}
	}
	private void fillUserToComment(ArrayList<Comment> commentList){
		for(int i=0;i<commentList.size();i++){
			User user= userInfoManager.getUserByAccId(commentList.get(i).getAccId());
			if(null!=user){
				commentList.get(i).setUser(user);
			}
		}
	}
	public void setGroupSize(int groupSize){
		this.groupSize=groupSize;
	}

}
