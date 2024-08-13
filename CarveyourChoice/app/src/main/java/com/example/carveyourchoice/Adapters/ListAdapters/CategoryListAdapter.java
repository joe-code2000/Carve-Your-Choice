package com.example.carveyourchoice.Adapters.ListAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carveyourchoice.Activities.EditRecipeActivity;
import com.example.carveyourchoice.Activities.MyRecipe.AddRecipeActivity;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private final Context context;
    private int resource;
    private List<Category> objects;
    private List<TextView> names;
    private String activityName;

    public CategoryListAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.names = new ArrayList<>();
    }
    public CategoryListAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects, String activityName) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.names = new ArrayList<>();
        this.activityName = activityName;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(this.resource,parent,false);

        TextView name = convertView.findViewById(R.id.name);
        name.setText(category.getName());
        names.add(name);
        name.setOnClickListener(v->{
            names.stream().filter(x->x != name).forEach(textView -> {
                textView.setTextColor(Color.WHITE);
            });
            name.setTextColor(Color.rgb(255,90,120));
            if (activityName != null){
                switch (activityName){
                    case "Activities.MyRecipe.AddRecipeActivity":
                        AddRecipeActivity.setSelectedCategory(category);
                        break;
                    case "Activities.EditRecipeActivity":
                        EditRecipeActivity.setSelectedCategory(category);
                        break;
                }
            }
        });

        return convertView;
    }
}
