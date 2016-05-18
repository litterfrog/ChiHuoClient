package com.fxp.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;

import com.fxp.base.BaseAppCompatActivity;
import com.fxp.fragments.HomeFragment;
import com.fxp.fragments.LikeFragment;
import com.fxp.fragments.RecentsFragment;
import com.fxp.fragments.SampleFragment;
import com.fxp.fragments.UserInfoFragment;
import com.fxp.fragments.VisitedFragment;
import com.moxun.tagcloud.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;

import java.lang.ref.WeakReference;


public class MainActivity extends BaseAppCompatActivity {
    private BottomBar mBottomBar;
    private UIHandler uiHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        uiHandler=new UIHandler(this);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setFragmentItems(getFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(RecentsFragment.newInstance(), R.drawable.ic_recents, "随便看看"),
                new BottomBarFragment(LikeFragment.newInstance(), R.drawable.ic_favorites, "最爱"),
                new BottomBarFragment(HomeFragment.newInstance(), R.drawable.ic_nearby, "美食"),
                new BottomBarFragment(UserInfoFragment.newInstance(), R.drawable.ic_friends, "我的主页"),
                new BottomBarFragment(VisitedFragment.newInstance(), R.drawable.ic_restaurants, "我吃过的")
        );

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
        mBottomBar.mapColorForTab(4, "#FF9800");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
    public UIHandler getUiHandler(){
        return uiHandler;
    }
    public static class UIHandler extends Handler {
        WeakReference<MainActivity> activity;
        public UIHandler(MainActivity activity){
            this.activity=new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(null==this.activity){
                return;
            }
            switch (msg.what) {
                case 0:
                    this.activity.get().mBottomBar.selectTabAtPosition(0,true);
                    break;
                case 1:
                    this.activity.get().mBottomBar.selectTabAtPosition(1,true);
                    break;
                case 2:
                    this.activity.get().mBottomBar.selectTabAtPosition(2,true);
                    break;
                case 3:
                    this.activity.get().mBottomBar.selectTabAtPosition(3,true);
                    break;
                case 4:
                    this.activity.get().mBottomBar.selectTabAtPosition(4,true);
                    break;
                default:
                    break;
            }
        }
    }
}
