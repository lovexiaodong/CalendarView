package com.ums.drawqixingdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mJump_btn;
    private Button mJump_btn2;
    private CalenderView mCalenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJump_btn = findViewById(R.id.jump_setting);
        mJump_btn2 = findViewById(R.id.jump_setting2);
        mCalenderView = findViewById(R.id.calenderView);
        mCalenderView.setDate(2017, 10, 10);
        mJump_btn.setOnClickListener(this);
        mJump_btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.jump_setting:
            
                break;
            case R.id.jump_setting2
                break;
        }
    }
}
