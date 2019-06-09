package com.example.airtot.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

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
    private Boolean btnclicked = false;
    private Integer[] imgs = new Integer[]{R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14};
    private Date alarmDate=null;
    private Boolean isalarm =false;
    private PendingIntent pendingIntent=null;
    private Notes needUpdateNotes = null;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("备忘录");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn = findViewById(R.id.save);
        editText = findViewById(R.id.editText);


        category = getIntent().getStringExtra("category");

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needUpdateNotes != null) {
                    needUpdateNotes.setContent(editText.getText().toString());
                    needUpdateNotes.update(needUpdateNotes.getId());
                    MainActivity.homeAdapter.notifyDataSetChanged();
                    return;
                }
                btnclicked=true;
                Notes notes = new Notes();
                String content = editText.getText().toString();
                notes.setContent(content);
                if (content.length() > 5) {
                    notes.setTitle(content.substring(0, 5));
                    if (content.length() > 10) {
                        notes.setPreview(content.substring(5,10));
                    }else {
                        notes.setPreview(content.substring(5));
                    }
                } else {
                    notes.setTitle(content);
                }
                notes.setCreateTime(new Date(System.currentTimeMillis()));
                notes.setCategory(category);
                Random random = new Random();
                int rid= random.nextInt(14);
                notes.setTitleImg(imgs[rid]);
                notes.setAlarm(isalarm);
                notes.setAlarmTime(alarmDate);
                notes.save();
                MainActivity.items.add(notes);
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
                AlarmActivity.alarmManager.cancel(pendingIntent);
                break;
            case android.R.id.home:
                finish();
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
                }

        }
    }

    @Override
    public void finish() {
        if (editText.getText().toString().length() >0) {
            btn.callOnClick();
        }
        Log.e(Main2Activity.class.toString(), "活动退出");
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        needUpdateNotes =(Notes)getIntent().getSerializableExtra("notes");
        if (needUpdateNotes != null) {
            category = needUpdateNotes.getCategory();
            editText.setText(needUpdateNotes.getContent());
        }
    }


}
