package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
public class PantryListActivity extends AppCompatActivity implements PantryAdapter.Listener{
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.recyclerPantry);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditingIngredientActivity.class)));
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_pantry);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry){
                return true;
            } else if(id == R.id.nav_recipes){
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                return true;
            }else if(id == R.id.nav_settings){
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshList();
    }

    private void refreshList(){
        List<Ingredients> items = dbHelper.getAllIngredients();
        adapter = new PantryAdapter(items,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEdit(Ingredients item){
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra("ingredient_id", item.getId());
        intent.putExtra("ingredient_name", item.getName());
        intent.putExtra("ingredient_quantity", item.getQuantity());
        intent.putExtra("ingredient_unit", item.getUnit());
        intent.putExtra("ingredient_expiry", item.getExpiryDate());
        startActivity(intent);
    }

    @Override
    public void onDelete(Ingredients item){
        dbHelper.deleteIngredient(item.getId());
        Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
        refreshList();
    }
}
