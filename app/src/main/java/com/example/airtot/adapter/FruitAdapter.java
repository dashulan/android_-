package com.example.airtot.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.airtot.R;
import com.example.airtot.entity.Fruit;

import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private Context context;

    private List<Fruit> fruitList;

    private ExplosionField explosionField;

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        if (context == null) {
            context = viewGroup.getContext();
        }
        explosionField = ExplosionField.attach2Window((Activity)context);


        View view = LayoutInflater.from(context).inflate(R.layout.card_fruit_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explosionField.explode(v);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
        Fruit fruit = fruitList.get(i);
        viewHolder.fruitName.setText(fruit.getName());
//        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        Glide.with(context).load(fruit.getImageId()).into(viewHolder.fruitImage);
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder( View itemView) {
            super(itemView);
            cardView =(CardView)itemView;
            fruitImage = (ImageView) itemView.findViewById(R.id.fruit_image);
            fruitName = (TextView) itemView.findViewById(R.id.fruit_name);
        }

    }


    public FruitAdapter(List<Fruit> fruitList) {
        this.fruitList = fruitList;
    }





}
