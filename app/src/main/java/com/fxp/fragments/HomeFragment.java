package com.fxp.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxp.activities.CommentActivity;
import com.fxp.activities.LargeImageActivity;
import com.fxp.activities.UploadCommentActivity;
import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Food;
import com.fxp.entity.Visited;
import com.fxp.main.MainActivity;
import com.fxp.manager.FoodManager;
import com.fxp.manager.LikeManager;
import com.fxp.manager.SharedPreferenceManager;
import com.fxp.manager.VisitedManager;
import com.fxp.myview.MyImageButton;
import com.fxp.util.DialogUtil;
import com.fxp.util.PictureUtil;
import com.markmao.pulltorefresh.widget.XListView;
import com.moxun.tagcloud.R;

public class HomeFragment extends Fragment {
	private XListView mListView;
	private Handler mHandler;
	ArrayList<Food> foodList;
	ArrayList<ArrayList<String>> picList=new ArrayList<ArrayList<String>>();
	FoodAdapter foodAdapter;
	FoodManager foodManager;
	LikeManager likeManager;
	VisitedManager visitedManager;
	private int foodGroupCount=0;
	Calendar calendar = Calendar.getInstance();
	MainActivity mainActivity;
	public static HomeFragment newInstance() {

		Bundle args = new Bundle();

		HomeFragment fragment = new HomeFragment();
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mainActivity=(MainActivity)getActivity();
		PictureUtil.init(getActivity());
		initFoodManager();
		initFoodList();
		initListView(view);

	}

	private void initFoodManager() {
		likeManager=new LikeManager(getActivity().getContentResolver());
		visitedManager=new VisitedManager(getActivity().getContentResolver());
		foodManager=new FoodManager(getActivity().getContentResolver());
		foodManager.setGroupSize(5);
	}

	private void initListView(View view) {
		mHandler = new Handler();
		mListView = (XListView) view.findViewById(R.id.lv_home_xlistview);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(new ListViewListener());
		mListView.setRefreshTime(getTime());

		foodAdapter = new FoodAdapter(getActivity()
				.getApplicationContext());
		mListView.setAdapter(foodAdapter);
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}
	private void initFoodList() {
		foodList = foodManager.getFoodListFragment(0);
		setPicListWithFoodList();
	}
	private void setPicListWithFoodList(){
		picList.clear();
		for(Food f:foodList){
			picList.add(PictureUtil.getCommonPicturePathList(f.getId()));
			
		}
	}
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	private class ListViewListener implements XListView.IXListViewListener{
		@Override
		public void onRefresh() {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(foodList!=null){
						foodGroupCount=0;
						foodList = foodManager.getFoodListFragment(0);
						setPicListWithFoodList();
						foodAdapter.notifyDataSetChanged();
					}
					onLoad();
				}
			}, 2500);
		}

		@Override
		public void onLoadMore() {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					foodList.addAll(foodManager.getFoodListFragment(++foodGroupCount));
					setPicListWithFoodList();
					foodAdapter.notifyDataSetChanged();
					onLoad();
				}
			}, 2500);
		}
}
	public class FoodAdapter extends BaseAdapter {
		private Context context;

		public FoodAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return foodList.size();
		}

		@Override
		public Object getItem(int position) {
			return foodList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				@SuppressWarnings("static-access")
				LayoutInflater inflater = getActivity().getLayoutInflater()
						.from(context);
				convertView = inflater.inflate(R.layout.item_getfood, null);
				holder = new Holder();
				adapterFindViews(holder, convertView);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			adapterSetViews(holder, position);

			adapterSetListener(holder, position);

			return convertView;

		}

		// 监听
		private void adapterSetListener(Holder holder, int position) {
			// 传入当前微博的id
			holder.myOnClickListener.setFoodId(foodList
					.get(position).getId());
			holder.mIBtn_comment.setOnClickListener(holder.myOnClickListener);
			holder.mIBtn_respot.setOnClickListener(holder.myOnClickListener);
			holder.mIBtn_like.setOnClickListener(holder.myOnClickListener);
			holder.tvComment.setOnClickListener(holder.myOnClickListener);
			//点击图片放大
			ImageOnClickListener imageOnClickListener = new ImageOnClickListener(position);
			for(ImageView iv:holder.nineList){
				iv.setOnClickListener(imageOnClickListener);
			}
			
		}

		private void adapterSetViews(Holder holder, int position) {
			holder.tvText.setText(foodList.get(position).getSummary());
			Log.i("TEST", "source:" + foodList.get(position).getSummary());
			// 昵称
			holder.tvScreen_name
					.setText(foodList.get(position).getName());
			//phone
			holder.tv_getfood_phone
					.setText(foodList.get(position).getPhone());
			// 头像
			holder.ivUser.setImageBitmap(PictureUtil.getFoodHeadPicture(foodList.get(position).getId()));
						
			//address
			holder.tvAddress.setText(foodList.get(position).getAddress());
			// 九宫格
			if (picList.get(position).size()==0) {
				holder.llNine.setVisibility(View.GONE);
			} else {
				holder.llNine.setVisibility(View.VISIBLE);
				holder.llChild1.setVisibility(View.VISIBLE);
				holder.llChild2.setVisibility(View.VISIBLE);
				holder.llChild3.setVisibility(View.VISIBLE);
				if (picList.get(position).size() <= 3) {
					holder.llChild2.setVisibility(View.GONE);
					holder.llChild3.setVisibility(View.GONE);
					setNinePic(3, position, holder);

				} else if (picList.get(position).size() <= 6) {
					holder.llChild3.setVisibility(View.GONE);
					setNinePic(6, position, holder);

				} else {
					setNinePic(9, position, holder);

				}

			}

		}

		private void setNinePic(int count, int position, Holder holder) {
			for (int i = 0; i < count; i++) {
				if (i < picList.get(position).size()) {
					holder.nineList.get(i).setVisibility(View.VISIBLE);
					holder.nineList.get(i).setImageBitmap(BitmapFactory.decodeFile(picList.get(position).get(i)));					
				} else {
					holder.nineList.get(i).setVisibility(View.INVISIBLE);
				}
			}
		}

		private void adapterFindViews(Holder holder, View convertView) {

			holder.tvText = (TextView) convertView.findViewById(R.id.tvText);
			holder.tvAddress = (TextView) convertView
					.findViewById(R.id.tvAddress);
			holder.tvScreen_name = (TextView) convertView
					.findViewById(R.id.tvScreen_name);
			holder.tv_getfood_phone = (TextView) convertView
					.findViewById(R.id.tv_getfood_phone);
			holder.tvComment = (TextView) convertView
					.findViewById(R.id.tvComment);
			holder.ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
			holder.llNine = (LinearLayout) convertView
					.findViewById(R.id.llNine);
			holder.llChild1 = (LinearLayout) convertView
					.findViewById(R.id.llChild1);
			holder.llChild2 = (LinearLayout) convertView
					.findViewById(R.id.llChild2);
			holder.llChild3 = (LinearLayout) convertView
					.findViewById(R.id.llChild3);
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivFirstOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivSecondOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivThirdOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivFourthOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivFifthOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivSixthOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivSeventhOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivEighthOfNine));
			holder.nineList.add((ImageView) convertView
					.findViewById(R.id.ivNinthOfNine));

			holder.mIBtn_comment = (MyImageButton) convertView
					.findViewById(R.id.mIBtn_comment);
			holder.mIBtn_respot = (MyImageButton) convertView
			.findViewById(R.id.mIBtn_respot);
			holder.mIBtn_like = (MyImageButton) convertView
					.findViewById(R.id.mIBtn_like);

		}

	}

	public class Holder {
		TextView tvText, tvSource, tvAddress, tvScreen_name,tvComment,tv_getfood_phone;
		ImageView ivUser;
		LinearLayout llNine, llChild1, llChild2, llChild3;
		ArrayList<ImageView> nineList = new ArrayList<ImageView>();
		MyImageButton mIBtn_comment,mIBtn_respot,mIBtn_like;
		MyOnClickListener myOnClickListener = new MyOnClickListener();
	}

	public class MyOnClickListener implements OnClickListener {
		private int foodId;

		@Override
		public void onClick(View v) {
			Intent intent=null;
			switch (v.getId()) {
				case R.id.mIBtn_comment:
					Log.i("TEST", "onClick-mIBtn_comment");
					intent = new Intent(getActivity()
							.getApplicationContext(), CommentActivity.class);
					if (foodId > 0) {
						Bundle bundle = new Bundle();
						bundle.putInt(ProviderConstant.TFOOD_ID, foodId);
						intent.putExtras(bundle);
					}
					startActivity(intent);
					break;
				case R.id.mIBtn_respot:
					Log.i("TEST", "onClick-mIBtn_respot");
					if(!SharedPreferenceManager.getInstance(getActivity()).checkLoginStatus()) {
						DialogUtil.dialogWithOneButton(getActivity(), "请先登录", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(null!=mainActivity){
									mainActivity.getUiHandler().sendEmptyMessage(3);
								}
								dialog.dismiss();
							}
						});
						break;
					}
					haveVisitedPlusOne(foodId);
					break;
				case R.id.mIBtn_like:
					if(!SharedPreferenceManager.getInstance(getActivity()).checkLoginStatus()) {
						DialogUtil.dialogWithOneButton(getActivity(), "请先登录", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(null!=mainActivity){
									mainActivity.getUiHandler().sendEmptyMessage(3);
								}
								dialog.dismiss();
							}
						});
						break;
					}
					likePlusOne();
					break;
				case R.id.tvComment:
					Log.i("TEST", "onClick-tvComment");

					if(!SharedPreferenceManager.getInstance(getActivity()).checkLoginStatus()) {
						DialogUtil.dialogWithOneButton(getActivity(), "请先登录", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(null!=mainActivity){
									mainActivity.getUiHandler().sendEmptyMessage(3);
								}
								dialog.dismiss();
							}
						});
						break;
					}
					intent = new Intent(getActivity()
							.getApplicationContext(), UploadCommentActivity.class);
					if(foodId>0){
						Bundle bundle = new Bundle();
						bundle.putInt(ProviderConstant.TFOOD_ID, foodId);
						intent.putExtras(bundle);
					}
					startActivity(intent);
					break;
				default:
					break;
			}
		}

		private void haveVisitedPlusOne(int foodId) {
			// TODO Auto-generated method stub
			if(!visitedManager.isAlreadlyVisited(foodId,SharedPreferenceManager.getInstance(getActivity()).getAccId())){
				boolean ret=visitedManager.insertVisited(foodId,SharedPreferenceManager.getInstance(getActivity()).getAccId());
				if(ret){
					Toast.makeText(getActivity().getApplicationContext(),"已经加入足迹列表",Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(getActivity().getApplicationContext(),"在足迹中已经存在咯",Toast.LENGTH_SHORT).show();
			}
			
		}
		private void likePlusOne() {
			//TODO ADD LIKE
			if(!likeManager.isAlreadlyLike(foodId, SharedPreferenceManager.getInstance(getActivity()).getAccId())){
				boolean ret=likeManager.insertLike(foodId, SharedPreferenceManager.getInstance(getActivity()).getAccId());
				if(ret){
					Toast.makeText(getActivity().getApplicationContext(),"已经加入喜爱列表",Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(getActivity().getApplicationContext(),"在喜爱列表中已经存在咯",Toast.LENGTH_SHORT).show();
			}
		}
		public int getFoodId() {
			return foodId;
		}

		public void setFoodId(int foodId) {
			this.foodId = foodId;
		}

	}

	public class ImageOnClickListener implements OnClickListener {
		private int position;
		private int imgPosition;
		ImageOnClickListener(int position){
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ivFirstOfNine:
				Log.i("TEST","ivFirstOfNine");
				imgPosition=0;
				break;
			case R.id.ivSecondOfNine:
				Log.i("TEST","ivSecondOfNine");
				imgPosition=1;
				break;
			case R.id.ivThirdOfNine:
				Log.i("TEST","ivThirdOfNine");
				imgPosition=2;
				break;
			case R.id.ivFourthOfNine:
				Log.i("TEST","ivFourthOfNine");
				imgPosition=3;
				break;
			case R.id.ivFifthOfNine:
				Log.i("TEST","ivFifthOfNine");
				imgPosition=4;
				break;
			case R.id.ivSixthOfNine:
				Log.i("TEST","ivSixthOfNine");
				imgPosition=5;
				break;
			case R.id.ivSeventhOfNine:
				Log.i("TEST","ivSeventhOfNine");
				imgPosition=6;
				break;
			case R.id.ivEighthOfNine:
				Log.i("TEST","ivEighthOfNine");
				imgPosition=7;
				break;
			case R.id.ivNinthOfNine:
				Log.i("TEST","ivNinthOfNine");
				imgPosition=8;
				break;
			default:
				break;
			}
			Intent intent=new Intent(getActivity().getApplicationContext(),LargeImageActivity.class);
			intent.putExtra("imgPosition", imgPosition);
			intent.putExtra("picList", picList.get(position));
			Log.i("TEST", "position--"+position);
			Log.i("TEST", "foodList==null"+(foodList.get(position).getPictruepath()==null));
			startActivity(intent);
			
		}
		
	}

}
