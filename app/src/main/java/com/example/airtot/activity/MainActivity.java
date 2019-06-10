package com.example.airtot.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.airtot.MyApplication;
import com.example.airtot.R;
import com.example.airtot.receiver.TimeReceiver;
import com.example.airtot.adapter.NotesAdapter;
import com.example.airtot.dao.entity.Notes;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ramotion.foldingcell.FoldingCell;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends AppCompatActivity {

    public static final int CREATENEWNOTES =1;
    public static final int UPATEOLDNOTES =2;

    private TimeReceiver timeReceiver;

    static NotesAdapter homeAdapter;

    ExplosionField explosionField;
    private FloatingActionButton fab;
    NavigationView navView;
    private String category ="生活";
    private DrawerLayout mDrawerLayout;
    ImageView imageView;
    private Boolean categoryChanged =true;


    static final ArrayList<Notes> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fab = findViewById(R.id.fab);
        navView = findViewById(R.id.nav);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.airtot.timer");
        timeReceiver = new TimeReceiver();
        registerReceiver(timeReceiver, intentFilter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AirToT");
        mDrawerLayout = findViewById(R.id.drawLayout);
        View view = navView.inflateHeaderView(R.layout.nav_header);

        imageView = view.findViewById(R.id.headImg);
        Glide.with(this).load(R.drawable.p6).into(imageView);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (!categoryChanged) {
                    return;
                }
                List<Notes> list = LitePal.select("category").find(Notes.class);
                Set<String> cates = new HashSet<>();
                for (Notes notes : list) {
                    cates.add(notes.getCategory());
                }
                for (String cate : cates) {
                    navView.getMenu().add(R.id.group1, Menu.NONE, Menu.NONE, cate);
                }
                categoryChanged=false;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        explosionField = ExplosionField.attach2Window(this);
        LitePal.getDatabase();

        homeAdapter = new NotesAdapter(R.layout.item_card, items, this);


        homeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.content_text4:
                        explosionField.explode(view);
                        view.setOnClickListener(null);
                        break;
                    case R.id.cell:
                        ((FoldingCell) view).toggle(false);
                        break;
                    case R.id.updateBtn:
                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        Notes notes = (Notes) adapter.getData().get(position);
                        intent.putExtra("notes", notes);
                        startActivity(intent);
                        break;
                }
            }
        });

        homeAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(final BaseQuickAdapter adapter, final View view, final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("是否删除");
                dialog.setCancelable(true);
                dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Notes notes = (Notes) adapter.getData().get(position);
                        notes.delete();
                        adapter.remove(position);
                        explosionField.explode(view);
                        Main2Activity.quitAlarm(notes.getSenderId());
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                return false;
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(homeAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });


      //  navView.setCheckedItem(R.id.live);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.addCat:
                        menuItem.setCheckable(true);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("请输入要增加的分类名称");
                        final EditText editText = new EditText(MyApplication.getContext());
                        dialog.setView(editText);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String cate = editText.getText().toString();
                                MenuItem menuItem1 = navView.getMenu().add(R.id.group1, Menu.NONE,Menu.NONE,cate);
                                menuItem1.setCheckable(true);
                                menuItem1.setChecked(true);
                                navView.setCheckedItem(menuItem1.getItemId());
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.removeCat:
                        if (navView.getCheckedItem().getItemId() == R.id.addCat || navView.getMenu().size()==0) {
                            break;
                        }
                        final String categoryRemove = navView.getCheckedItem().getTitle().toString();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("谨慎操作");
                        alertDialog.setMessage("是否确定删除:"+categoryRemove);
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                navView.getMenu().removeItem(navView.getCheckedItem().getItemId());
                                List<Notes> notesList = LitePal.where("category=?", categoryRemove).find(Notes.class);
                                for (Notes notes : notesList) {
                                    Main2Activity.quitAlarm(notes.getSenderId());
                                    notes.delete();
                                }
                                homeAdapter.notifyDataSetChanged();
                            }
                        });
                        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                        break;
                    default:
                        List<Notes> notesList = getNotesListByCategory(menuItem.getTitle().toString());
                        homeAdapter.setNewData(notesList);
                        homeAdapter.notifyDataSetChanged();
                        category = menuItem.getTitle().toString();
                        menuItem.setCheckable(true);
                        menuItem.setChecked(true);
//                mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }


    private List<Notes> getNotesListByCategory(String category) {
        List<Notes> notesList = LitePal.where("category=?", category).find(Notes.class);
        return notesList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        homeAdapter.setNewData(getNotesListByCategory(category));
        homeAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CREATENEWNOTES:
                if (resultCode == RESULT_OK) {
                    Notes notes = (Notes) data.getSerializableExtra("newNotes");
                    homeAdapter.addData(notes);
                }
                break;
            case UPATEOLDNOTES:
                if (requestCode == RESULT_OK) {

                }
                break;
            default:
                break;
        }
    }
}
