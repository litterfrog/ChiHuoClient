package com.fxp.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxp.activities.UserInfoModifyActivity;
import com.fxp.constants.ProviderConstant;
import com.fxp.entity.User;
import com.fxp.entity.UserAccount;
import com.fxp.entity.UserInfoItem;
import com.fxp.manager.UserAccountManager;
import com.moxun.tagcloud.R;


public class UserInfoFragment extends Fragment {
	private ContentResolver contentResolver;
	private TextView tvFragProMyName, tvFragProMyFollowers_count,
			tvFragProMyFriends_count, tvFragProMyStatuses_count,tv_switch_register,tv_Profile_Title;
	private ImageView ivFragProMyHeade;
	private ListView lvUserInfo;
	private Button btnProfileLoginOut,btn_login_register;
	private EditText et_email,et_password,et_password_rescan;
	private LinearLayout llfriends,ll_login_register_form,ll_userinfo_form,ll_password_rescan;
	private ArrayList<UserInfoItem> userInfoList=new ArrayList<UserInfoItem>();
	private ProfileOnClickListener clickListener=new ProfileOnClickListener();
	private SharedPreferences prefs;
	private UserAccountManager userAccountManager;
	private User currentUser;
	private UserInfoAdapter adapter;
	private final String PREFS_NAME="chihuoACC";
	private final String PREFS_ACCID="accId";
	private final String PREFS_ACCEMAIL="accEmail";
	private final String PREFS_ACCPASSWORD="accPassword";
	public static UserInfoFragment newInstance() {

		Bundle args = new Bundle();

		UserInfoFragment fragment = new UserInfoFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findViews(view);
		setListener();
		contentResolver=getActivity().getContentResolver();
		userAccountManager=new UserAccountManager(contentResolver);
		initFace();
	}

	@Override
	public void onResume() {
		super.onResume();
		int accId = prefs.getInt(PREFS_ACCID, 0);
		if(0!=accId){
			refreshUserInfoFace(accId);
		}
	}

	private void refreshUserInfoFace(int accId) {
		currentUser=new User();
		currentUser.setAccId(accId);
		selectUserInfo(currentUser);
		tvFragProMyName.setText(currentUser.getName());
		tvFragProMyFollowers_count.setText("");
		tvFragProMyFriends_count.setText("");
		tvFragProMyStatuses_count.setText("");
		initUserInfoList(currentUser);
		adapter.notifyDataSetChanged();
	}


	private void initFace() {
		prefs = getActivity().getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		int accId = prefs.getInt(PREFS_ACCID, 0);
		if(0==accId){
			showLoginFace();
		}else{
			showUserInfoFace(accId);
		}
		
	}

	private void showUserInfoFace(int accId) {
		tv_Profile_Title.setText("我");
		tv_switch_register.setText("设置");
		
		tv_switch_register.setVisibility(View.VISIBLE);
		ll_login_register_form.setVisibility(View.GONE);
		ll_userinfo_form.setVisibility(View.VISIBLE);
		
		currentUser=new User();
		currentUser.setAccId(accId);
		selectUserInfo(currentUser);
		setViews(currentUser);
	}

	private void showLoginFace() {
		tv_Profile_Title.setText("登录");
		tv_switch_register.setText("注册");
		btn_login_register.setText("登录");
		et_email.setText("");
		et_password.setText("");
		
		tv_switch_register.setVisibility(View.VISIBLE);
		ll_login_register_form.setVisibility(View.VISIBLE);
		ll_userinfo_form.setVisibility(View.GONE);
		ll_password_rescan.setVisibility(View.GONE);		
	}
	private void showRegisterFace() {
		tv_Profile_Title.setText("注册");
		btn_login_register.setText("注册");
		tv_switch_register.setText("登录");
		et_email.setText("");
		et_password.setText("");
		et_password_rescan.setText("");


		ll_login_register_form.setVisibility(View.VISIBLE);
		ll_userinfo_form.setVisibility(View.GONE);
		ll_password_rescan.setVisibility(View.VISIBLE);
	}
	private void saveACCinfoToLocal(UserAccount userAccount) {
		if(null==prefs){
			prefs = getActivity().getSharedPreferences(PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PREFS_ACCID, userAccount.getAccId());
		editor.putString(PREFS_ACCEMAIL, userAccount.getEmail());
		editor.putString(PREFS_ACCPASSWORD, userAccount.getPassword());
		editor.commit();
	}
	private void selectUserInfo(User user) {
		Cursor cursor = contentResolver.query(
      ProviderConstant.USER_URI, new String[] {
    		  
    		  ProviderConstant.TUSERINFO_ID,ProviderConstant.TUSERINFO_ACCID, ProviderConstant.TUSERINFO_NAME,ProviderConstant.TUSERINFO_PHONE,ProviderConstant.TUSERINFO_SEX,ProviderConstant.TUSERINFO_ADDRESS}, ProviderConstant.TUSERINFO_ACCID+"="+user.getAccId(), null, null);
		if(cursor.moveToNext()){
			user.setId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TUSERINFO_ID)));
			user.setAccId(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TUSERINFO_ACCID)));
			user.setName(cursor.getString(cursor.getColumnIndex(ProviderConstant.TUSERINFO_NAME)));
			user.setPhone(cursor.getString(cursor.getColumnIndex(ProviderConstant.TUSERINFO_PHONE)));
			user.setSex(cursor.getInt(cursor.getColumnIndex(ProviderConstant.TUSERINFO_SEX)));
			user.setAddress(cursor.getString(cursor.getColumnIndex(ProviderConstant.TUSERINFO_ADDRESS)));
		}
		
	}

	private void setListener(){
		btnProfileLoginOut.setOnClickListener(clickListener);
		llfriends.setOnClickListener(clickListener);
		btn_login_register.setOnClickListener(clickListener);
		tv_switch_register.setOnClickListener(clickListener);
	}

	private void setViews(User user){
		tvFragProMyName.setText(user.getName());
		tvFragProMyFollowers_count.setText("");
		tvFragProMyFriends_count.setText("");
		tvFragProMyStatuses_count.setText("");
		initUserInfoList(user);
		adapter=new UserInfoAdapter();
		lvUserInfo.setAdapter(adapter);
	}
	private void initUserInfoList(User user){
		userInfoList.clear();
		if(null==user.getAddress()){
			userInfoList.add(new UserInfoItem("所在地","未知"));
		}else{
			userInfoList.add(new UserInfoItem("所在地",user.getAddress()));
		}
		
		if(user.getSex()==ProviderConstant.ISMALE){
			userInfoList.add(new UserInfoItem("性别","男"));
		}else if(user.getSex()==ProviderConstant.ISFEMALE){
			userInfoList.add(new UserInfoItem("性别","女"));
		}else{
			userInfoList.add(new UserInfoItem("性别","未知"));
		}
		if(null==user.getPhone()){
			userInfoList.add(new UserInfoItem("手机号码","未知"));
		}else{
			userInfoList.add(new UserInfoItem("手机号码",user.getPhone()));
		}
		
	}
	public class UserInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return userInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return userInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder=null;
			if(convertView==null){
				LayoutInflater inflater=LayoutInflater.from(getActivity());
				convertView=inflater.inflate(R.layout.item_user, null);
				holder=new Holder();
				holder.tvUserProperty=(TextView) convertView.findViewById(R.id.tvUserProperty);
				holder.tvUserValue=(TextView) convertView.findViewById(R.id.tvUserValue);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			holder.tvUserProperty.setText(userInfoList.get(position).property);
			holder.tvUserValue.setText(userInfoList.get(position).value);
			return convertView;
		}
		
	}
	class Holder{
		TextView tvUserProperty,tvUserValue;
	}
	private void findViews(View view) {
		tvFragProMyName = (TextView) view.findViewById(R.id.tvFragProMyName);
		tvFragProMyFollowers_count = (TextView) view
				.findViewById(R.id.tvFragProMyFollowers_count);
		tvFragProMyFriends_count = (TextView) view
				.findViewById(R.id.tvFragProMyFriends_count);
		tvFragProMyStatuses_count = (TextView) view
				.findViewById(R.id.tvFragProMyStatuses_count);
		tv_switch_register = (TextView) view
				.findViewById(R.id.tv_switch_register);
		tv_Profile_Title = (TextView) view
				.findViewById(R.id.tv_Profile_Title);
		ivFragProMyHeade = (ImageView) view.findViewById(R.id.ivFragProMyHead);
		btnProfileLoginOut = (Button) view.findViewById(R.id.btnProfileLoginOut);
		btn_login_register = (Button) view.findViewById(R.id.btn_login_register);
		lvUserInfo=(ListView) view.findViewById(R.id.lvUserInfo);
		llfriends=(LinearLayout) view.findViewById(R.id.llfriends);
		ll_login_register_form=(LinearLayout) view.findViewById(R.id.ll_login_register_form);
		ll_userinfo_form=(LinearLayout) view.findViewById(R.id.ll_userinfo_form);
		ll_password_rescan=(LinearLayout) view.findViewById(R.id.ll_password_rescan);
		et_email=(EditText) view.findViewById(R.id.et_email);
		et_password=(EditText) view.findViewById(R.id.et_password);
		et_password_rescan=(EditText) view.findViewById(R.id.et_password_rescan);
	}
	public class ProfileOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnProfileLoginOut:
				Log.i("TEST", "btnProfileLoginOut");
				doLoginOut();
				break;
			case R.id.btn_login_register:
				doLoginOrRegister();
				break;
			case R.id.tv_switch_register:
				if("注册"==tv_switch_register.getText().toString().trim()){
					showRegisterFace();
				}else if("设置"==tv_switch_register.getText().toString().trim()){
					//启动设置activity
					Intent intent=new Intent(getActivity(),UserInfoModifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", currentUser);
					intent.putExtras(bundle);
					startActivity(intent);
				}else if("登录"==tv_switch_register.getText().toString().trim()){
					showLoginFace();
				}
				break;
			case R.id.llfriends:
				break;
			default:
				break;
			}
			
		}

		private void doLoginOut() {
			clearLocalCache();
			showLoginFace();
			
		}

		private void clearLocalCache() {
			if(null==prefs){
				prefs = getActivity().getSharedPreferences(PREFS_NAME,
						Context.MODE_PRIVATE);
			}
			SharedPreferences.Editor editor = prefs.edit();
			editor.clear();
			editor.commit();
			
		}


		private void dialog(String message) {
			Builder builder = new Builder(getActivity());
			builder.setMessage(message);
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
		private void doLoginOrRegister() {
			if(btn_login_register.getText().toString().trim().equals("登录")){
				if(checkEtEmail()&&checkEtPassword()){
					UserAccount userAccByEmail = userAccountManager.getUserAccByEmail(et_email.getText().toString().trim());
					if(null==userAccByEmail){
						//提示无此账号
						dialog("无此账号");
					}else if(!userAccByEmail.getPassword().trim().equals(et_password.getText().toString().trim())){						
						//提示密码不正确
						dialog("密码不正确");
					}else{
						saveACCinfoToLocal(userAccByEmail);
						showUserInfoFace(userAccByEmail.getAccId());
					}
					
					
				}
			}else{
				if(checkEtEmail()&&checkEtPassword()&&checkEtPasswordRescan()){
					//插入useracc表
					UserAccount userAccByEmail = userAccountManager.getUserAccByEmail(et_email.getText().toString().trim());
					if(null==userAccByEmail){
						//无此账号,开始插入
						UserAccount userAccount=new UserAccount(et_email.getText().toString().trim(), et_password.getText().toString().trim());
						Boolean setUsrAccount = userAccountManager.setUsrAccount(userAccount);
						if(setUsrAccount){
							//插入成功
							UserAccount userAccWithId = userAccountManager.getUserAccByEmail(userAccount.getEmail());
							Toast.makeText(getActivity(), "注册成功，记得完善信息哦！", Toast.LENGTH_SHORT).show();
							saveACCinfoToLocal(userAccWithId);
							showUserInfoFace(-1);
						}else{
							dialog("注册失败");
						}

						
					}else{
						//提示此账号已经存在
						dialog("此账号已经存在");
					}
					
				}
				
			}
			
		}

		private boolean checkEtPasswordRescan() {
			if(TextUtils.isEmpty(et_password_rescan.getText().toString())){
				//提示不能为空
				dialog("密码不能为空");
				return false;
			}else if(!et_password_rescan.getText().toString().equals(et_password.getText().toString())){
				//提示两次不相符
				dialog("两次密码不相符");
				return false;
			}
			return true;
		}

		private boolean checkEtPassword() {
			if(TextUtils.isEmpty(et_password.getText().toString())){
				//提示不能为空
				dialog("密码不能为空");
				return false;
			}
			return true;
		}

		private Boolean checkEtEmail() {
			if(TextUtils.isEmpty(et_email.getText().toString())){
				//提示不能为空
				dialog("邮箱不能为空");
				return false;
			}else if(!et_email.getText().toString().contains("@")){
				//提示邮箱格式不正确
				dialog("邮箱格式不正确");
				return false;
			}
			return true;
			
		}
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
