package com.fxp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.fxp.constants.Constant;
import com.fxp.entity.Comment;
import com.moxun.tagcloud.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class PictureUtil {
	private static final String USER_HEAD_PATH="chihuo/user";
	private static String[] picList;
	private static Context m_context;
	private static File getAbsolutPath(String picPath){
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return null;
		}
		File absolutePath=new File(Environment.getExternalStorageDirectory().getAbsolutePath().trim()
				+"/"+picPath.trim());
		   if(!absolutePath.exists()){
			   Log.i("test", "mkdir:"+absolutePath.getAbsolutePath());
			   absolutePath.mkdirs();
		   }			   
		return absolutePath;
	}
	public static void init(Context context){
		m_context=context;
	}
	public static void saveCommentPicture(Bitmap bm,int commentId) {
//		Log.e(TAG, "保存图片");
		File commentBitmapFile = new File(Constant.COMMENT_FOLDER_NAME, Constant.PREFIX_COMMENT_PIC+"_"+commentId);
		if (commentBitmapFile.exists()) {
			commentBitmapFile.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(commentBitmapFile);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
//			Log.i(TAG, "已经保存");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static ArrayList<Bitmap> getCommonPictureList(int foodId){
		String picPath=Constant.BASE_PATH+foodId;
		File absolutePath=getAbsolutPath(picPath);
		ArrayList<Bitmap> commonPicList=new ArrayList<Bitmap>();
		if(null==absolutePath){
			return commonPicList;
		}	
		if(absolutePath.isDirectory()){
			picList=absolutePath.list();
			if(picList!=null){
				
				for(int i=0;i<picList.length;i++){
					if(picList[i].startsWith(Constant.PREFIX_COMMON_PIC)){
						commonPicList.add(BitmapFactory.decodeFile(absolutePath+"/"+picList[i]));
					}					
				}
			}
		}
		return commonPicList;
	}
	public static ArrayList<String> getCommonPicturePathList(int foodId){
		String picPath=Constant.BASE_PATH+foodId;
		File absolutePath=getAbsolutPath(picPath);
		ArrayList<String> commonPicPathList=new ArrayList<String>();
		if(null==absolutePath){
			return commonPicPathList;
		}	
		if(absolutePath.isDirectory()){
			picList=absolutePath.list();
			if(picList!=null){
				
				for(int i=0;i<picList.length;i++){
					if(picList[i].startsWith(Constant.PREFIX_COMMON_PIC)){
						commonPicPathList.add(absolutePath+"/"+picList[i]);
					}					
				}
			}
		}
		return commonPicPathList;
	}
	
	public static Bitmap getFoodHeadPicture(int foodId){
		String picPath=Constant.BASE_PATH+foodId;
		File absolutePath=getAbsolutPath(picPath);
		if(null==absolutePath){
			return BitmapFactory.decodeResource(m_context.getResources(), R.drawable.portrait);
		}
		Bitmap headPic=BitmapFactory.decodeResource(m_context.getResources(), R.drawable.portrait);
		if(absolutePath.isDirectory()){
			picList=absolutePath.list();
			if(picList!=null){
				for(int i=0;i<picList.length;i++){
					if(picList[i].startsWith(Constant.PREFIX_FOOD_HEAD_PIC)){
						
						headPic=BitmapFactory.decodeFile(absolutePath+"/"+picList[i]);
						break;
					}
					
				}
			}
		}
		return headPic;
	}
	public static Bitmap getUserHeadPicture(int userId){
		File absolutePath=getAbsolutPath(USER_HEAD_PATH);
		if(null==absolutePath){
			return BitmapFactory.decodeResource(m_context.getResources(), R.drawable.portrait);
		}
		Bitmap headPic=BitmapFactory.decodeResource(m_context.getResources(), R.drawable.portrait);
		if(absolutePath.isDirectory()){
			picList=absolutePath.list();
			if(picList!=null){
				for(int i=0;i<picList.length;i++){
					if(picList[i].startsWith(Constant.PREFIX_USER_HEAD_PIC+userId)){
						
						headPic=BitmapFactory.decodeFile(absolutePath+"/"+picList[i]);
						break;
					}
					
				}
			}
		}
		return headPic;
	}
	public static ArrayList<Bitmap> getCommentPictureList(int commentId){
		File absolutePath=getAbsolutPath(Constant.COMMENT_FOLDER_NAME);
		ArrayList<Bitmap> commentPicList=new ArrayList<Bitmap>();
		if(null==absolutePath){
			return commentPicList;
		}		
		if(absolutePath.isDirectory()){
			picList=absolutePath.list();
			if(picList!=null){				
				for(int i=0;i<picList.length;i++){
					//文件名：前缀_评论id_文件编号
					if(picList[i].startsWith(Constant.PREFIX_COMMENT_PIC+commentId+"_")){
						commentPicList.add(BitmapFactory.decodeFile(absolutePath+"/"+picList[i]));
						break;
					}
					
				}
			}
		}
		return commentPicList;
	}
	public static Bitmap getCommentPicture(Comment comment){
		File absolutePath=getAbsolutPath(Constant.COMMENT_FOLDER_NAME);
		if(null==absolutePath){
			return null;
		}
		Bitmap commentPicture=null;
		if(absolutePath.isDirectory()){
			picList=absolutePath.list();
			if(picList!=null){				
				for(int i=0;i<picList.length;i++){
					//文件名：前缀_commentid
					if(picList[i].startsWith(Constant.PREFIX_COMMENT_PIC+comment.getId())){
						commentPicture=BitmapFactory.decodeFile(absolutePath+"/"+picList[i]);
						break;
					}
					
				}
			}
		}
		return commentPicture;
	}
	
}
