package com.example.carveyourchoice.Adapters.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Fragments.SelectIngredientFragment;
import com.example.carveyourchoice.MainActivity;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Context context;
    private final List<Category> categories;
    private List<TextView> names;
    private String activity_name;

    public CategoryRecyclerAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.names = new ArrayList<>();
    }
    public CategoryRecyclerAdapter(Context context, List<Category> categories,String activity_name) {
        this.context = context;
        this.categories = categories;
        this.names = new ArrayList<>();
        this.activity_name = activity_name;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycler_item,parent,false);
        return new ItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder view = (ItemViewHolder) holder;
        Category category = categories.get(position);
        view.name.setText(category.getName());
        names.add(view.name);
        if (category.getName().compareTo("All") == 0){
            if (activity_name != null){
                switch (activity_name){
                    case "MainActivity":
                        MainActivity.filterRecipes(category.getName().toLowerCase());
                        break;
                    case "Activities.Grocery.GroceryDetailActivity":
                    case "Activities.MyRecipe.AddRecipeActivity":
                    case "Activities.EditRecipeActivity":
                        SelectIngredientFragment.filterIngredients(category.getId());
                        break;
                }
            }
            view.name.setBackgroundResource(R.drawable.rounded_corner_2);
        }
        view.itemView.setOnClickListener(v->{
            names.stream()
            .filter(x->x.getText().toString().toLowerCase().compareTo(category.getName().toLowerCase())!=0)
            .forEach(n->{
                n.setBackgroundResource(R.drawable.rounded_corner);
            });
            view.name.setBackgroundResource(R.drawable.rounded_corner_2);

            if (activity_name != null){
                switch (activity_name){
                    case "MainActivity":
                        MainActivity.filterRecipes(category.getName().toLowerCase());
                        break;
                    case "Activities.Grocery.GroceryDetailActivity":
                    case "Activities.MyRecipe.AddRecipeActivity":
                    case "Activities.EditRecipeActivity":
                        SelectIngredientFragment.filterIngredients(category.getId());
                        break;
                }
            }


        });
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }

    }
}
