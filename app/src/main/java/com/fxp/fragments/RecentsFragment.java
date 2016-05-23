package com.fxp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxp.entity.Food;
import com.fxp.main.MainActivity;
import com.fxp.manager.FoodManager;
import com.moxun.tagcloud.R;
import com.moxun.tagcloud.TextTagsAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by fuxinpeng on 2016/3/27.
 */
public class RecentsFragment extends Fragment {
    private int groupCount=0;
    private FoodManager foodManager;
    private TagCloudView tagCloudView;
    private TextTagsAdapter textTagsAdapter;
    private String[] data = new String[20];
    private ArrayList<String> labels=new ArrayList<String>();
    public RecentsFragment(){}
    public static RecentsFragment newInstance() {

        Bundle args = new Bundle();

        RecentsFragment fragment = new RecentsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recents,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud);
        tagCloudView.setBackgroundColor(Color.TRANSPARENT);
        initFoodManager();
        initData();
        textTagsAdapter = new TextTagsAdapter(data);
        tagCloudView.setAdapter(textTagsAdapter);
        textTagsAdapter.setOnTextClickListener(new TextTagsAdapter.OnTextClickListener() {
            @Override
            public void onTextClick(int position) {
                MainActivity activity = (MainActivity) getActivity();
                Message message=new Message();
                Bundle bundle = new Bundle();
                bundle.putString("tag", data[position]);
                message.setData(bundle);
                message.what=2;
                activity.getUiHandler().sendMessage(message);
                Toast.makeText(getActivity().getApplicationContext(), data[position], Toast.LENGTH_SHORT).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void initFoodManager() {
        foodManager=new FoodManager(getActivity().getContentResolver());
        foodManager.setGroupSize(5);
    }

    private void initData(){
        for(int i=0;i<data.length;i++){
            data[i]="未定义"+i;
        }
        for(;labels.size()<=20&&groupCount<5;groupCount++){
            ArrayList<Food> foods=foodManager.getFoodListFragment(groupCount);
            if(foods.size()==0){
                break;
            }
            for(Food food:foods){
                Collections.addAll(labels,food.getLabel().split(","));
                if(labels.size()>20){
                    break;
                }
            }
        }
        for(int i=0;i<data.length&&i<labels.size();i++){
            data[i]=labels.get(i);
        }

    }
}
