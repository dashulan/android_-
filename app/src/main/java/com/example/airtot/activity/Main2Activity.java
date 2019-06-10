package com.example.airtot.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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


    private EditText editText;

    private Integer[] imgs = new Integer[]{R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14};

    private String category;

    private Notes oldNotes = null;
    private Notes newNotes = null;
    private Boolean newOne = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(Main2Activity.class.toString(), "活动开始");

        setContentView(R.layout.activity_main2);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("备忘录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = findViewById(R.id.editText);

        newOne = getIntent().getBooleanExtra("newOne", true);
        if (!newOne) {
            oldNotes = (Notes) getIntent().getSerializableExtra("oldNotes");
            editText.setText(oldNotes.getContent());
        }

        category = getIntent().getStringExtra("category");

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
                Notes notes;
                if (newOne) {
                    if (newNotes == null) {
                        newNotes = new Notes();
                    }
                    notes = newNotes;
                } else {
                    notes = oldNotes;
                }
                Intent intent = new Intent(Main2Activity.this, AlarmActivity.class);
                intent.putExtra("notes", notes);
                if (newOne) {
                    startActivityForResult(intent, 1);
                } else {
                    startActivityForResult(intent,2);
                }
                break;
            case R.id.quitAlarm:
                Notes notesToQuit;
                if (newOne) {
                    notesToQuit = newNotes;
                } else {
                    notesToQuit = oldNotes;
                }
                quitAlarm(notesToQuit.getSenderId());
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.changeCategor:
                getCategoryByDialog();
                break;
        }

        return true;
    }

    public void getCategoryByDialog() {
        AlertDialog.Builder dialog =new  AlertDialog.Builder(Main2Activity.this);
        dialog.setTitle("请输入分类名称");
        final EditText editText = new EditText(MyApplication.getContext());
        dialog.setView(editText);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cate = editText.getText().toString();
                category = cate;
                oldNotes.setCategory(category);
                oldNotes.update(oldNotes.getId());
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }


    @Override
    public void finish() {
        if (newOne) {
            if (editText.getText().toString().length() > 0) {
                saveNotes();
                Intent intent = new Intent();
                intent.putExtra("newNotes", newNotes);
                setResult(RESULT_OK, intent);
            }
        } else {
            oldNotes.setContent(editText.getText().toString());
            oldNotes.update(oldNotes.getId());
            Intent intent = new Intent();
            intent.putExtra("oldNotes", oldNotes);
            setResult(RESULT_OK, intent);
        }
        Log.e(Main2Activity.class.toString(), "活动退出");
        super.finish();
    }



    public void saveNotes() {
        if (newNotes == null) {
            newNotes = new Notes();
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
        newNotes.save();
    }




    public static void quitAlarm(int senderId) {
        AlarmManager alarmManager = (AlarmManager) MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent senderInent = new Intent();
        senderInent.setAction("com.example.airtot.timer");
        PendingIntent newPedingIntnet = PendingIntent.getBroadcast(MyApplication.getContext(), senderId, senderInent, 0);
        alarmManager.cancel(newPedingIntnet);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            newNotes = (Notes) data.getSerializableExtra("notes");
        } else {
            oldNotes = (Notes) data.getSerializableExtra("notes");
        }
    }
}
