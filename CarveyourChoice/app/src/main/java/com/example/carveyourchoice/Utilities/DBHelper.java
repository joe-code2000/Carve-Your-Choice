package com.example.carveyourchoice.Utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DBHelper extends SQLiteOpenHelper {
    private static final String database_name = "FoodieDB";
    private final Context context;
    private final SQLiteDatabase readDB = this.getReadableDatabase();
    private final SQLiteDatabase writeDB = this.getWritableDatabase();

    public DBHelper(@Nullable Context context) {
        super(context, database_name, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table if not exists User (id integer primary key autoincrement,name varchar(20) not null);"
        );
        sqLiteDatabase.execSQL(
                "create table if not exists IngredientCategory (id integer primary key autoincrement,name varchar(20) not null)"
        );
        sqLiteDatabase.execSQL(
                "create table if not exists Ingredient (id integer primary key autoincrement,name varchar(20) not null,imagePath text,category_id integer," +
                        "foreign key (category_id) references IngredientCategory(id) on delete cascade)"
        );

        sqLiteDatabase.execSQL(
                "create table if not exists RecipeCategory (id integer primary key autoincrement,name varchar(20) not null)"
        );
        sqLiteDatabase.execSQL(
                "create table if not exists Recipe (id integer primary key autoincrement,name varchar(20) not null, diet text, category_id integer,imagePath text,instruction text,duration text not null,user_id integer,videoUrl text," +
                        "foreign key (category_id) references RecipeCategory(id) on delete cascade,foreign key (user_id) references User(id) on delete cascade) ;"
        );

        sqLiteDatabase.execSQL(
                "create table if not exists RecipeIngredients " +
                        "(id integer primary key autoincrement,recipe_id integer, ingredient_id integer," +
                        "foreign key(recipe_id) references Recipe(id) on delete cascade, foreign key(ingredient_id) references Ingredient(id) on delete cascade);"
        );
    }

    public Cursor execRead(String query){
        Cursor res = readDB.rawQuery(query,null);
        return res;
    }

    @SuppressLint("Range")
    public Category getIngredientCategory(int category_id){
        String table = "IngredientCategory";
        Cursor result = execRead("select * from "+table+" where id="+category_id);
        Category category = null;
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String category_name = result.getString(result.getColumnIndex("name"));
                category = new Category(id,category_name);
            }
        }
        return category;
    }

    @SuppressLint("Range")
    public List<Category> getIngredientCategories(){
        String table = "IngredientCategory";
        Cursor result = execRead("select * from "+table);
        List<Category> categories = new ArrayList<>();
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String category_name = result.getString(result.getColumnIndex("name"));
                categories.add(new Category(id,category_name));
            }
        }
        return categories;
    }

    @SuppressLint("Range")
    public Category getRecipeCategory(int category_id){
        String table = "RecipeCategory";
        Cursor result = execRead("select * from "+table+" where id="+category_id);
        Category category = null;
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String category_name = result.getString(result.getColumnIndex("name"));
                category = new Category(id,category_name);
            }
        }
        return category;
    }

    @SuppressLint("Range")
    public List<Category> getRecipeCategories(){
        String table = "RecipeCategory";
        Cursor result = execRead("select * from "+table);
        List<Category> categories = new ArrayList<>();
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String category_name = result.getString(result.getColumnIndex("name"));
                categories.add(new Category(id,category_name));
            }
        }
        return categories;
    }

    @SuppressLint("Range")
    public Ingredient getIngredient(int ingredient_id){
        String table = "Ingredient";
        Cursor result = execRead("select * from "+table+" where id="+ingredient_id);
        Ingredient ingredient = null;
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String name = result.getString(result.getColumnIndex("name"));
                String imagePath = result.getString(result.getColumnIndex("imagePath"));
                int category_id = result.getInt(result.getColumnIndex("category_id"));
                ingredient = new Ingredient(id,name);
                ingredient.setImage(imagePath);
                ingredient.setCategory(getIngredientCategory(category_id));
            }
        }
        return ingredient;
    }

    @SuppressLint("Range")
    public List<Ingredient> getIngredients(){
        String table = "Ingredient";
        Cursor result = execRead("select * from "+table);
        List<Ingredient> ingredients = new ArrayList<>();
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String name = result.getString(result.getColumnIndex("name"));
                String imagePath = result.getString(result.getColumnIndex("imagePath"));
                Ingredient ingredient = new Ingredient(id,name);
                ingredient.setImage(imagePath);
                int category_id = result.getInt(result.getColumnIndex("category_id"));
                ingredient.setCategory(getIngredientCategory(category_id));
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    @SuppressLint("Range")
    public Recipe getRecipe(int recipe_id){
        String table = "Recipe";
        Cursor result = execRead("select * from "+table+" where id="+recipe_id);
        Recipe recipe = null;
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String recipe_name = result.getString(result.getColumnIndex("name"));
                String diet = result.getString(result.getColumnIndex("diet"));
                String duration = result.getString(result.getColumnIndex("duration"));
                int category_id = result.getInt(result.getColumnIndex("category_id"));
                String imagePath = result.getString(result.getColumnIndex("imagePath"));
                String instruction = result.getString(result.getColumnIndex("instruction"));
                String videoUrl = result.getString(result.getColumnIndex("videoUrl"));
                int user_id = result.getInt(result.getColumnIndex("user_id"));
                recipe = new Recipe(id,user_id,recipe_name);
                recipe.setDiet(diet);
                recipe.setCategory(getRecipeCategory(category_id));
                recipe.setImage(imagePath);
                recipe.setInstruction(instruction);
                recipe.setIngredients(getRecipeIngredients(id));
                recipe.setDuration(duration);
                recipe.setVideo(videoUrl);
            }
        }
        return recipe;
    }

    @SuppressLint("Range")
    public List<Recipe> getRecipes(){
        String table = "Recipe";
        Cursor result = execRead("select * from "+table);
        List<Recipe> recipes = new ArrayList<>();
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String recipe_name = result.getString(result.getColumnIndex("name"));
                String diet = result.getString(result.getColumnIndex("diet"));
                int category_id = result.getInt(result.getColumnIndex("category_id"));
                String duration = result.getString(result.getColumnIndex("duration"));
                String imagePath = result.getString(result.getColumnIndex("imagePath"));
                String instruction = result.getString(result.getColumnIndex("instruction"));
                String videoUrl = result.getString(result.getColumnIndex("videoUrl"));
                int user_id = result.getInt(result.getColumnIndex("user_id"));
                Recipe recipe = new Recipe(id,user_id,recipe_name);
                recipe.setDiet(diet);
                recipe.setCategory(getRecipeCategory(category_id));
                recipe.setDuration(duration);
                recipe.setImage(imagePath);
                recipe.setInstruction(instruction);
                recipe.setIngredients(getRecipeIngredients(id));
                recipe.setVideo(videoUrl);
                recipes.add(recipe);
            }
        }

        return recipes;
    }

    @SuppressLint("Range")
    public List<Recipe> getRecipes(int user_id){
        String table = "Recipe";
        Cursor result = execRead("select * from "+table+" where user_id="+user_id);
        List<Recipe> recipes = new ArrayList<>();
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int id = result.getInt(result.getColumnIndex("id"));
                String recipe_name = result.getString(result.getColumnIndex("name"));
                String diet = result.getString(result.getColumnIndex("diet"));
                int category_id = result.getInt(result.getColumnIndex("category_id"));
                String duration = result.getString(result.getColumnIndex("duration"));
                String imagePath = result.getString(result.getColumnIndex("imagePath"));
                String instruction = result.getString(result.getColumnIndex("instruction"));
                String videoUrl = result.getString(result.getColumnIndex("videoUrl"));
                Recipe recipe = new Recipe(id,result.getInt(result.getColumnIndex("user_id")),recipe_name);
                recipe.setDiet(diet);
                recipe.setCategory(getRecipeCategory(category_id));
                recipe.setDuration(duration);
                recipe.setImage(imagePath);
                recipe.setInstruction(instruction);
                recipe.setIngredients(getRecipeIngredients(id));
                recipe.setVideo(videoUrl);
                recipes.add(recipe);
            }
        }

        return recipes;
    }

    @SuppressLint("Range")
    public List<Ingredient> getRecipeIngredients(int recipe_id){
        List<Ingredient> ingredients = new ArrayList<>();
        String table = "RecipeIngredients";
        Cursor result = execRead("select * from "+table+" where recipe_id="+recipe_id);
        if (result.isAfterLast() == false){
            while(result.moveToNext()){
                int ingredient_id = result.getInt(result.getColumnIndex("ingredient_id"));
                ingredients.add(getIngredient(ingredient_id));
            }
        }
        return ingredients;
    }

    public void insertIngredient(String name, int category_id){
        String table = "Ingredient";
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("imagePath", saveImage(R.raw.no_image));
        values.put("category_id",category_id);
        writeDB.insert(table,null,values);
    }

    public void insertIngredient(String name, String image, int category_id){
        String table = "Ingredient";
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("imagePath", image);
        values.put("category_id",category_id);
        writeDB.insert(table,null,values);
    }

    public void insertRecipe(int user_id,String name, String diet, int category_id, String duration, String instruction,String videoUrl){
        String table = "Recipe";
        ContentValues values = new ContentValues();
        values.put("user_id",user_id);
        values.put("name", name);
        values.put("diet",diet);
        values.put("imagePath", saveImage(R.raw.no_image));
        values.put("category_id",category_id);
        values.put("duration",duration);
        values.put("instruction",instruction);
        values.put("videoUrl",videoUrl);
        writeDB.insert(table,null,values);
    }

    public void insertRecipe(int user_id, String name, String diet,String image, int category_id, String duration, String instruction,String videoUrl){
        String table = "Recipe";
        ContentValues values = new ContentValues();
        values.put("user_id",user_id);
        values.put("name", name);
        values.put("diet",diet);
        values.put("imagePath", image);
        values.put("category_id",category_id);
        values.put("duration",duration);
        values.put("instruction",instruction);
        values.put("videoUrl",videoUrl);
        writeDB.insert(table,null,values);
    }

    public void insertRecipeIngredient(int recipe_id, int ingredient_id){
        String table = "RecipeIngredients";
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipe_id);
        values.put("ingredient_id",ingredient_id);
        writeDB.insert(table,null,values);
    }

    public void deleteRecipe(int id, int user_id){
        String table = "Recipe";
        writeDB.delete(table,"id=? and user_id=?",new String[]{Integer.toString(id),Integer.toString(user_id)});
    }

    public void updateRecipe(int id, String name, String diet, int category_id, String duration, String instruction,String videoUrl){
        String table = "Recipe";
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("diet",diet);
        values.put("imagePath", saveImage(R.raw.no_image));
        values.put("category_id",category_id);
        values.put("duration",duration);
        values.put("instruction",instruction);
        values.put("videoUrl",videoUrl);
        writeDB.update(table,values,"id=?",new String[]{Integer.toString(id)});
    }

    public void updateRecipe(int id, String name, String diet, String image, int category_id, String duration, String instruction, String videoUrl){
        String table = "Recipe";
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("diet",diet);
        values.put("imagePath", image);
        values.put("category_id",category_id);
        values.put("duration",duration);
        values.put("instruction",instruction);
        values.put("videoUrl",videoUrl);
        writeDB.update(table,values,"id=?",new String[]{Integer.toString(id)});
    }

    public void resetRecipeIngredients(int recipe_id){
        String table = "RecipeIngredients";
        writeDB.delete(table,"recipe_id=?",new String[]{Integer.toString(recipe_id)});
    }
    public void updateRecipeIngredients(int recipe_id, int ingredient_id){
        String table = "RecipeIngredients";
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipe_id);
        values.put("ingredient_id",ingredient_id);
        writeDB.insert(table,null,values);
    }

    @SuppressLint("Range")
    public void init(){

//        if (!initialised){
        String table;
        ContentValues values;
        onCreate(writeDB);

        //Initialize users
        table = "User";
        values = new ContentValues();
        values.put("name","admin");
        values.put("name","basic_user");
        writeDB.insert(table,null,values);
        
        //Initialize ingredientCategory
        String[] ingredientCategories = {
                "All","Organic","Inorganic"
        };
        table = "IngredientCategory";
        values = new ContentValues();
        for (String ingredientCategory: ingredientCategories) {
            values.put("name", ingredientCategory);
            writeDB.insert(table,null,values);
        }
        //Initialize Ingredient
        Utilities.readJsonFile(context,R.raw.ingredients).forEach(jsonObject -> {
            try {
                String name = jsonObject.getString("Name");
                int category_id =jsonObject.getInt("Category_Id");
                String image = jsonObject.getString("Image");
                int resource = context.getResources().getIdentifier(image,"raw",context.getPackageName());
                insertIngredient(name,saveImage(resource),category_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        //Initialize RecipeCategories
        String[] recipeCategories = {
                "All","Breakfast","Lunch","Dinner"
        };
        table = "RecipeCategory";
        values = new ContentValues();
        for (String recipeCategory: recipeCategories) {
            values.put("name", recipeCategory);
            writeDB.insert(table,null,values);
        }
        //Initialize Recipe
        Utilities.readJsonFile(context,R.raw.recipes).forEach(jsonObject -> {
            try {
                int user_id = jsonObject.getInt("User_Id");
                String name = jsonObject.getString("Name");
                String diet = jsonObject.getString("Diet");
                int category_id =jsonObject.getInt("Category_Id");
                String image = jsonObject.getString("Image");
                String ingredients = jsonObject.getString("Ingredients");
                String duration = jsonObject.getString("Duration");
                String instruction = jsonObject.getString("Instructions");
                String video_url = jsonObject.getString("Video_Url");
                int resource = context.getResources().getIdentifier(image,"raw",context.getPackageName());
                insertRecipe(user_id,name,diet,saveImage(resource),category_id,duration,instruction,video_url);
                List<Integer> ids = getRecipes().stream().map(recipe -> recipe.getId()).collect(Collectors.toList());
                if(ids.size() > 0){
                    Utilities.convertStringToIntList(ingredients).forEach(id->{
                        insertRecipeIngredient(ids.get(ids.size()-1),id);
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }

    @SuppressLint("Range")
    public void resetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> tables = new ArrayList<>();
        SQLiteDatabase readDB = this.getReadableDatabase();
        Cursor res = readDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN ('android_metadata', 'sqlite_sequence', 'room_master_table')",null);
        if (res.isAfterLast() == false){
            while (res.moveToNext()){
                tables.add(res.getString(res.getColumnIndex("name")));
            }
        }
        if (tables.size() != 6 || getRecipes().size() < 12 || getIngredients().size() < 39){
            tables.forEach(x->{
                db.execSQL("drop table "+x);
            });
            this.init();
        }
    }


    private String saveImage(int resource){
        ContextWrapper contextWrapper;
        File dir, file;
        BufferedInputStream bufferedInputStream;
        Bitmap bitmap;
        InputStream resourceStream;
        contextWrapper = new ContextWrapper(context);
        String filePath = context.getResources().getResourceName(resource).split("/")[1]+".jpg";
        resourceStream = context.getResources().openRawResource(resource);
        bufferedInputStream = new BufferedInputStream(resourceStream);
        bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        dir = contextWrapper.getDir("appImageDirectory",Context.MODE_PRIVATE);
        file = new File(dir,filePath);
        if (!file.exists()){
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return "appImageDirectory/"+filePath;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
