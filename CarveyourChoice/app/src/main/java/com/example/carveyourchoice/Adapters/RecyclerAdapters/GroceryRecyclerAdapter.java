package com.example.carveyourchoice.Adapters.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.Grocery.GroceryDetailActivity;
import com.example.carveyourchoice.Models.Grocery;
import com.example.carveyourchoice.R;

import java.util.ArrayList;
import java.util.List;

public class GroceryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Grocery> groceries = new ArrayList<>();

    public GroceryRecyclerAdapter(Context context, List<Grocery> groceries) {
        this.context = context;
        this.groceries = groceries;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_recycler_item,parent,false);
        return new ItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder view = (ItemViewHolder) holder;
        Grocery grocery = groceries.get(position);
        if (grocery.getName() != null){
            view.name.setText(grocery.getName());
        }
        view.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, GroceryDetailActivity.class);
            intent.putExtra("Id",grocery.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.groceries.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
