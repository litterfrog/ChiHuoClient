package com.fxp.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Food;
import com.fxp.entity.Like;
import com.fxp.entity.Visited;

import java.util.ArrayList;

/**
 * Created by fuxinpeng on 2016/5/19.
 */
public class VisitedManager {
    private ContentResolver contentResolver;
    private int groupSize;
    private FoodManager foodManager;
    private String[] visitedColums =new String[] {
            ProviderConstant.TVISITED_ID,
            ProviderConstant.TVISITED_ACCID,
            ProviderConstant.TVISITED_FOODID,
    };
    public VisitedManager(ContentResolver contentResolver){
        this.contentResolver=contentResolver;
        foodManager=new FoodManager(contentResolver);
        this.groupSize=10;
    }
    public boolean insertVisited(int foodId,int accId){
        ContentValues content=new ContentValues();
        content.put(ProviderConstant.TVISITED_ACCID, accId);
        content.put(ProviderConstant.TVISITED_FOODID, foodId);
        Uri insert = contentResolver.insert(ProviderConstant.VISITED_URI, content);
        if(Integer.valueOf(insert.getPathSegments().get(1))>0){
            return true;
        }
        return false;
    }
    public boolean deleteVisited(int foodId,int accId){
        int ret=contentResolver.delete(
                ProviderConstant.VISITED_URI,
                ProviderConstant.TVISITED_ACCID+"="+accId+" AND "+ProviderConstant.TVISITED_FOODID+"="+foodId,
                null
        );
        if(ret<=0){
            return false;
        }
        return true;
    }
    public boolean isAlreadlyVisited(int foodId,int accId){
        if(foodId<=0||accId<=0){
            return false;
        }
        Cursor cursor = contentResolver.query(
                ProviderConstant.VISITED_URI,
                visitedColums,
                ProviderConstant.TVISITED_ACCID+"="+accId+" AND "+ProviderConstant.TVISITED_FOODID+"="+foodId,
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
    public ArrayList<Visited> getVisitedListAll(){
        Cursor cursor = contentResolver.query(
                ProviderConstant.VISITED_URI,
                visitedColums,
                null,
                null,
                null);
        return getVisitedFromCursor(cursor);
    }
    public ArrayList<Visited> getVisitedListFragment(int groupcount){
        Cursor cursor = contentResolver.query(
                ProviderConstant.VISITED_URI,
                visitedColums,
                null,
                null,
                ProviderConstant.TVISITED_ID + " asc limit " + (groupcount * groupSize) + "," + groupSize);


        return getVisitedFromCursor(cursor);
    }
    private ArrayList<Visited> getVisitedFromCursor(Cursor cursor){
        ArrayList<Visited> visitedList=new ArrayList<Visited>();
        while(cursor.moveToNext()){
            Visited visited =new Visited();
            visited.setId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TVISITED_ID)));
            visited.setAccId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TVISITED_ACCID)));
            visited.setFoodId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TVISITED_FOODID)));
            visitedList.add(visited);
        }
        cursor.close();
        fillFoodToVisited(visitedList);
        return visitedList;
    }
    public void setGroupSize(int groupSize){
        this.groupSize=groupSize;
    }
    private void fillFoodToVisited(ArrayList<Visited> visitedList){
        for(int i=0;i<visitedList.size();i++){
            Food food = foodManager.getFoodById(visitedList.get(i).getFoodId());
            if(null!=food){
                visitedList.get(i).setFood(food);
            }
        }
    }
}
