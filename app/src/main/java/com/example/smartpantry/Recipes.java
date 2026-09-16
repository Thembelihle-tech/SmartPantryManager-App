package com.example.smartpantry;

import java.util.List;

public class Recipes {
    private long id;
    private String name;
    private String steps;
    private List<RecipeIngredients> ingredients;

    public Recipes(long id, String name, String steps, List<RecipeIngredients> ingredients){
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    public long getId(){return id;}
    public String getName(){return name;}
    public String getSteps(){return steps;}
    public List<RecipeIngredients> getIngredients(){return ingredients;}
}
