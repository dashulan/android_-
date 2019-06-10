package com.example.airtot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.airtot.MyApplication;
import com.example.airtot.activity.AlarmActivity;
import com.example.airtot.activity.MainActivity;

import java.util.Locale;

public class TimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        AlarmActivity.calcleBtn.setVisibility(View.GONE);
        String category = intent.getStringExtra("category");
        String content = intent.getStringExtra("content");
        Toast.makeText(MyApplication.getContext(),"时间到了:"+category+":"+content.substring(0,3)+"...",Toast.LENGTH_LONG).show();

    }
}
