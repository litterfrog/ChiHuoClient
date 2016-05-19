package com.fxp.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.fxp.base.BaseAppCompatActivity;
import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Comment;
import com.fxp.manager.CommentManager;
import com.fxp.util.BitMapUtil;
import com.fxp.util.DialogUtil;
import com.fxp.util.PictureUtil;
import com.fxp.manager.SharedPreferenceManager;
import com.fxp.util.TimeUtil;
import com.moxun.tagcloud.R;

public class UploadCommentActivity extends BaseAppCompatActivity {
	public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
	public static Uri imageUri;	// 拍照后的图片路径
	/** 用来请求照相功能的常量 */
	public static final int CAMERA_WITH_DATA = 168;
	/** 用来请求图片选择器的常量 */
	public static final int PHOTO_PICKED_WITH_DATA = CAMERA_WITH_DATA + 1;
	EditText etUpload;
	Button btnUploadComment;
	ImageButton ibtnGetBitmap;
	ImageView ivUploadPic;
	FrameLayout flUploadPic;
	private CommentManager commentManager;
	private int foodId;
//	Bitmap bitmap=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		diffWayToUpload();
		setContentView(R.layout.activity_upload);
		commentManager=new CommentManager(getContentResolver());
		Intent intent = getIntent();
		foodId = intent.getIntExtra(ProviderConstant.TFOOD_ID, 0);
		Log.i("TEST", foodId+"-TFOOD_ID");
		findviews();
		setListener();
		
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btnUploadWeiBo:

			int accId= SharedPreferenceManager.getInstance(this).getAccId();
			if(0==accId){
				//提示请登录
				DialogUtil.dialogWithOneButton(this,"请先登录");
				break;
			}
			String currentTime=TimeUtil.getCurrentTime();
			commentManager.setComment(foodId, accId, etUpload.getText().toString(), currentTime);
			if(BitMapUtil.bitmap==null){
				//不带图片
				Log.i("test","不带图片");
			}else{
				//带图片
				Log.i("test","带图片");
				ArrayList<Comment> commentFoodIdAccIdTime = commentManager.getCommentFoodIdAccIdTime(foodId, accId, currentTime);
				if(null!=commentFoodIdAccIdTime&&1==commentFoodIdAccIdTime.size()){
					PictureUtil.saveCommentPicture(BitMapUtil.bitmap,commentFoodIdAccIdTime.get(0).getId());
				}
			}

			finish();
			break;
		case R.id.btnUploadCancel:
			finish();
			break;
		case R.id.ibtnGetBitmap:			
			getPicFromGallery();
			break;
		case R.id.ibtnCamera:			
			getPicFromCamera();
			break;	
		case R.id.ibtnDeleteBitmap:
			BitMapUtil.bitmap=null;
			flUploadPic.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	private void getPicFromGallery(){
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,	"image/*");
		startActivityForResult(intent,	PHOTO_PICKED_WITH_DATA);
	}
	private void getPicFromCamera(){
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
            
            startActivityForResult(intent, CAMERA_WITH_DATA);
		} else {
			Toast.makeText(getApplicationContext(), "没有找到SD卡",
					Toast.LENGTH_SHORT).show();
		}
	}
	private void diffWayToUpload(){
		if(BitMapUtil.cameraFlag){
			BitMapUtil.cameraFlag=false;
			getPicFromGallery();
		}
		if(BitMapUtil.galleryFlag){
			BitMapUtil.galleryFlag=false;
			getPicFromCamera();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==CAMERA_WITH_DATA&&resultCode == Activity.RESULT_OK) {  
            String sdStatus = Environment.getExternalStorageState();  
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
                Log.i("TestFile",  
                        "SD card is not avaiable/writeable right now.");  
                return;  
            }  
            @SuppressWarnings("static-access")
			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".png";     
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
            Bundle bundle = data.getExtras();  
            BitMapUtil.bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
          
            FileOutputStream b = null;  
           //保存在系统相册位置呢
            if(!PHOTO_DIR.exists()){
            	PHOTO_DIR.mkdirs();// 创建文件夹  
            }
            
            File file = new File(PHOTO_DIR.getAbsolutePath(),name);  
  
            try {  
                b = new FileOutputStream(file);  
                BitMapUtil.bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    b.flush();  
                    b.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }
            
		}
		
		if(requestCode==PHOTO_PICKED_WITH_DATA&&resultCode == Activity.RESULT_OK){
			Uri imgUri = data.getData();
			try {
				byte[] readStream = BitMapUtil.readStream(getContentResolver().openInputStream(Uri.parse(imgUri.toString())));
				BitMapUtil.bitmap=BitMapUtil.getPicFromBytes(readStream, null);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(BitMapUtil.bitmap!=null){
			ivUploadPic.setImageBitmap(BitMapUtil.bitmap);
			flUploadPic.setVisibility(View.VISIBLE);
			
		}
		

	}


	private void setListener() {
		
		etUpload.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().equals("")){
					btnUploadComment.setEnabled(false);
				}else{
					btnUploadComment.setEnabled(true);
				}
			}
		});
	}

	private void findviews() {
		etUpload = (EditText) findViewById(R.id.etUpload);
		btnUploadComment = (Button) findViewById(R.id.btnUploadWeiBo);
		ibtnGetBitmap=(ImageButton) findViewById(R.id.ibtnGetBitmap);
		ivUploadPic=(ImageView) findViewById(R.id.ivUploadPic);
		flUploadPic=(FrameLayout) findViewById(R.id.flUploadPic);
	}
}
