package com.fxp.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxp.activities.CommentActivity;
import com.fxp.activities.LargeImageActivity;
import com.fxp.activities.UploadCommentActivity;
import com.fxp.constants.ProviderConstant;
import com.fxp.entity.Food;
import com.fxp.main.MainActivity;
import com.fxp.manager.FoodManager;
import com.fxp.manager.SharedPreferenceManager;
import com.fxp.myview.MyImageButton;
import com.fxp.util.DialogUtil;
import com.fxp.util.PictureUtil;
import com.markmao.pulltorefresh.widget.XListView;
import com.moxun.tagcloud.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fuxinpeng on 2016/5/18.
 */
public class LikeFragment extends Fragment {
    private MainActivity mainActivity;
    private XListView mListView;
    private Handler mHandler;
    ArrayList<Food> foodList;
    ArrayList<ArrayList<String>> picList=new ArrayList<ArrayList<String>>();
    LikeAdapter likeAdapter;
    FoodManager foodManager;
    private int foodGroupCount=0;
    Calendar calendar = Calendar.getInstance();
    public static LikeFragment newInstance() {

        Bundle args = new Bundle();

        LikeFragment fragment = new LikeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like, container, false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity=(MainActivity)getActivity();
        if(!SharedPreferenceManager.getInstance(getActivity()).checkLoginStatus()){
            DialogUtil.dialogWithOneButton(getActivity(), "请先登录",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    mainActivity.getUiHandler().sendEmptyMessage(3);
                    dialog.dismiss();
                }
            });
            return;
        }

        PictureUtil.init(getActivity());
        initFoodManager();
        initFoodList();
        initListView(view);

    }

    private void initFoodManager() {
        foodManager=new FoodManager(getActivity().getContentResolver());
        foodManager.setGroupSize(5);
    }

    private void initListView(View view) {
        mHandler = new Handler();
        mListView = (XListView) view.findViewById(R.id.lv_like_xlistview);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(new ListViewListener());
        mListView.setRefreshTime(getTime());

        likeAdapter = new LikeAdapter(getActivity()
                .getApplicationContext());
        mListView.setAdapter(likeAdapter);
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
                        likeAdapter.notifyDataSetChanged();
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
                    likeAdapter.notifyDataSetChanged();
                    onLoad();
                }
            }, 2500);
        }
    }
    public class LikeAdapter extends BaseAdapter {
        private Context context;

        public LikeAdapter(Context context) {
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
        }

    }

    public class Holder {
        TextView tvText, tvSource, tvAddress, tvScreen_name,tvComment;
        ImageView ivUser;
        LinearLayout llNine, llChild1, llChild2, llChild3;
        ArrayList<ImageView> nineList = new ArrayList<ImageView>();
        MyImageButton mIBtn_comment,mIBtn_respot;
        MyOnClickListener myOnClickListener = new MyOnClickListener();
    }

    public class MyOnClickListener implements View.OnClickListener {
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
                    haveVisitedPlusOne();
                    break;
                case R.id.tvComment:
                    Log.i("TEST", "onClick-tvComment");
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

        private void haveVisitedPlusOne() {
            // TODO Auto-generated method stub

        }

        public int getFoodId() {
            return foodId;
        }

        public void setFoodId(int foodId) {
            this.foodId = foodId;
        }

    }
    public class ImageOnClickListener implements View.OnClickListener {
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
