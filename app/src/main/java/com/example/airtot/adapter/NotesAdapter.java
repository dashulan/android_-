package com.example.airtot.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.airtot.R;
import com.example.airtot.dao.entity.Notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.SimpleFormatter;

public class NotesAdapter extends BaseQuickAdapter<Notes, BaseViewHolder> {

    private DateFormat dateFormat = new SimpleDateFormat("MM月dd日");
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private Context pContext;

    public NotesAdapter(int layoutResId, @Nullable List<Notes> data, Context pContext) {
        super(layoutResId, data);
        this.pContext = pContext;
    }



    @Override
    protected void convert(BaseViewHolder helper, Notes item) {
        helper.setText(R.id.title_text, item.getTitle())
                .setText(R.id.create_date, dateFormat.format(item.getCreateTime()))
                .setText(R.id.wordSum, String.valueOf(item.getContent().length()))
                .setText(R.id.create_time, timeFormat.format(item.getCreateTime()))
                .setText(R.id.title_pre, item.getPreview());

        helper.setText(R.id.contentMain, item.getContent());

        helper.addOnClickListener(R.id.cell);
        helper.addOnClickListener(R.id.content_text4)
                .addOnLongClickListener(R.id.cell);
//        helper.addOnLongClickListener(R.id.content_text3)
        helper.addOnClickListener(R.id.updateBtn);


        ImageView view = helper.getView(R.id.imgTitle);
        Glide.with(pContext).load(item.getTitleImg()).into(view);

    }
}
