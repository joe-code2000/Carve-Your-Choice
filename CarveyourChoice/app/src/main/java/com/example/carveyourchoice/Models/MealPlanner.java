package com.example.carveyourchoice.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MealPlanner {
    private List<Recipe> breakfast, lunch, dinner;
    private final String date;

    public MealPlanner(String date) {
        this.date = date;
        this.breakfast = new ArrayList<>();
        this.lunch = new ArrayList<>();
        this.dinner = new ArrayList<>();
    }
    //Breakfast functions
    public void addToBreakfast(Recipe recipe){
        List<Integer> ids = breakfast.stream().map(r->r.getId()).collect(Collectors.toList());
        if (recipe != null){
            if (!ids.contains(recipe.getId())){
                this.breakfast.add(recipe);
            }
        }

    }

    public void removeFromBreakfast(int id){
        Recipe recipe = null;
        for (int i = 0; i < breakfast.size(); i++) {
            if(breakfast.get(i).getId() == id){
                recipe = breakfast.get(i);
                break;
            }
        }
        if (recipe != null){
            this.breakfast.remove(recipe);
        }
    }

    public List<Recipe> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Recipe> breakfast) {
        this.breakfast = breakfast;
    }

    //Lunch functions
    public void addToLunch(Recipe recipe){
        List<Integer> ids = lunch.stream().map(r->r.getId()).collect(Collectors.toList());
        if (recipe != null) {
            if (!ids.contains(recipe.getId())) {
                this.lunch.add(recipe);
            }
        }
    }

    public void removeFromLunch(int id){
        Recipe recipe = null;
        for (int i = 0; i < lunch.size(); i++) {
            if(lunch.get(i).getId() == id){
                recipe = lunch.get(i);
                break;
            }
        }
        if (recipe != null){
            this.lunch.remove(recipe);
        }
    }

    public List<Recipe> getLunch() {
        return lunch;
    }

    public void setLunch(List<Recipe> lunch) {
        this.lunch = lunch;
    }

    //Dinner functions
    public void addToDinner(Recipe recipe){
        List<Integer> ids = dinner.stream().map(r->r.getId()).collect(Collectors.toList());
        if (recipe != null) {
            if (!ids.contains(recipe.getId())) {
                this.dinner.add(recipe);
            }
        }
    }

    public void removeFromDinner(int id){
        Recipe recipe = null;
        for (int i = 0; i < dinner.size(); i++) {
            if(dinner.get(i).getId() == id){
                recipe = dinner.get(i);
                break;
            }
        }
        if (recipe != null){
            this.dinner.remove(recipe);
        }
    }

    public List<Recipe> getDinner() {
        return dinner;
    }

    public void setDinner(List<Recipe> dinner) {
        this.dinner = dinner;
    }

    public String getDate() {
        return date;
    }
}
