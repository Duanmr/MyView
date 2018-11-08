package com.example.xcroundprogressbardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.xcroundprogressbardemo.view.AnswerChartView;
import com.example.xcroundprogressbardemo.view.MyProgress;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int mTotalProgress = 90;
    private int mCurrentProgress = 0;
    //进度条
    private CompletedView mTasksView;
    private AnswerChartView roundProgressBar2;

    private MyProgress MyProgress;
    private List<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        List<Double> floats = new ArrayList<>();
//        floats.add(400.08d);
//        floats.add(500d);
        roundProgressBar2.setAngle(90);

        strings = new ArrayList<>();
        strings.add("live");
        strings.add("live1");
        strings.add("live2");
        strings.add("live3");
        MyProgress = findViewById(R.id.MyProgress);
        MyProgress.setProgress(30);
        MyProgress.setArrStr(strings);
    }


}
