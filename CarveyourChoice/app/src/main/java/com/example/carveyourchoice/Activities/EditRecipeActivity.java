package com.example.carveyourchoice.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.carveyourchoice.Adapters.RecyclerAdapters.SelectedIngredientRecyclerAdapter;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditRecipeActivity extends AppCompatActivity {
    private static Context context;
    private DBHelper DB;
    private static Uri imageUri;
    private static List<Ingredient> selected_ingredients;
    private static RecyclerView ingredientRecyclerView;
    private static SelectedIngredientRecyclerAdapter ingredientAdapter;
    private static String selectedName, selectedDiet, selectedInstruction, selectedDuration, selectedVideo;
    private static Category selectedCategory;
    private static int id;
    private static Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        context = getApplicationContext();
        DB = new DBHelper(context);
        selected_ingredients = new ArrayList<>();

        if(getIntent().hasExtra("id")){
            id = getIntent().getIntExtra("id",-1);
            recipe = DB.getRecipe(id);
        }

        if (recipe != null){
            setSelectedName(recipe.getName());
            setSelectedDiet(recipe.getDiet());
            setSelectedCategory(recipe.getCategory());
            setSelected_ingredients(recipe.getIngredients());
            setSelectedDuration(recipe.getDuration());
            setSelectedInstruction(recipe.getInstruction());
            setSelectedVideo(recipe.getVideo());
        }else{
            Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        EditRecipeActivity.id = id;
    }

    public static Recipe getRecipe() {
        return recipe;
    }

    public static void setRecipe(Recipe recipe) {
        EditRecipeActivity.recipe = recipe;
    }

    public static Uri getImageUri() {
        return imageUri;
    }

    public static void setImageUri(Uri imageUri) {
        EditRecipeActivity.imageUri = imageUri;
    }

    public static List<Ingredient> getSelected_ingredients() {
        return selected_ingredients;
    }

    public static void setSelected_ingredients(List<Ingredient> selected_ingredients) {
        EditRecipeActivity.selected_ingredients = selected_ingredients;
    }

    public static void addToSelectedIngredients(Ingredient ingredient){
        List<Integer> ids = selected_ingredients.stream().map(x->x.getId()).collect(Collectors.toList());
        if (!ids.contains(ingredient.getId())){
            selected_ingredients.add(ingredient);
        }
    }

    public static void removeFromSelectedIngredients(int id){
        List<Integer> ids = selected_ingredients.stream().map(x->x.getId()).collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id){
                selected_ingredients.remove(selected_ingredients.get(i));
            }
        }
    }

    public static RecyclerView getIngredientRecyclerView() {
        return ingredientRecyclerView;
    }

    public static void setIngredientRecyclerView(RecyclerView ingredientRecyclerView) {
        EditRecipeActivity.ingredientRecyclerView = ingredientRecyclerView;
    }

    public static SelectedIngredientRecyclerAdapter getIngredientAdapter() {
        return ingredientAdapter;
    }

    public static void setIngredientAdapter(SelectedIngredientRecyclerAdapter ingredientAdapter) {
        EditRecipeActivity.ingredientAdapter = ingredientAdapter;
    }

    public static String getSelectedDiet() {
        return selectedDiet;
    }

    public static void setSelectedDiet(String selectedDiet) {
        EditRecipeActivity.selectedDiet = selectedDiet;
    }

    public static String getSelectedName() {
        return selectedName;
    }

    public static void setSelectedName(String selectedName) {
        EditRecipeActivity.selectedName = selectedName;
    }

    public static String getSelectedInstruction() {
        return selectedInstruction;
    }

    public static void setSelectedInstruction(String selectedInstruction) {
        EditRecipeActivity.selectedInstruction = selectedInstruction;
    }

    public static String getSelectedDuration() {
        return selectedDuration;
    }

    public static void setSelectedDuration(String selectedDuration) {
        EditRecipeActivity.selectedDuration = selectedDuration;
    }

    public static Category getSelectedCategory() {
        return selectedCategory;
    }

    public static void setSelectedCategory(Category selectedCategory) {
        EditRecipeActivity.selectedCategory = selectedCategory;
    }

    public static String getSelectedVideo() {
        return selectedVideo;
    }

    public static void setSelectedVideo(String selectedVideo) {
        EditRecipeActivity.selectedVideo = selectedVideo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            setImageUri(data.getData());
        }
    }
}