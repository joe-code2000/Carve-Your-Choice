package com.example.carveyourchoice.Activities.MyRecipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.Grocery.GroceryActivity;
import com.example.carveyourchoice.Activities.MealPlanner.MealPlannerActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.RecipesRecyclerAdapter;
import com.example.carveyourchoice.MainActivity;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyRecipeActivity extends AppCompatActivity {
    private DBHelper DB;
    private List<Recipe> recipes;
    private Context context;
    private RecyclerView recipesRecycler;
    private RecipesRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
        context = getApplicationContext();
        initializeBottomNav();
        DB = new DBHelper(getApplicationContext());
        recipes = new ArrayList<>();
        addRecipes();

        Button add_recipe_fab = findViewById(R.id.add_recipe_btn);

        add_recipe_fab.setOnClickListener(v->{
            Intent intent = new Intent(context,AddRecipeActivity.class);
            startActivity(intent);
        });

        recipesRecycler = findViewById(R.id.recipes);
        recipesRecycler.setLayoutManager(new GridLayoutManager(context,2));
        recyclerAdapter = new RecipesRecyclerAdapter(context,recipes,"add");
        recipesRecycler.setAdapter(recyclerAdapter);
    }
    private void initializeBottomNav(){
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setSelectedItemId(R.id.myRecipe_nav);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_nav:
                    startActivity(new Intent(context, MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.myRecipe_nav:

                    return true;
                case R.id.grocery_nav:
                    startActivity(new Intent(context, GroceryActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.mealPlanner_nav:
                    startActivity(new Intent(context, MealPlannerActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }


    private void addRecipes(){
        recipes = DB.getRecipes(2).stream().collect(Collectors.toList());
    }


    @Override
    protected void onResume() {
        super.onResume();
        recipes = new ArrayList<>();
        addRecipes();
        recyclerAdapter = new RecipesRecyclerAdapter(context,recipes,"add");
        recipesRecycler.setAdapter(recyclerAdapter);
    }
}