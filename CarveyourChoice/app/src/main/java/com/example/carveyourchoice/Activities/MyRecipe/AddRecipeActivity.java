package com.example.carveyourchoice.Activities.MyRecipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Adapters.RecyclerAdapters.SelectedIngredientRecyclerAdapter;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddRecipeActivity extends AppCompatActivity {
    private static Uri imageUri;
    private static List<Ingredient> selected_ingredients;
    private static RecyclerView ingredientRecyclerView;
    private static SelectedIngredientRecyclerAdapter ingredientAdapter;
    private static String selectedName, selectedDiet, selectedInstruction, selectedDuration, selectedVideo;
    private static Category selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        selected_ingredients = new ArrayList<>();

    }

    public static List<Ingredient> getSelected_ingredients() {
        return selected_ingredients;
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
        AddRecipeActivity.ingredientRecyclerView = ingredientRecyclerView;
    }

    public static SelectedIngredientRecyclerAdapter getIngredientAdapter() {
        return ingredientAdapter;
    }

    public static void setIngredientAdapter(SelectedIngredientRecyclerAdapter ingredientAdapter) {
        AddRecipeActivity.ingredientAdapter = ingredientAdapter;
    }

    public static Category getSelectedCategory() {
        return selectedCategory;
    }

    public static void setSelectedCategory(Category selectedCategory) {
        AddRecipeActivity.selectedCategory = selectedCategory;
    }

    public static String getSelectedName() {
        return selectedName;
    }

    public static void setSelectedName(String selectedName) {
        AddRecipeActivity.selectedName = selectedName;
    }

    public static String getSelectedDiet() {
        return selectedDiet;
    }

    public static void setSelectedDiet(String selectedDiet) {
        AddRecipeActivity.selectedDiet = selectedDiet;
    }

    public static String getSelectedInstruction() {
        return selectedInstruction;
    }

    public static void setSelectedInstruction(String selectedInstruction) {
        AddRecipeActivity.selectedInstruction = selectedInstruction;
    }

    public static String getSelectedDuration() {
        return selectedDuration;
    }

    public static void setSelectedDuration(String selectedDuration) {
        AddRecipeActivity.selectedDuration = selectedDuration;
    }

    public static Uri getImageUri() {
        return imageUri;
    }

    public static void setImageUri(Uri imageUri) {
        AddRecipeActivity.imageUri = imageUri;
    }


    public static String getSelectedVideo() {
        return selectedVideo;
    }

    public static void setSelectedVideo(String selectedVideo) {
        AddRecipeActivity.selectedVideo = selectedVideo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            setImageUri(data.getData());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AddRecipeActivity.setSelectedName(null);
        AddRecipeActivity.setSelectedDuration(null);
        AddRecipeActivity.setSelectedInstruction(null);
        AddRecipeActivity.setSelectedCategory(null);
        AddRecipeActivity.getSelected_ingredients().clear();
        AddRecipeActivity.setImageUri(null);
    }
}