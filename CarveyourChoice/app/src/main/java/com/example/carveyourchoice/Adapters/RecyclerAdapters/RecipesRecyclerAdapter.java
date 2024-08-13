package com.example.carveyourchoice.Adapters.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.EditRecipeActivity;
import com.example.carveyourchoice.Activities.RecipeDetailActivity;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.Utilities;

import java.util.List;

public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Recipe> recipes;
    private String layout;

    public RecipesRecyclerAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public RecipesRecyclerAdapter(Context context, List<Recipe> recipes, String layout) {
        this.context = context;
        this.recipes = recipes;
        this.layout = layout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_item,parent,false);
        return new ItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder view = (ItemViewHolder) holder;
        Recipe recipe = recipes.get(position);
        view.name.setText(recipe.getName());

        if(recipe.getImage() != null){
            view.image.setImageURI(Utilities.getImageUri(context,Utilities.loadImage(recipe.getImage(),context)));
        }
        int ingredientSize = recipe.getIngredients().size();
        String ingredientCount = ingredientSize < 2 ? ingredientSize + " Ingredient": ingredientSize + " Ingredients";
        view.ingredient_count.setText(ingredientCount);
        view.duration.setText(recipe.getDuration());
        view.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("id",recipe.getId());
            if (layout != null){
                switch (layout.split(",")[0]){
                    case "delete":
                        intent.putExtra("delete",this.layout);
                        intent.putExtra("activity_name","Activities.MealPlanner.MealPlannerActivity");
                        break;
                    case "add":
                        intent.putExtra("add","add");
                        break;
                }
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView name,ingredient_count,duration;
        private ImageView image;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            ingredient_count = (TextView) itemView.findViewById(R.id.ingredient_count);
            duration = (TextView) itemView.findViewById(R.id.duration);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
