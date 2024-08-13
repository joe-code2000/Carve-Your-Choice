package com.example.carveyourchoice.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Grocery {
    private final int id;
    private String name = "";
    private List<Ingredient> ingredients;

    public Grocery(int id) {
        this.id = id;
        this.ingredients = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addToIngredients(Ingredient ingredient){
        List<Integer> ids = ingredients.stream().map(x->x.getId()).collect(Collectors.toList());
        if (!ids.contains(ingredient.getId())){
            ingredients.add(ingredient);
        }
    }
}
