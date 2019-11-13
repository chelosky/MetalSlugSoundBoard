package com.chelosky.metalslugsoundboard.Adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chelosky.metalslugsoundboard.Models.ItemModel;
import com.chelosky.metalslugsoundboard.R;

import java.util.List;


public class ItemRecyclerView extends RecyclerView.Adapter<ItemRecyclerView.ItemViewHolder> implements View.OnTouchListener, View.OnClickListener {
    private List<ItemModel> listaItems;
    private View.OnTouchListener listenerListener;
    private View.OnClickListener listenerClick;
    public ItemRecyclerView(List<ItemModel> listaItems) {
        this.listaItems = listaItems;
    }

    @NonNull
    @Override
    public ItemRecyclerView.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_option,parent,false);
        view.setOnClickListener(this);
        view.setOnTouchListener(this);
        return new ItemViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listenerClick = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerView.ItemViewHolder holder, int position) {
        ItemModel item = listaItems.get(position);
        holder.imageItem.setImageResource(item.getImageItem());
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    @Override
    public void onClick(View v) {
        if(listenerClick!=null){
            listenerClick.onClick(v);
        }
    }

    public void setOnTouchListener(View.OnTouchListener listener){
        this.listenerListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(listenerListener!=null){
            listenerListener.onTouch(v,event);
        }
        return false;
    }

    public class ItemViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageItem;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = (ImageView)itemView.findViewById(R.id.itemImage);
        }
    }
}
