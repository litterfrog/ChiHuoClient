package com.fxp.activities;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxp.base.BaseAppCompatActivity;
import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Comment;
import com.fxp.manager.CommentManager;
import com.fxp.util.PictureUtil;
import com.moxun.tagcloud.R;

public class CommentActivity extends BaseAppCompatActivity {
	EditText etSendComment;
	ListView lvComment;
	Button btnSendComment;
	private int foodId;
	ArrayList<Comment> commentList1;
	CommentAdapter adapter=new CommentAdapter();
	Calendar calendar = Calendar.getInstance();
	CommentManager mCommentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Intent intent = getIntent();
		foodId = intent.getIntExtra(ProviderConstant.TFOOD_ID, 0);
		Log.i("TEST", foodId+"-TFOOD_ID");
		findViews();
		setListener();
		initData();
		if(null!=commentList1){
			lvComment.setAdapter(adapter);
		}		
	}
	private void initData() {
		mCommentManager=new CommentManager(getContentResolver());
		commentList1=mCommentManager.getCommentListAllByFoodId(foodId);
		Log.i("text", commentList1.toString());
	}
	private void findViews(){
		etSendComment=(EditText) findViewById(R.id.etSendComment);
		lvComment=(ListView) findViewById(R.id.lvComment);
		btnSendComment=(Button) findViewById(R.id.btnSendComment);
		btnSendComment.setEnabled(false);
	}
	private void setListener(){
		etSendComment.addTextChangedListener(new TextWatcher() {
			
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
					btnSendComment.setEnabled(false);
				}else{
					btnSendComment.setEnabled(true);
				}
				
			}
		});
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btnSendComment:
			sendComment();
			finish();
			break;
		case R.id.btnCancelComment:
			
			finish();
			break;
		default:
			break;
		}

	}
	private void sendComment() {
		// TODO Auto-generated method stub
		
	}
	public class CommentAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return commentList1.size();
		}

		@Override
		public Object getItem(int position) {
			return commentList1.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder=null;
			if(convertView==null){
				LayoutInflater inflater=getLayoutInflater().from(getApplicationContext());
				convertView=inflater.inflate(R.layout.item_getcomment, null);
				holder=new Holder();
				adapterFindViews(holder,convertView);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			Log.i("text", " "+(commentList1.get(position).getText()==null));
			Log.i("screen_name", " "+(commentList1.get(position).getUser()==null));
//			评论者
			if(commentList1.get(position).getUser()==null){
				holder.tvComment_name.setText("我");
			}else{
				holder.tvComment_name.setText(commentList1.get(position).getUser().getName());
//				 头像
				holder.ivCommHead.setImageBitmap(PictureUtil.getUserHeadPicture(commentList1.get(position).getUser().getId()));
			}

//			评论内容
			holder.tvComment_text.setText(commentList1.get(position).getText());
			//时间
			holder.tvCommCreated_at.setText(commentList1.get(position).getCreatedAt());
			//评论图片
			if(null!=PictureUtil.getCommentPicture(commentList1.get(position))){
				holder.ivComment.setVisibility(View.VISIBLE);
				holder.ivComment.setImageBitmap(PictureUtil.getCommentPicture(commentList1.get(position)));
			}else{
				holder.ivComment.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		private void adapterFindViews(Holder holder,View convertView){
			holder.tvComment_name=(TextView) convertView.findViewById(R.id.tvComment_name);
			holder.tvComment_text=(TextView) convertView.findViewById(R.id.tvComment_text);
			holder.tvCommCreated_at=(TextView) convertView.findViewById(R.id.tvCommCreated_at);
			holder.tvCommSource=(TextView) convertView.findViewById(R.id.tvCommSource);
			holder.ivCommHead=(ImageView) convertView.findViewById(R.id.ivCommHead);
			holder.ivComment=(ImageView) convertView.findViewById(R.id.ivComment);
		}
		
	}
	
	class Holder{
		TextView tvComment_name,tvComment_text,tvCommCreated_at,tvCommSource;
		ImageView ivCommHead,ivComment;
	}
}
