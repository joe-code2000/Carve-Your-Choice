package com.example.carveyourchoice.Activities.Grocery;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carveyourchoice.Models.Grocery;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.example.carveyourchoice.Utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroceryDetailActivity extends AppCompatActivity {
    private DBHelper DB;
    private static final String directory = "jsonDirectory";
    private static final String filename= "groceries.json";
    private static Context context;
    private static List<Ingredient> ingredients;
    private static int id = -1;
    private static Grocery grocery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_detail);
        //Initialize context
        context = getApplicationContext();
        //Initialize DB
        DB = new DBHelper(getApplicationContext());
        //Initialize ingredients
        ingredients = new ArrayList<>();

        //Get id intent
        if(getIntent().hasExtra("Id")){
            //Initialize id
            id = getIntent().getIntExtra("Id",-1);
            //Load grocery data
            loadJson();
        }
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        GroceryDetailActivity.context = context;
    }

    public static List<Ingredient> getIngredients() {
        return ingredients;
    }

    public static void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    public static void removeIngredient(int id){
        List<Integer> ids = ingredients.stream().map(x->x.getId()).collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id){
                ingredients.remove(ingredients.get(i));
            }
        }
    }

    public static int getId() {
        return id;
    }

    public static Grocery getGrocery() {
        return grocery;
    }

    public static void setGrocery(Grocery grocery) {
        GroceryDetailActivity.grocery = grocery;
    }

    private void loadJson(){
        List<JSONObject> jsonData = Utilities.readJsonFile(getApplicationContext(),directory,filename);
        jsonData.forEach(data->{
            try {
                if (id == data.getInt("Id")){
                    Grocery g = new Grocery(data.getInt("Id"));
                    g.setName(data.getString("Name"));
                    Utilities.convertStringToIntList(data.getString("Ingredients")).forEach(id->{
                        ingredients.add(DB.getIngredient(id));
                    });
                    grocery = g;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public static String convertDataToStr(){
        StringBuilder jsonData = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        if (grocery != null){
            try {
                jsonObject.put("Id",grocery.getId());
                jsonObject.put("Name",grocery.getName());
                jsonObject.put("Ingredients",getIngredients().stream().map(ingredient -> ingredient.getId()).collect(Collectors.toList()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonData.append(jsonObject+",");
        }
        return jsonData.toString();
    }
}