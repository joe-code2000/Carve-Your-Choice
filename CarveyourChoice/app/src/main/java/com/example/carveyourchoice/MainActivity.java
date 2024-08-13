package com.example.carveyourchoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.carveyourchoice.Activities.Grocery.GroceryActivity;
import com.example.carveyourchoice.Activities.MealPlanner.MealPlannerActivity;
import com.example.carveyourchoice.Activities.MyRecipe.MyRecipeActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.CategoryRecyclerAdapter;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.RecipesRecyclerAdapter;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private static List<Recipe> recipes, tempRecipes;
    private List<Category> categories;
    private CategoryRecyclerAdapter categoryAdapter;
    private static RecipesRecyclerAdapter recipeAdapter;
    private RecyclerView categoryView, recipeView;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        initializeBottomNav();
        DB = new DBHelper(getApplicationContext());
        DB.resetDatabase();

        categories = new ArrayList<>();
        DB.getRecipeCategories().forEach(category -> {
            categories.add(category);
        });

        categoryView = findViewById(R.id.categories);
        categoryView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        categoryAdapter = new CategoryRecyclerAdapter(getApplicationContext(),categories,getLocalClassName());
        categoryView.setAdapter(categoryAdapter);

        recipes = new ArrayList<>();
        addRecipes();
        tempRecipes = recipes.stream().collect(Collectors.toList());

        recipeView = findViewById(R.id.recipes);
        recipeView.setLayoutManager(new GridLayoutManager(this,2));
        recipeAdapter = new RecipesRecyclerAdapter(getApplicationContext(),tempRecipes,"add");
        recipeView.setAdapter(recipeAdapter);
    }

    private void initializeBottomNav(){
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setSelectedItemId(R.id.home_nav);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_nav:
                    return true;
                case R.id.myRecipe_nav:
                    startActivity(new Intent(getApplicationContext(), MyRecipeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.grocery_nav:
                    startActivity(new Intent(getApplicationContext(), GroceryActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.mealPlanner_nav:
                    startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }

    private void addRecipes(){
        recipes = DB.getRecipes().stream().collect(Collectors.toList());
    }
    public static void filterRecipes(String category){
        tempRecipes.clear();
        if (category.compareTo("all") == 0){
            recipes.stream().collect(Collectors.toList()).forEach(recipe -> {
                tempRecipes.add(recipe);
            });
        }else {
            recipes.stream()
                    .filter(x->x.getCategory().getName().toLowerCase().compareTo(category) == 0)
                    .forEach(recipe -> {
                        tempRecipes.add(recipe);
                    });
        }
        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recipes = new ArrayList<>();
        addRecipes();
        recipeAdapter = new RecipesRecyclerAdapter(context,tempRecipes,"add");
        recipeView.setAdapter(recipeAdapter);
    }

}