package com.example.carveyourchoice.Adapters.RecyclerAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.EditRecipeActivity;
import com.example.carveyourchoice.Activities.Grocery.GroceryDetailActivity;
import com.example.carveyourchoice.Activities.MyRecipe.AddRecipeActivity;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.Utilities;

import java.util.List;

public class SelectedIngredientRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Ingredient> ingredients;
    private String activity_classname;

    public SelectedIngredientRecyclerAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    public SelectedIngredientRecyclerAdapter(Context context, List<Ingredient> ingredients, String activity_classname) {
        this.context = context;
        this.ingredients = ingredients;
        this.activity_classname = activity_classname;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ingredient_items,parent,false);
        return new ItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder view = (ItemViewHolder) holder;
        Ingredient ingredient = ingredients.get(position);
        if (ingredient.getImage() != null){
            view.image.setImageDrawable(Drawable.createFromPath(Utilities.loadImage(ingredient.getImage(),context).toString()));
        }
        view.name.setText(ingredient.getName());
        view.deleteBtn.setOnClickListener(v ->{
            if(activity_classname != null){
                switch (activity_classname){
                    case "Activities.Grocery.GroceryDetailActivity":
                        ingredients.remove(ingredient);
                        GroceryDetailActivity.removeIngredient(ingredient.getId());
                        break;
                    case "Activities.EditRecipeActivity":
                        ingredients.remove(ingredient);
                        EditRecipeActivity.removeFromSelectedIngredients(ingredient.getId());
                        break;
                    case "Activities.MyRecipe.AddRecipeActivity":
                        ingredients.remove(ingredient);
                        AddRecipeActivity.removeFromSelectedIngredients(ingredient.getId());
                        break;
                }
                this.notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private Button deleteBtn;
        private ImageView image;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            deleteBtn = (Button)itemView.findViewById(R.id.delete);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
