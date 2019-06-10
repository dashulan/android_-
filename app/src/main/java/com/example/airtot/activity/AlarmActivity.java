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
import com.example.airtot.dao.entity.Notes;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AlarmActivity extends Activity {
    private Button quitBtn;
    private Button saveBtn;

    private Date alarmTime;

    static AlarmManager alarmManager;
    private Notes notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setFinishOnTouchOutside(true);
        final TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        final DatePicker datePicker = findViewById(R.id.datePicker);


        notes = (Notes) getIntent().getSerializableExtra("notes");

        ((ViewGroup)((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);


        final Calendar now = Calendar.getInstance();
        quitBtn = findViewById(R.id.quit);
        saveBtn = findViewById(R.id.save);

         alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();
                int mon = datePicker.getMonth();
                int year = now.get(Calendar.YEAR);
                int day = datePicker.getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,mon,day,hour,min);
                alarmTime = calendar.getTime();

                int r1 = (int)(Math.random()*1000);
                setAlarm(r1);
                finish();
            }
        });

    }

    public void setAlarm(int senderId) {
        Intent myIntent = new Intent();
        notes.setSenderId(senderId);
        notes.update(notes.getId());
        myIntent.putExtra("category", notes.getCategory());
        myIntent.putExtra("content", notes.getContent());
        myIntent.setAction("com.example.airtot.timer");
        PendingIntent sender = PendingIntent.getBroadcast(MyApplication.getContext(), senderId, myIntent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTime(), sender);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("notes", notes);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
