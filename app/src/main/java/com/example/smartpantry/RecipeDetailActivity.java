package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RecipeDetailActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.toolbarRecipeDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar() .setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v ->finish());

        long recipeId = getIntent().getLongExtra("recipe_id", -1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Recipes found = null;
        for (Recipes r : dbHelper.getAllRecipes()){
            if(r.getId() == recipeId){
                found = r;
                break;
            }
        }

        TextView nameView = findViewById(R.id.textRecipeDetailName);
        TextView ingredientsView = findViewById(R.id.textRecipeDetailIngredients);
        TextView stepsView = findViewById(R.id.textRecipeDetailSteps);

        if (found != null){
            nameView.setText(found.getName());

            StringBuilder sb = new StringBuilder();
            for (RecipeIngredients ri : found.getIngredients()){
                sb.append("• ").append(ri.getQuantity()).append(" ")
                        .append(ri.getUnit()).append(" ").append(ri.getName()).append("\n");
            }
            ingredientsView.setText(sb.toString());
            stepsView.setText(found.getSteps());
        }
    }
}
