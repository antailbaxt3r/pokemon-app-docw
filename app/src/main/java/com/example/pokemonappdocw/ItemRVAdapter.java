package com.example.pokemonappdocw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemRVAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    Context context;
    List<Item> itemList;

    public ItemRVAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item_format, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        final Item item = itemList.get(position);
        holder.itemName.setText(item.getItemType());
        holder.itemCount.setText("x" + item.getItemCount());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
