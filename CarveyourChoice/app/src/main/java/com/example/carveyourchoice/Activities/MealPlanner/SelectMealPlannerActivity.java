package com.example.carveyourchoice.Activities.MealPlanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Adapters.RecyclerAdapters.DatesRecyclerAdapter;
import com.example.carveyourchoice.Models.MealPlanner;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.example.carveyourchoice.Utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectMealPlannerActivity extends AppCompatActivity {
    private DBHelper DB;
    private static Context context;
    private static final String directory = "jsonDirectory";
    private static final String filename= "meal_planner.json";
    private static List<MealPlanner> mealPlanners = new ArrayList<>();
    private static List<Recipe> recipes = new ArrayList<>();
    private DatesRecyclerAdapter datesAdapter;
    private static RecyclerView datesRecyclerView;
    private String today = Utilities.getTodayDateStr();
    private static String currentDate;
    private static CheckBox breakfast_CheckBox,lunch_CheckBox,dinner_CheckBox;
    private static int recipe_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_meal_planner);
        context = getApplicationContext();
        DB = new DBHelper(context);
        currentDate = today;
        initialiseDateRecycler();
        DB.getRecipes().forEach(recipe -> {
            recipes.add(recipe);
        });
        loadJson();

        if(getIntent().hasExtra("add")){
            recipe_id = getIntent().getIntExtra("add",-1);
        }else {
            finish();
        }
        breakfast_CheckBox = findViewById(R.id.checkbox_breakfast);
        lunch_CheckBox = findViewById(R.id.checkbox_lunch);
        dinner_CheckBox = findViewById(R.id.checkbox_dinner);

        Button done = findViewById(R.id.done);
        done.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), MealPlannerActivity.class);
            JSONObject object = new JSONObject();
            try {
                object.put("Date",currentDate);
                if (breakfast_CheckBox.isChecked()){
                    object.put("Breakfast",true);
                }else{
                    object.put("Breakfast",false);
                }
                if (lunch_CheckBox.isChecked()){
                    object.put("Lunch",true);
                }else{
                    object.put("Lunch",false);
                }
                if (dinner_CheckBox.isChecked()){
                    object.put("Dinner",true);
                }else {
                    object.put("Dinner",false);
                }
                object.put("Id",recipe_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent.putExtra("add",object.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        });

        Button go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(v->{
            finish();
        });
        setCurrentDate(today);
    }

    public static void setCurrentDate(String date){
        currentDate = date;
        breakfast_CheckBox.setChecked(false);
        lunch_CheckBox.setChecked(false);
        dinner_CheckBox.setChecked(false);

        MealPlanner mealPlanner = getMealPlanner(date);

        for (int i = 0; i < mealPlanner.getBreakfast().size(); i++) {
            Recipe recipe = mealPlanner.getBreakfast().get(i);
            if (recipe.getId() == recipe_id){
                breakfast_CheckBox.setChecked(true);
                break;
            }
        }
        for (int i = 0; i < mealPlanner.getLunch().size(); i++) {
            Recipe recipe = mealPlanner.getLunch().get(i);
            if (recipe.getId() == recipe_id){
                lunch_CheckBox.setChecked(true);
                break;
            }
        }
        for (int i = 0; i < mealPlanner.getDinner().size(); i++) {
            Recipe recipe = mealPlanner.getDinner().get(i);
            if (recipe.getId() == recipe_id){
                dinner_CheckBox.setChecked(true);
                break;
            }
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        mealPlanners = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mealPlanners = new ArrayList<>();
    }
}