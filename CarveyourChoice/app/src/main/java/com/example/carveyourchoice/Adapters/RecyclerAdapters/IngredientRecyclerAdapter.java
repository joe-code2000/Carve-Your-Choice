package com.example.carveyourchoice.Adapters.RecyclerAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.Utilities;

import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Ingredient> ingredients;

    public IngredientRecyclerAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_recycler_item,parent,false);
        return new ItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder view = (ItemViewHolder) holder;
        Ingredient ingredient = ingredients.get(position);
        view.image.setImageDrawable(Drawable.createFromPath(Utilities.loadImage(ingredient.getImage(),context).toString()));
        view.name.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView image;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
