package com.example.airtot.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airtot.R;
import com.example.airtot.entity.Item;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class FoldingCellListAdapter extends RecyclerView.Adapter<FoldingCellListAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private ExplosionField explosionField;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        if (context == null) {
            context = viewGroup.getContext();
        }
        explosionField = ExplosionField.attach2Window((Activity) context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, viewGroup, false);
        final ViewHolder viewHolder =  new ViewHolder(view);
        viewHolder.cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                ((FoldingCell)v).toggle(false);
                registerToggle(pos);
            }
        });
//        if (unfoldedIndexes.contains(i)) {
//            viewHolder.cell.unfold(true);
//        } else {
//            viewHolder.cell.fold(true);
//        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
        Item item = itemList.get(i);
        viewHolder.price.setText(item.getPrice());
        viewHolder.fromAddress.setText(item.getFromAddress());
        viewHolder.toAddress.setText(item.getToAddress());
        viewHolder.pledgePrice.setText(item.getPledgePrice());

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        FoldingCell cell;
        TextView price;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView fromAddress;
        TextView toAddress;

        public ViewHolder(View itemView) {
            super(itemView);
//            cardView =(CardView) itemView;
//            cell = cardView.findViewById(R.id.cell);
//            price = cell.findViewById(R.id.title_text);
//            fromAddress = cell.findViewById(R.id.content_text1);
//            toAddress = cell.findViewById(R.id.content_text2);
//            pledgePrice = cell.findViewById(R.id.content_text3);
//            contentRequestBtn = cell.findViewById(R.id.content_text4);
        }
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }


    public FoldingCellListAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }
}
