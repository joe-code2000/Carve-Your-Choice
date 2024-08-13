package com.example.carveyourchoice.Activities.Grocery;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.MealPlanner.MealPlannerActivity;
import com.example.carveyourchoice.Activities.MyRecipe.MyRecipeActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.GroceryRecyclerAdapter;
import com.example.carveyourchoice.MainActivity;
import com.example.carveyourchoice.Models.Grocery;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.example.carveyourchoice.Utilities.Utilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroceryActivity extends AppCompatActivity {
    private DBHelper DB;
    private static final String directory = "jsonDirectory";
    private static final String filename= "groceries.json";
    private List<Grocery> groceries;
    private RecyclerView groceryView ;
    private GroceryRecyclerAdapter groceryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);
        //Initializes the bottom navigation view
        initializeBottomNav();
        //Initialize DB
        DB = new DBHelper(getApplicationContext());
        //Initialize groceries
        groceries = new ArrayList<>();
        loadJson();

        if (getIntent().hasExtra("save")){
            String data = getIntent().getStringExtra("save");
            saveJson(getApplicationContext(),data);
        }

        if (getIntent().hasExtra("delete")){
            List<Grocery> tempGroceries = groceries;
            groceries = new ArrayList<>();
            tempGroceries.forEach(x -> {
                if(x.getId() != getIntent().getIntExtra("delete",-1)){
                    groceries.add(x);
                }
            });
            saveJson();
        }

        Button add_grocery = findViewById(R.id.add_grocery_btn);

        //Add groceries function
        add_grocery.setOnClickListener(v->{
            //Initialize bottom sheet dialog
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getApplicationContext());
            bottomSheetDialog.setContentView(R.layout.add_grocery_sheet);
            bottomSheetDialog.setCanceledOnTouchOutside(true);

            //Initialize dialog
            Dialog dialog = new Dialog(GroceryActivity.this);

            dialog.setContentView(R.layout.add_grocery_sheet);
            dialog.setCanceledOnTouchOutside(true);

            EditText editText = dialog.findViewById(R.id.editName);
            Button addBtn = dialog.findViewById(R.id.add_btn);
            Button cancelBtn = dialog.findViewById(R.id.cancel_btn);

            //Show dialog
            dialog.show();

            //Add grocery
            addBtn.setOnClickListener(x -> {
                if (!editText.getText().toString().isEmpty()){
                    Grocery grocery = new Grocery(Utilities.randomId(1,10000));
                    grocery.setName(editText.getText().toString());
                    groceries.add(grocery);
                    saveJson();
                    groceryAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(), "Please provide grocery name", Toast.LENGTH_SHORT).show();
                }

            });

            //Cancel adding grocery
            cancelBtn.setOnClickListener(x->{
                dialog.dismiss();
            });
        });

        //Initialize groceryView
        groceryView = findViewById(R.id.groceries);
        //Set groceryView layout
        groceryView.setLayoutManager(new GridLayoutManager(this,2));
        //Initialize groceryAdapter
        groceryAdapter = new GroceryRecyclerAdapter(getApplicationContext(),groceries);
        //groceryView set adapter to groceryAdapter
        groceryView.setAdapter(groceryAdapter);
    }

    private void initializeBottomNav(){
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setSelectedItemId(R.id.grocery_nav);
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
                    return true;
                case R.id.mealPlanner_nav:
                    startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }

    //Load data from jsonDirectory/groceries.json in form of List<JSONObject>
    private void loadJson(){
        List<JSONObject> jsonData = Utilities.readJsonFile(getApplicationContext(),directory,filename);
        jsonData.forEach(data->{
            try {
                Grocery grocery = new Grocery(data.getInt("Id"));
                grocery.setName(data.getString("Name"));
                List<Ingredient> ingredients = new ArrayList<>();
                Utilities.convertStringToIntList(data.getString("Ingredients")).forEach(id->{
                    ingredients.add(DB.getIngredient(id));
                });
                grocery.setIngredients(ingredients);
                groceries.add(grocery);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    //Save data to jsonDirectory/groceries.json in form String
    private void saveJson(){
        StringBuilder jsonData = new StringBuilder();
        jsonData.append("[");
        groceries.forEach(grocery -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Id",grocery.getId());
                jsonObject.put("Name",grocery.getName());
                jsonObject.put("Ingredients",grocery.getIngredients().stream().map(ingredient -> ingredient.getId()).collect(Collectors.toList()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonData.append("\n"+jsonObject+",");
        });
        jsonData.append("\n]");
        Utilities.saveFile(getApplicationContext(),directory,filename,jsonData.toString());
    }

    //Save data to jsonDirectory/groceries.json in form String
    private void saveJson(Context context,String data){
        StringBuilder jsonData = new StringBuilder();
        jsonData.append("[");
        groceries.forEach(grocery -> {
            JSONObject jsonObject = new JSONObject();
            try {
                JSONObject dataObject = new JSONObject(data);
                if (grocery.getId() == dataObject.getInt("Id")){
                    grocery.setName(dataObject.getString("Name"));
                    grocery.getIngredients().clear();
                    Utilities.convertStringToIntList(dataObject.getString("Ingredients")).forEach(id->{
                        grocery.addToIngredients(DB.getIngredient(id));
                    });
                }
                jsonObject.put("Id",grocery.getId());
                jsonObject.put("Name",grocery.getName());
                jsonObject.put("Ingredients",grocery.getIngredients().stream().map(ingredient -> ingredient.getId()).collect(Collectors.toList()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonData.append("\n"+jsonObject+",");
        });
        jsonData.append("\n]");
        Utilities.saveFile(context,directory,filename,jsonData.toString());
    }


}