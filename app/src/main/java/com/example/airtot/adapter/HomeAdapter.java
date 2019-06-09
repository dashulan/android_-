package com.example.airtot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.airtot.R;
import com.example.airtot.activity.Main2Activity;
import com.example.airtot.activity.MainActivity;
import com.example.airtot.entity.Item;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<Item, BaseViewHolder> {

    private Context pContext;


    public HomeAdapter(int layoutResId, @Nullable List<Item> data,Context context) {
        super(layoutResId, data);
        pContext = context;
    }



    @Override
    protected void convert(BaseViewHolder helper, Item item) {

    }

}
