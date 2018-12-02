package com.example.pindao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.library.ChannelActivity;
import com.andy.library.ChannelBean;
import com.example.pindao.fragment.AFragment;
import com.example.pindao.fragment.BFragment;
import com.example.pindao.fragment.CFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout Tab_Layout;
    private TextView Add_Fragment;
    private ViewPager vp;
    List<Fragment> mList=new ArrayList<>();
    List<ChannelBean> channelBeans=new ArrayList<>();
    String jsonStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        channelBeans.add(new ChannelBean("热点",true));
        channelBeans.add(new ChannelBean("小伙儿",true));
        channelBeans.add(new ChannelBean("娱乐",true));
        channelBeans.add(new ChannelBean("军事",true));
        channelBeans.add(new ChannelBean("游戏",false));
        channelBeans.add(new ChannelBean("外卖",false));
        mList.add(new AFragment());
        mList.add(new BFragment());
        mList.add(new CFragment());
        mList.add(new AFragment());
        mList.add(new BFragment());
        mList.add(new CFragment());
        for (int i = 0; i < channelBeans.size(); i++) {
            if(channelBeans.get(i).isSelect()){
                Tab_Layout.addTab(Tab_Layout.newTab().setText(channelBeans.get(i).getName()));
            }
        }
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mList.get(i);
            }

            @Override
            public int getCount() {
                int count=0;
                for (int i = 0; i <channelBeans.size() ; i++) {
                    if(channelBeans.get(i).isSelect()){
                        count++;
                    }

                }
                return count;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return channelBeans.get(position).getName();
            }
        });
        Tab_Layout.setupWithViewPager(vp);
    }

    private void initView() {
        Tab_Layout = (TabLayout) findViewById(R.id.Tab_Layout);
        Add_Fragment = (TextView) findViewById(R.id.Add_Fragment);
        vp = (ViewPager) findViewById(R.id.vp);
        Add_Fragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Add_Fragment:
                ChannelActivity.startChannelActivity(MainActivity.this,channelBeans);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ChannelActivity.REQUEST_CODE && resultCode==ChannelActivity.RESULT_CODE){
            jsonStr=data.getStringExtra(ChannelActivity.RESULT_JSON_KEY);
            Toast.makeText(this, jsonStr, Toast.LENGTH_SHORT).show();
            Tab_Layout.removeAllTabs();
            Gson gson=new Gson();
            Type type=new TypeToken<List<ChannelBean>>(){

            }.getType();
            channelBeans=gson.fromJson(jsonStr,type);
            for (int i = 0; i <channelBeans.size(); i++) {
                if (channelBeans.get(i).isSelect()){
                    try {
                        Tab_Layout.addTab(Tab_Layout.newTab().setText(channelBeans.get(i).getName()));
                    }catch (Exception e){
                    }
                }

            }
            vp.getAdapter().notifyDataSetChanged();




        }
    }
}
