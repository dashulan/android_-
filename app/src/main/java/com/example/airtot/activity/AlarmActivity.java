package com.example.airtot.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.airtot.MyApplication;
import com.example.airtot.R;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AlarmActivity extends Activity {
    private Button quitBtn;
    private Button saveBtn;
    private Boolean isSetting =false;
    private Date alarmTime;
    public static Button calcleBtn;
    String category;
    String content;
    static AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setFinishOnTouchOutside(true);
        final TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        final DatePicker datePicker = findViewById(R.id.datePicker);
        ((ViewGroup)((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);


        category = getIntent().getStringExtra("category");
        content = getIntent().getStringExtra("content");

        final Calendar now = Calendar.getInstance();
        quitBtn = findViewById(R.id.quit);
        saveBtn = findViewById(R.id.save);
//        calcleBtn = findViewById(R.id.cancleAlarm);
         alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isAlarm", isSetting);
                Long time = alarmTime==null?System.currentTimeMillis():alarmTime.getTime();
                intent.putExtra("alarmDate",time);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


        Random random = new Random(47);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();
                int mon = datePicker.getMonth();
                int year = now.get(Calendar.YEAR);
                int day = datePicker.getDayOfMonth();
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,mon,day,hour,min);
                alarmTime = calendar.getTime();
                Random random = new Random(47);
                int r = random.nextInt(1000);
                int r1 = (int)(Math.random()*1000);

                Intent myIntent = new Intent();
                myIntent.putExtra("category", category);
                myIntent.putExtra("content", content);
                myIntent.setAction("com.example.airtot.timer");

                PendingIntent sender = PendingIntent.getBroadcast(MyApplication.getContext(), r1, myIntent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTime(), sender);
//                calcleBtn.setVisibility(View.VISIBLE);
                isSetting =true;
                Intent intent = new Intent();
                intent.putExtra("isAlarm", isSetting);
                intent.putExtra("alarmDate",alarmTime.getTime());
                intent.putExtra("sender", sender);
                intent.putExtra("senderid",r1);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

//        calcleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isSetting=false;
//            }
//        });
    }

}
