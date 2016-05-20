package com.fxp.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Comment;
import com.fxp.entity.Food;
import com.fxp.entity.Like;

import java.util.ArrayList;

/**
 * Created by fuxinpeng on 2016/5/19.
 */
public class LikeManager {
    private ContentResolver contentResolver;
    private int groupSize;
    private FoodManager foodManager;
    private String[] likeColums =new String[] {
            ProviderConstant.TLIKE_ID,
            ProviderConstant.TLIKE_ACCID,
            ProviderConstant.TLIKE_FOODID,
    };
    public LikeManager(ContentResolver contentResolver){
        this.contentResolver=contentResolver;
        foodManager=new FoodManager(contentResolver);
        this.groupSize=10;
    }
    public boolean insertLike(int foodId,int accId){
        ContentValues content=new ContentValues();
        content.put(ProviderConstant.TLIKE_ACCID, accId);
        content.put(ProviderConstant.TLIKE_FOODID, foodId);
        Uri insert = contentResolver.insert(ProviderConstant.LIKE_URI, content);
        if(Integer.valueOf(insert.getPathSegments().get(1))>0){
            return true;
        }
        return false;
    }
    public boolean deleteLike(int foodId,int accId){
        int ret=contentResolver.delete(
                ProviderConstant.LIKE_URI,
                ProviderConstant.TLIKE_ACCID + "=" + accId + " AND " + ProviderConstant.TLIKE_FOODID + "=" + foodId,
                null
        );
        if(ret<=0){
            return false;
        }
        return true;
    }
    public boolean isAlreadlyLike(int foodId,int accId){
        Cursor cursor = contentResolver.query(
                ProviderConstant.LIKE_URI,
                likeColums,
                ProviderConstant.TLIKE_ACCID + "=" + accId + " AND " + ProviderConstant.TLIKE_FOODID + "=" + foodId,
                null,
                null);
        if(null==cursor){
            return false;
        }
        if(cursor.moveToNext()){
            return true;
        }
        return false;
    }
    public ArrayList<Like> getLikeListAll(){
        Cursor cursor = contentResolver.query(
                ProviderConstant.LIKE_URI,
                likeColums,
                null,
                null,
                null);
        return getLikeFromCursor(cursor);
    }
    public ArrayList<Like> getLikeListFragment(int groupcount){
        Cursor cursor = contentResolver.query(
                ProviderConstant.LIKE_URI,
                likeColums,
                null,
                null,
                ProviderConstant.TLIKE_ID + " asc limit " + (groupcount * groupSize) + "," + groupSize);


        return getLikeFromCursor(cursor);
    }
    public ArrayList<Like> getLikeListFragmentByAccId(int groupcount,int accId){
        Cursor cursor = contentResolver.query(
                ProviderConstant.LIKE_URI,
                likeColums,
                ProviderConstant.TLIKE_ACCID+"="+accId,
                null,
                ProviderConstant.TLIKE_ID + " asc limit " + (groupcount * groupSize) + "," + groupSize);


        return getLikeFromCursor(cursor);
    }
    private ArrayList<Like> getLikeFromCursor(Cursor cursor){
        ArrayList<Like> likeList=new ArrayList<Like>();
        while(cursor.moveToNext()){
            Like like =new Like();
            like.setId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TLIKE_ID)));
            like.setAccId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TLIKE_ACCID)));
            like.setFoodId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TLIKE_FOODID)));
            likeList.add(like);
        }
        cursor.close();
        fillFoodToLike(likeList);
        return likeList;
    }
    public void setGroupSize(int groupSize){
        this.groupSize=groupSize;
    }
    private void fillFoodToLike(ArrayList<Like> likeList){
        for(int i=0;i<likeList.size();i++){
            Food food = foodManager.getFoodById(likeList.get(i).getFoodId());
            if(null!=food){
                likeList.get(i).setFood(food);
            }
        }
    }
}
