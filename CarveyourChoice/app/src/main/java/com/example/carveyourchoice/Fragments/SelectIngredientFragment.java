package com.example.carveyourchoice.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Adapters.ListAdapters.IngredientListAdapter;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.CategoryRecyclerAdapter;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectIngredientFragment extends Fragment {
    private static Context context;
    private DBHelper DB;
    private RecyclerView categoryView;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private List<Category> categories;
    private static List<Ingredient> ingredients,tempIngredients;
    private static ListView ingredientListView;
    private static IngredientListAdapter ingredientsAdapter;
    private static String activity_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_ingredient, container, false);
        context = getContext();
        DB = new DBHelper(context);

        //Initialize categories list
        categories = new ArrayList<>();
        //Add categories from the sqlite database
        DB.getIngredientCategories().forEach(category -> {
            categories.add(category);
        });

        //Initialize tempIngredients and ingredients list
        tempIngredients = new ArrayList<>();
        ingredients = new ArrayList<>();

        //Add ingredients from the sqlite database
        DB.getIngredients().forEach(ingredient -> {
            ingredients.add(ingredient);
        });

        //initialize activity_name with the current activity local class name
        activity_name = getActivity().getLocalClassName();

        //Initialize ingredientListView
        ingredientListView = view.findViewById(R.id.list_ingredient_item);

        //Initialize ingredientsAdapter
        ingredientsAdapter = new IngredientListAdapter(getContext(),R.layout.select_ingredient_item,tempIngredients,activity_name);

        //Set ingredientListView adapter to ingredientsAdapter
        ingredientListView.setAdapter(ingredientsAdapter);

        //Initialize categoryView
        categoryView = view.findViewById(R.id.categories);

        //Set categoryView layout manager
        categoryView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        //Initialize categoryRecyclerAdapter
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(context,categories,activity_name);

        //Set ingredientListView adapter to ingredientsAdapter
        categoryView.setAdapter(categoryRecyclerAdapter);

        Button done = view.findViewById(R.id.done);
        Button go_back = view.findViewById(R.id.go_back);

        //Finish selecting ingredients button
        done.setOnClickListener(v ->{
            //Switch fragments function
            switchFragments(view,activity_name);
        });
        //Go to the previous fragment
        go_back.setOnClickListener(v->{
            //Switch fragments function
            switchFragments(view,activity_name);
        });


        return view;
    }
    //Switch fragments function
    private void switchFragments(View view,String activity_name){
        switch (activity_name){
            case "Activities.Grocery.GroceryDetailActivity":
                //Opens GroceryDetailFragment
                Navigation.findNavController(view).navigate(R.id.groceryDetailFragment);
                break;
            case "Activities.MyRecipe.AddRecipeActivity":
                //Opens AddRecipeFragment
                Navigation.findNavController(view).navigate(R.id.addRecipeFragment);
                break;
            case "Activities.EditRecipeActivity":
                //Opens EditRecipeFragment
                Navigation.findNavController(view).navigate(R.id.editRecipeFragment);
                break;
        }
    }
    //Switch filter ingredients depending on the category id selected
    public static void filterIngredients(int id){
        tempIngredients.clear();
        if (id == 1){
            ingredients.forEach(ingredient -> {
                    tempIngredients.add(ingredient);
            });
        }else{
            ingredients.forEach(ingredient -> {
                if (ingredient.getCategory().getId() == id){
                    tempIngredients.add(ingredient);
                }
            });
        }
        //Reinitialize ingredientsAdapter
        ingredientsAdapter = new IngredientListAdapter(context,R.layout.select_ingredient_item,tempIngredients,activity_name);

        //Reset ingredientListView adapter to ingredientsAdapter
        ingredientListView.setAdapter(ingredientsAdapter);
    }
}