package com.fxp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxun.tagcloud.R;
import com.moxun.tagcloud.TextTagsAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

/**
 * Created by fuxinpeng on 2016/3/27.
 */
public class RecentsFragment extends Fragment {
    private TagCloudView tagCloudView;
    private TextTagsAdapter textTagsAdapter;
    private String[] data = new String[20];
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
        initData();
        textTagsAdapter = new TextTagsAdapter(data);
        tagCloudView.setAdapter(textTagsAdapter);
        super.onViewCreated(view, savedInstanceState);
    }
    private void initData(){
        for(int i=0;i<data.length;i++){
            data[i]="牛肉干"+i;
        }
    }
}
