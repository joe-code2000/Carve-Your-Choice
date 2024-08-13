package com.example.carveyourchoice.Activities.MealPlanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.Grocery.GroceryActivity;
import com.example.carveyourchoice.Activities.MyRecipe.MyRecipeActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.DatesRecyclerAdapter;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.RecipesRecyclerAdapter;
import com.example.carveyourchoice.MainActivity;
import com.example.carveyourchoice.Models.MealPlanner;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.example.carveyourchoice.Utilities.Utilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MealPlannerActivity extends AppCompatActivity {
    private DBHelper DB;
    private static final String BREAKFAST_LIAR = "breakfast";
    private static final String LUNCH_LIAR = "lunch";
    private static final String DINNER_LIAR = "dinner";
    private static final String directory = "jsonDirectory";
    private static final String filename= "meal_planner.json";
    private static List<Recipe> recipes;
    private static List<MealPlanner> mealPlanners = new ArrayList<>();
    private static Context context;
    private DatesRecyclerAdapter datesAdapter;
    private RecyclerView datesRecyclerView;
    private static RecyclerView breakfast_recipesView,lunch_recipesView,dinner_recipesView;
    private static final String today = Utilities.getTodayDateStr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);
        //Initialize bottom navigation
        initializeBottomNav();
        context = getApplicationContext();
        DB = new DBHelper(context);
        initialiseDateRecycler();
        recipes = new ArrayList<>();
        DB.getRecipes().forEach(recipe -> {
            recipes.add(recipe);
        });
        loadJson();
        if (getIntent().hasExtra("add")){
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("add"));
                String date = jsonObject.getString("Date");
                boolean breakfast = jsonObject.getBoolean("Breakfast");
                boolean lunch = jsonObject.getBoolean("Lunch");
                boolean dinner = jsonObject.getBoolean("Dinner");
                int id = jsonObject.getInt("Id");
                Recipe recipe = getRecipe(id);
                if (recipe != null){
                    if (breakfast){
                        getMealPlanner(date).addToBreakfast(recipe);
                    }else{
                        getMealPlanner(date).removeFromBreakfast(recipe.getId());
                    }

                    if (lunch){
                        getMealPlanner(date).addToLunch(recipe);
                    }else {
                        getMealPlanner(date).removeFromLunch(recipe.getId());
                    }

                    if (dinner){
                        getMealPlanner(date).addToDinner(recipe);
                    }else {
                        getMealPlanner(date).removeFromDinner(recipe.getId());
                    }
                }

                saveJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (getIntent().hasExtra("delete")){
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("delete"));
                String date = jsonObject.getString("Date");
                String liar = jsonObject.getString("Liar");
                int id = jsonObject.getInt("Id");
                Recipe recipe = getRecipe(id);
                if(recipe != null){
                    switch (liar){
                        case BREAKFAST_LIAR:
                            getMealPlanner(date).removeFromBreakfast(recipe.getId());
                            break;
                        case LUNCH_LIAR:
                            getMealPlanner(date).removeFromLunch(recipe.getId());
                            break;
                        case DINNER_LIAR:
                            getMealPlanner(date).removeFromDinner(recipe.getId());
                            break;
                    }
                }
                saveJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        initializeRecipeRecyclers();
        setMealPlanner(today);
    }

    private void initializeBottomNav(){
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setSelectedItemId(R.id.mealPlanner_nav);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_nav:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
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

                    return true;
            }
            return false;
        });
    }

    public void initialiseDateRecycler(){
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            String date =  Utilities.dateAdder(today,i);
            mealPlanners.add(new MealPlanner(date));
            dates.add(date);
        }

        datesRecyclerView = findViewById(R.id.date_picker);
        datesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        datesAdapter = new DatesRecyclerAdapter(getApplicationContext(),dates,this);
        datesRecyclerView.setAdapter(datesAdapter);
    }

    public Recipe getRecipe(int id){
        Recipe recipe = null;
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).getId() == id){
                recipe = recipes.get(i);
                break;
            }
        }
        return recipe;
    }

    public void initializeRecipeRecyclers(){
        breakfast_recipesView = findViewById(R.id.breakfast_recipes);
        breakfast_recipesView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        lunch_recipesView = findViewById(R.id.lunch_recipes);
        lunch_recipesView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        dinner_recipesView = findViewById(R.id.dinner_recipes);
        dinner_recipesView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
    }

    public static void setRecipeAdapters(MealPlanner mealPlanner){
        breakfast_recipesView.setAdapter(new RecipesRecyclerAdapter(context,mealPlanner.getBreakfast(),"delete,"+mealPlanner.getDate()+",breakfast"));
        lunch_recipesView.setAdapter(new RecipesRecyclerAdapter(context,mealPlanner.getLunch(),"delete,"+mealPlanner.getDate()+",lunch"));
        dinner_recipesView.setAdapter(new RecipesRecyclerAdapter(context,mealPlanner.getDinner(),"delete,"+mealPlanner.getDate()+",dinner"));
    }

    public static void setMealPlanner(String date){
        MealPlanner mealPlanner = getMealPlanner(date);
        setRecipeAdapters(mealPlanner);
    }

    public static MealPlanner getMealPlanner(String date){
        for (int i = 0; i < mealPlanners.size(); i++) {
            if (Utilities.dateCompare(mealPlanners.get(i).getDate(),date).compareTo("same") == 0){
                return mealPlanners.get(i);
            }
        }
        return null;
    }

    private void loadJson(){
        Utilities.readJsonFile(getApplicationContext(),directory,filename).forEach(object -> {
            try {
                String date = object.getString("Date");
                if(Utilities.dateCompare(date,today).compareTo("before")!=0){
                    MealPlanner mealPlanner = getMealPlanner(date);
                    Utilities.convertStringToIntList(object.getString("Breakfast")).forEach(id->{
                        mealPlanner.addToBreakfast(getRecipe(id));
                    });
                    Utilities.convertStringToIntList(object.getString("Lunch")).forEach(id->{
                        mealPlanner.addToLunch(getRecipe(id));
                    });
                    Utilities.convertStringToIntList(object.getString("Dinner")).forEach(id->{
                        mealPlanner.addToDinner(getRecipe(id));
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveJson(){
        StringBuilder jsonData = new StringBuilder();
        jsonData.append("[");
        mealPlanners.forEach(mealPlanner -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Date",mealPlanner.getDate());
                jsonObject.put("Breakfast",mealPlanner.getBreakfast().stream().map(recipe -> recipe.getId()).collect(Collectors.toList()));
                jsonObject.put("Lunch",mealPlanner.getLunch().stream().map(recipe -> recipe.getId()).collect(Collectors.toList()));
                jsonObject.put("Dinner",mealPlanner.getDinner().stream().map(recipe -> recipe.getId()).collect(Collectors.toList()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonData.append("\n"+jsonObject+",");
        });
        jsonData.append("\n]");
        Utilities.saveFile(getApplicationContext(),directory,filename,jsonData.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDateRecycler();
        recipes = new ArrayList<>();
        DB.getRecipes().forEach(recipe -> {
            recipes.add(recipe);
        });
        loadJson();
        setMealPlanner(today);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveJson();
        mealPlanners = new ArrayList<>();
    }
}