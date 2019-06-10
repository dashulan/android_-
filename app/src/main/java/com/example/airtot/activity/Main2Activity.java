package com.example.airtot.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.airtot.MyApplication;
import com.example.airtot.dao.entity.Notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.airtot.R;

import java.util.Date;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {

    private Button btn;
    private EditText editText;

    private Integer[] imgs = new Integer[]{R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14};

    private Date alarmDate=null;
    private Boolean isalarm =false;
    private PendingIntent pendingIntent=null;
    private String category;

    private int senderId =0;
    private Notes oldNotes = null;
    private Notes newNotes;
    private Boolean newOne = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("备忘录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn = findViewById(R.id.save);
        editText = findViewById(R.id.editText);

        category = getIntent().getStringExtra("category");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldNotes != null) {
                    oldNotes.setContent(editText.getText().toString());
                    oldNotes.update(oldNotes.getId());
                    MainActivity.homeAdapter.notifyDataSetChanged();
                    return;
                }
                String content = editText.getText().toString();
                newNotes.setContent(content);
                if (content.length() > 5) {
                    newNotes.setTitle(content.substring(0, 5));
                    if (content.length() > 10) {
                        newNotes.setPreview(content.substring(5,10));
                    }else {
                        newNotes.setPreview(content.substring(5));
                    }
                } else {
                    newNotes.setTitle(content);
                }

                newNotes.setCreateTime(new Date(System.currentTimeMillis()));
                newNotes.setCategory(category);

                Random random = new Random();
                int rid= random.nextInt(14);
                newNotes.setTitleImg(imgs[rid]);
                newNotes.setAlarm(isalarm);
                newNotes.setAlarmTime(alarmDate);
                newNotes.setSenderId(senderId);
                newNotes.save();
                MainActivity.items.add(newNotes);
                MainActivity.homeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_2_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alarm:
                if(editText.getText().toString().length()==0)
                    break;
                Intent intent = new Intent(Main2Activity.this, AlarmActivity.class);
                intent.putExtra("isSetting", isalarm);
                intent.putExtra("category",category);
                intent.putExtra("content",editText.getText().toString());
                startActivityForResult(intent,1);
                break;
            case R.id.quitAlarm:
                quitAlarm(senderId);
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.changeCategor:
                category = "生活";
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    isalarm = data.getBooleanExtra("isAlarm", false);
                    alarmDate = new Date(data.getLongExtra("alarmDate", System.currentTimeMillis()));
                    pendingIntent = data.getParcelableExtra("sender");
                    senderId = data.getIntExtra("senderid",0);
                }

        }
    }

    @Override
    public void finish() {
        if (editText.getText().toString().length() >0) {
            saveNotes();
            Intent intent = new Intent();
            intent.putExtra("newNotes", newNotes);
            setResult(RESULT_OK,intent);
        }
        Log.e(Main2Activity.class.toString(), "活动退出");
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        oldNotes =(Notes)getIntent().getSerializableExtra("notes");
        if (oldNotes != null) {
            category = oldNotes.getCategory();
            senderId = oldNotes.getSenderId();
            editText.setText(oldNotes.getContent());
        }
    }


    public void saveNotes() {
        newNotes = new Notes();

        String content = editText.getText().toString();
        newNotes.setContent(content);
        if (content.length() > 5) {
            newNotes.setTitle(content.substring(0, 5));
            if (content.length() > 10) {
                newNotes.setPreview(content.substring(5,10));
            }else {
                newNotes.setPreview(content.substring(5));
            }
        } else {
            newNotes.setTitle(content);
        }

        newNotes.setCreateTime(new Date(System.currentTimeMillis()));
        newNotes.setCategory(category);

        Random random = new Random();
        int rid= random.nextInt(14);
        newNotes.setTitleImg(imgs[rid]);
        newNotes.setAlarm(isalarm);
        newNotes.setAlarmTime(alarmDate);

        newNotes.setSenderId(senderId);
        newNotes.save();
    }




    public static void quitAlarm(int senderId) {
        AlarmManager alarmManager = (AlarmManager) MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent senderInent = new Intent();
        senderInent.setAction("com.example.airtot.timer");
        PendingIntent newPedingIntnet = PendingIntent.getBroadcast(MyApplication.getContext(), senderId, senderInent, 0);
        alarmManager.cancel(newPedingIntnet);

    }

}
