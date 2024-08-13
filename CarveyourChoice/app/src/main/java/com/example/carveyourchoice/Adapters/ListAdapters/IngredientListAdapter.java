package com.example.carveyourchoice.Adapters.ListAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.carveyourchoice.Activities.EditRecipeActivity;
import com.example.carveyourchoice.Activities.MyRecipe.AddRecipeActivity;
import com.example.carveyourchoice.Activities.Grocery.GroceryDetailActivity;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.Utilities;

import java.util.List;

public class IngredientListAdapter extends ArrayAdapter<Ingredient>{
    private Context context;
    private int resource;
    private List<Ingredient> objects;
    private int layout = -1;
    private String activityName;

    public IngredientListAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    public IngredientListAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> objects,String activityName) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.activityName = activityName;

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ingredient ingredient = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(this.resource,parent,false);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        if (ingredient.getImage() != null){
            image.setImageDrawable(Drawable.createFromPath(Utilities.loadImage(ingredient.getImage(),context).toString()));
        }
        TextView nameTxtView = convertView.findViewById(R.id.name);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        nameTxtView.setText(ingredient.getName());

        if (activityName != null){
            switch (activityName){
                case "Activities.Grocery.GroceryDetailActivity":
                    GroceryDetailActivity.getIngredients().forEach(x->{
                        if (x.getId() == ingredient.getId()){
                            checkBox.setChecked(true);
                        }
                    });
                    checkBox.setOnClickListener(view -> {
                        if (checkBox.isChecked()){
                            GroceryDetailActivity.addIngredient(ingredient);
                        }else {
                            GroceryDetailActivity.removeIngredient(ingredient.getId());
                        }
                    });
                    break;
                case "Activities.MyRecipe.AddRecipeActivity":
                    AddRecipeActivity.getSelected_ingredients().forEach(x->{
                        if (x.getId() == ingredient.getId()){
                            checkBox.setChecked(true);
                        }
                    });
                    checkBox.setOnClickListener(view -> {
                        if (checkBox.isChecked()){
                            AddRecipeActivity.addToSelectedIngredients(ingredient);
                        }else {
                            AddRecipeActivity.removeFromSelectedIngredients(ingredient.getId());
                        }
                    });
                    break;
                case "Activities.EditRecipeActivity":
                    EditRecipeActivity.getSelected_ingredients().forEach(x->{
                        if (x.getId() == ingredient.getId()){
                            checkBox.setChecked(true);
                        }
                    });
                    checkBox.setOnClickListener(view -> {
                        if (checkBox.isChecked()){
                            EditRecipeActivity.addToSelectedIngredients(ingredient);
                        }else {
                            EditRecipeActivity.removeFromSelectedIngredients(ingredient.getId());
                        }
                    });
                    break;
            }
        }


        return convertView;
    }

}
