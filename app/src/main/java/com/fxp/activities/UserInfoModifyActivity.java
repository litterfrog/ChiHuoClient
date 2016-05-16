package com.fxp.activities;

import com.example.handlerthreadtest.R;
import com.fxp.entity.User;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserInfoModifyActivity extends Activity {
	private EditText et_userinfo_modify_name,
				et_userinfo_modify_sex,
				et_userinfo_modify_phone,
				et_userinfo_modify_address;
	private Button btn_userinfo_modify_submit;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user=(User) this.getIntent().getExtras().getSerializable("user");
		Log.i("test",user.toString());
		setContentView(R.layout.activity_userinfomodify);
		findViews();
		initViews();
	}
	private void initViews() {
		if(TextUtils.isEmpty(user.getName())){
			et_userinfo_modify_name.setHint("未设置");
			user.setName("");
		}else{
			et_userinfo_modify_name.setText(user.getName());
		}
		if(-1==user.getSex()){
			et_userinfo_modify_sex.setHint("未设置");
		}else{
			et_userinfo_modify_sex.setText(user.getSex()==User.ISMALE?"男":"女");
		}
		if(TextUtils.isEmpty(user.getPhone())){
			et_userinfo_modify_phone.setHint("未设置");
			user.setPhone("");
		}else{
			et_userinfo_modify_phone.setText(user.getPhone());
		}
		if(TextUtils.isEmpty(user.getAddress())){
			et_userinfo_modify_address.setHint("未设置");
			user.setAddress("");
		}else{
			et_userinfo_modify_address.setText(user.getAddress());
		}
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_userinfo_modify_submit:
			
			break;

		default:
			break;
		}
	}
	private void findViews() {
		et_userinfo_modify_name=(EditText) findViewById(R.id.et_userinfo_modify_name);
		et_userinfo_modify_sex=(EditText) findViewById(R.id.et_userinfo_modify_sex);
		et_userinfo_modify_phone=(EditText) findViewById(R.id.et_userinfo_modify_phone);
		et_userinfo_modify_address=(EditText) findViewById(R.id.et_userinfo_modify_address);
		btn_userinfo_modify_submit=(Button) findViewById(R.id.btn_userinfo_modify_submit);
	}
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
		}
}
