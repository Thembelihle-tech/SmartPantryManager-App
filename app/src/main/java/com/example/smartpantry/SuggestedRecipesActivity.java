package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity{
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerSuggested);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyView = findViewById(R.id.textEmptySuggestions);

        List<Ingredients> pantry = dbHelper.getAllIngredients();
        List<Recipes> allRecipes = dbHelper.getAllRecipes();
        List<Recipes> matches = new ArrayList<>();
        for(Recipes recipe : allRecipes){
            if (recipeIsFullyCovered(recipe, pantry)){
                matches.add(recipe);
            }
        }

        if (matches.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            RecipeAdapter adapter = new RecipeAdapter(matches, recipe -> {
                Intent intent = new Intent(this, RecipeDetailActivity.class);
                intent.putExtra("recipe_id", recipe.getId());
            });
            recyclerView.setAdapter(adapter);
        }
    }
    BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
    bottomNav.setSelectedItemId(R.id.nav_recipes);
    bottomNav.setOnItemselectedListener(item -> {
        int id = item.getItemId();
        if (id == R.id.nav_pantry){
            startActivity(new Intent(this, PantryListActivity.class));
            return true;
        }else if(id == R.id.nav_recipes){
            return true;
        }else if(id == R.id.nav_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return false;
    });
}
private boolean recipeIsFullyCovered(Recipes recipe, List<Ingredients> pantry){
    for (RecipeIngredient required : recipe.getIngredients()){
        boolean covered = false;
        for (Ingredients pantryItem : pantry){
            if (MatchingUtils.pantryCovers(pantryItem, required)){
                covered = true;
                break;
            }
        }
        if(!covered){
            return false;
        }
    }
    return true;
}
