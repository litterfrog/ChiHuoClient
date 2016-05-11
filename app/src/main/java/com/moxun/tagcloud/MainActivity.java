package com.moxun.tagcloud;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.moxun.tagcloudlib.view.TagCloudView;

public class MainActivity extends AppCompatActivity {

    private TagCloudView tagCloudView;
    private TextTagsAdapter textTagsAdapter;
    private ViewTagsAdapter viewTagsAdapter;
    private VectorTagsAdapter vectorTagsAdapter;
    private String[] data = new String[20];
    private Button btn1;
    private Button btn2;
    private Button btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);
        tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud);
        tagCloudView.setBackgroundColor(Color.TRANSPARENT);
        initData();
        textTagsAdapter = new TextTagsAdapter(data);
        viewTagsAdapter = new ViewTagsAdapter();
        vectorTagsAdapter = new VectorTagsAdapter();

        tagCloudView.setAdapter(textTagsAdapter);

        btn1=(Button)findViewById(R.id.tag_text);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tagCloudView.setAdapter(textTagsAdapter);
            }
        });

        btn2=(Button)findViewById(R.id.tag_view);
        btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        tagCloudView.setAdapter(viewTagsAdapter);
                    }
                });

        btn3=(Button)findViewById(R.id.tag_vector);
        btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        tagCloudView.setAdapter(vectorTagsAdapter);
                    }
                });
    }
    private void initData(){
        for(int i=0;i<data.length;i++){
            data[i]="牛肉干"+i;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.FLAG_PERFORM_NO_CLOSE, Menu.FIRST + 1, 5, "登录").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case Menu.FIRST + 1:
                Intent intent=new Intent();
                intent.setAction("com.fxp.login.LOGIN");
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
