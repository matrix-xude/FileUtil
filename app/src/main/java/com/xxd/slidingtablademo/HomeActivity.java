package com.xxd.slidingtablademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private EditText et_demo;
    private SlidingTabLayout st_demo, sl_2;
    private SegmentTabLayout st_st;
    private ViewPager vp_demo;
    private ArrayList<Fragment> list;

    private String names[] = new String[]{"创建文件", "删除文件", "解压文件"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  1:

                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(HomeActivity.this,"授权通过",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initData() {

        String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        final int permission = ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    HomeActivity.this,
                    PERMISSIONS_STORAGE,
                    1
            );
        }

        vp_demo.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return names.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                TextView tv = new TextView(getApplicationContext());
                tv.setTextSize(30);
                tv.setPadding(200, 50, 0, 0);
                tv.setTextColor(Color.BLACK);
                tv.setText(names[position]);


                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File directory = Environment.getExternalStorageDirectory();
                        if(position == 0){

                            try {
                                FileUtil.writeFile(directory + "/aaa/1.txt","我厉害");
                                FileUtil.writeFile(directory + "/aaa/2.txt","我厉害");
                                FileUtil.writeFile(directory + "/aaa/1/1.txt","我厉害");
                                FileUtil.writeFile(directory + "/aaa/2/2.txt","我厉害");
                                FileUtil.writeFile(directory + "/aaa/3/4/4.txt","我厉害");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(position == 1){
                            FileUtil.deleteFile(directory + "/aaa");
                        }else {
                            try {
                                FileUtil.unZipFolder(directory+"/aoe/a.zip",directory+"/abc");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                container.addView(tv);
                return tv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        st_demo.setViewPager(vp_demo, names);

    }

    private void initView() {
        et_demo = (EditText) findViewById(R.id.et_demo);
        st_demo = (SlidingTabLayout) findViewById(R.id.st_demo);
        sl_2 = (SlidingTabLayout) findViewById(R.id.sl_2);
        vp_demo = (ViewPager) findViewById(R.id.vp_demo);
        st_st = (SegmentTabLayout) findViewById(R.id.st_st);
    }
}
