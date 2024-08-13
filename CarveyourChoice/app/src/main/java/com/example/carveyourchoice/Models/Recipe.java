package com.example.carveyourchoice.Models;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private final int id, user_id;
    private final String name;
    private String diet,duration,image,instruction,video;
    private List<String> tips = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private Category category;

    public Recipe(int id, int user_id, String name) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.duration = null;
        this.image = null;
        this.instruction = null;
        this.category = null;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image.toString();
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }
}
