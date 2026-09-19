package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiry_date TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "steps TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES " + TABLE_RECIPES + "(id))");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }


    public long addIngredient(Ingredients item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", item.getName());
        cv.put("quantity", item.getQuantity());
        cv.put("unit", item.getUnit());
        cv.put("expiry_date", item.getExpiryDate());
        long id = db.insert(TABLE_PANTRY, null, cv);
        db.close();
        return id;
    }

    public int updateIngredient(Ingredients item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", item.getName());
        cv.put("quantity", item.getQuantity());
        cv.put("unit", item.getUnit());
        cv.put("expiry_date", item.getExpiryDate());
        int rows = db.update(TABLE_PANTRY, cv, "id=?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rows;
    }

    public void deleteIngredient(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PANTRY, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Ingredients> getAllIngredients() {
        List<Ingredients> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null, "name ASC");
        while (c.moveToNext()) {
            list.add(new Ingredients(
                    c.getLong(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("quantity")),
                    c.getString(c.getColumnIndexOrThrow("unit")),
                    c.getString(c.getColumnIndexOrThrow("expiry_date"))
            ));
        }
        c.close();
        db.close();
        return list;
    }


    public List<Recipes> getAllRecipes() {
        List<Recipes> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null, "name ASC");
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow("id"));
            String name = c.getString(c.getColumnIndexOrThrow("name"));
            String steps = c.getString(c.getColumnIndexOrThrow("steps"));
            recipes.add(new Recipes(id, name, steps, getIngredientsForRecipe(db, id)));
        }
        c.close();
        db.close();
        return recipes;
    }

    private List<RecipeIngredients> getIngredientsForRecipe(SQLiteDatabase db, long recipeId) {
        List<RecipeIngredients> list = new ArrayList<>();
        Cursor c = db.query(TABLE_RECIPE_INGREDIENTS, null, "recipe_id=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        while (c.moveToNext()) {
            list.add(new RecipeIngredients(
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("quantity")),
                    c.getString(c.getColumnIndexOrThrow("unit"))
            ));
        }
        c.close();
        return list;
    }



    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Simple Ice Cream",
                "1. Whip the cream until form. 2. Add condensed milk and vanilla extract. 3. Transfer to airtight container. 4. Freeze for 8-10 hours or overnight 5. Scoop and enjoy!",
                new Object[][]{{"Whipping cream", 476, "g"}, {"condensed milk", 310, "g"}, {"vanilla extract", 4, "g"}});

        addRecipe(db, "Grilled Cheese Sandwich",
                "1. Butter bread. 2. Add cheese between slices. 3. Grill both sides until golden.",
                new Object[][]{{"bread", 2, "pcs"}, {"cheese", 50, "g"}, {"butter", 10, "g"}});

        addRecipe(db, "Tomato Pasta",
                "1. Boil pasta. 2. Fry garlic and tomato into a sauce. 3. Mix with pasta, serve.",
                new Object[][]{{"pasta", 200, "g"}, {"tomato", 3, "pcs"}, {"garlic", 2, "pcs"}, {"olive oil", 15, "ml"}});

        addRecipe(db, "Omelette",
                "1. Beat eggs. 2. Add chopped vegetables. 3. Cook in pan until set.",
                new Object[][]{{"egg", 3, "pcs"}, {"onion", 1, "pcs"}, {"tomato", 1, "pcs"}, {"salt", 1, "g"}});

        addRecipe(db, "Potato Hashbrown",
                "1. Dice potatoes. 2. Fry with onion until crisp. 3. Season and serve.",
                new Object[][]{{"potato", 3, "pcs"}, {"onion", 1, "pcs"}, {"oil", 15, "ml"}});

        addRecipe(db, "Tomato Egg Stir Fry",
                "1. Beat eggs. 2. Fry tomato until soft. 3. Add eggs, stir until set. 4. Season and serve.",
                new Object[][]{{"tomato", 2, "pcs"}, {"egg", 3, "pcs"}, {"salt", 1, "g"}});

        addRecipe(db, "French Toast",
                "1. Beat eggs. 2. Coat bread in eggs. 3. Serve warm.",
                new Object[][]{{"bread", 2, "pcs"}, {"eggs", 2, "pcs"}, {"oil", 10, "ml"}});

        addRecipe(db, "Vanilla Pancakes",
                "1. Mix with flour and egg into batter. 3. Fry on both sides.",
                new Object[][]{{"vanilla extract", 4, "g"}, {"flour", 150, "g"}, {"egg", 1, "pcs"}, {"milk", 100, "ml"}});

        addRecipe(db, "Garlic Butter Rice",
                "1. Melt butter. 2. Fry garlic. 3. Mix in cooked rice, season, serve.",
                new Object[][]{{"rice", 200, "g"}, {"garlic", 3, "pcs"}, {"butter", 20, "g"}});

        addRecipe(db, "Chicken Sandwich",
                "1. Slice cooked chicken. 2. Layer between bread with lettuce. 3. Serve.",
                new Object[][]{{"bread", 2, "pcs"}, {"chicken", 100, "g"}, {"lettuce", 1, "pcs"}});

        addRecipe(db, "Carrot & Potato Soup",
                "1. Boil carrot and potato until soft. 2. Blend. 3. Season and serve hot.",
                new Object[][]{{"carrot", 2, "pcs"}, {"potato", 2, "pcs"}, {"onion", 1, "pcs"}});

        addRecipe(db, "Milk Cereal Bowl",
                "1. Pour cereal into a bowl. 2. Add cold milk. 3. Serve immediately.",
                new Object[][]{{"cereal", 60, "g"}, {"milk", 200, "ml"}});

        addRecipe(db, "Onion & Egg Fried Rice",
                "1. Fry onion until soft. 2. Add rice and scrambled egg. 3. Stir-fry and serve.",
                new Object[][]{{"rice", 250, "g"}, {"onion", 1, "pcs"}, {"egg", 2, "pcs"}});

        addRecipe(db, "Tomato Soup",
                "1. Simmer tomato and onion until soft. 2. Blend smooth. 3. Season and serve.",
                new Object[][]{{"tomato", 4, "pcs"}, {"onion", 1, "pcs"}, {"salt", 1, "g"}});

        addRecipe(db, "Buttered Pasta",
                "1. Boil pasta. 2. Toss with melted butter and cheese. 3. Serve warm.",
                new Object[][]{{"pasta", 200, "g"}, {"butter", 15, "g"}, {"cheese", 30, "g"}});

        addRecipe(db, "Carrot Muffins",
                "1. Grate carrot. 2. Mix all ingredients at 180°C for 20 minutes. 3. Serve chilled.",
                new Object[][]{{"carrot", 2, "pcs"}, {"Flour", 125, "g"}, {"salt", 1, "g"}, {"eggs", 2, "pcs"}, {"sugar", 200, "g"}});

        addRecipe(db, "Banana & Milk Smoothie",
                "1. Blend banana and milk together. 2. Pour into a glass. 3. Serve cold.",
                new Object[][]{{"banana", 2, "pcs"}, {"milk", 250, "ml"}});

        addRecipe(db, "Butter Roti",
                "1. Swift flour. 2. Rub in butter until breadcrumbs. 3. Add boiling water. 4. Roll and toast. 5. Enjoy!",
                new Object[][]{{"flour", 2, "c"}, {"salt", 1, "tsp"}, {"butter", 8, "tsp"}, {"water", 1, "c"}});
    }

    private void addRecipe(SQLiteDatabase db, String name, String steps, Object[][] ingredients) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("steps", steps);
        long recipeId = db.insert(TABLE_RECIPES, null, cv);

        for (Object[] ing : ingredients) {
            ContentValues icv = new ContentValues();
            icv.put("recipe_id", recipeId);
            icv.put("name", (String) ing[0]);
            icv.put("quantity", ((Number) ing[1]).doubleValue());
            icv.put("unit", (String) ing[2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, icv);
        }
    }
}
