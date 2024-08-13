package com.example.carveyourchoice.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.carveyourchoice.Adapters.ListAdapters.CategoryListAdapter;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ListCategoriesFragment extends Fragment {
    private DBHelper DB;
    private List<Category> categories;
    private ListView categoryView;
    private CategoryListAdapter categoryAdapter;
    private Context context;
    private String activity_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_categories, container, false);

        context = getContext();
        DB = new DBHelper(context);

        categories = new ArrayList<>();
        DB.getRecipeCategories().forEach(category -> {
            if (category.getName().compareTo("All") != 0){
                categories.add(category);
            }
        });

        activity_name = getActivity().getLocalClassName();

        categoryView = view.findViewById(R.id.categories);
        categoryAdapter = new CategoryListAdapter(context,R.layout.list_category_items,categories,activity_name);
        categoryView.setAdapter(categoryAdapter);

        Button done = view.findViewById(R.id.done);
        Button go_back = view.findViewById(R.id.go_back);

        done.setOnClickListener(v ->{
            switch (activity_name){
                case "Activities.MyRecipe.AddRecipeActivity":
                    Navigation.findNavController(view).navigate(R.id.addRecipeFragment);
                    break;
                case "Activities.EditRecipeActivity":
                    Navigation.findNavController(view).navigate(R.id.editRecipeFragment);
                    break;
            }
        });
        go_back.setOnClickListener(v->{
            switch (activity_name){
                case "Activities.MyRecipe.AddRecipeActivity":
                    Navigation.findNavController(view).navigate(R.id.addRecipeFragment);
                    break;
                case "Activities.EditRecipeActivity":
                    Navigation.findNavController(view).navigate(R.id.editRecipeFragment);
                    break;
            }
        });

        return view;
    }
}