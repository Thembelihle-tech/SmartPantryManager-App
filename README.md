# SmartPantry Manager
A Java Android app that suggests recipes based strictly on the ingredients you already have so nothing you have goes to watse.

## About the app
Food waste often happens because people do not know what to cook with the odds in the kitchen. SmartPantry Manager lets you record the ingredients you actually have at home and then shows you only the recipes you can cook right now with no shopping required 
  but only the ingredients you currently have.
The idea is the strict matching rule whereby the recipe is only suggested if every ingredient it need is in you pantry and the required quantity. If a recipes need 5 ingredients and you only have 4 then the recipe is not shown.

## Features
* Pantry management (full CRUD): add, view, edit and delete ingredients.
* Pantry list screen: a RecyclerView with a custom adapter bound to the SQLite database.
* Recipe collection: 18 recipes preloaded into the database on the first run and each with ingredients, quantities and preparation steps.
* Suggeste recipes screen: runs the strict matching algorithm against the current pantry and lists only the recipes you can make.
* Recipe detail screen: recipe photo, full ingredient list and method.
* Empty state feedback: a message ("No recipes match your pantry yet. Add more ingredients") instead of just a blank screen.
* Settings screen: expiring soon alerts toggle and units preference saved with SharedPreferences.
* Input validation: required fields, numeric or positive quantity checks and an auto formatting validated expiry data.
* Bottom navigation bar: quick switching between Pantry, Recipes and Settings.
* Persistent data: everything is stored on the device and is still there after the app is closed and reopened.

## Strict matching algorithm
The logic lives in MatchingUtils.java. A recipe is suggested only if every required ingredient passes the below:
* Name normalisation: Lowercase names, strips punctuation and extra space and handles simple plurals so "tomato" matches "tomatoes" ("berries" matches "berry", etc.).
* Oil grouping: Olive, sunflower, vegetable, canola and cooking oil all count as one "oil".
* Unit conversion: Converts to a common base unit before comparing (kg to g, l to ml, tbsp to tsp, etc.).
* Quantity totalling: If the same ingredient is added in several rows then the quanitities are summed before checking against the recipe.
* All or nothing rule: recipeIsCovered() returns false as soon as one ingredient is missing or insufficient so partial matches are excluded.

The suggestions list refreshes in onResume(), so adding or removing an ingredient immediately adds or removes recipes from the list.

## Database choice: SQLite
Chosen via SQLiteOpenHelper

SQLite because:
* It is local and offline. A pantry app should work in the kitchen without needing internet access.
* There is no backend needed. No server or API leys to build, host or maintain which keeps the setup simple for anyone running the project.
* It is fast and lightweight for small structured dataset such as pantry items and recipes.
* It is built into Android.
* It is relational by nature, since recipes and their ingredients map cleanly to related tables.

## Data model
* pantry_items -> id(PK, auto), name, quantity, unit, expiry_date
* recipes -> id(PK, auto), name, steps
* recipe_ingredients -> id(PK, auto), recipe_id(FK -> recipes.id), name, quantity, unit

DatabaseHelper.java creates the tables, seeds the recipes on first run and provides the CRUD methods (addIngredient, getAllIngredients, updateIngredient, deleteIngredient, getAllRecipes). When the database version is increased then only the recipe tables are rebuilt and reseeded so the pantry data is preserved.

## Project structure
SmartPantryManager/
└── app/src/main/
    ├── java/com/example/smartpantry/
    │   ├── PantryListActivity.java        # Launcher screen: pantry list (RecyclerView)
    │   ├── AddEditIngredientActivity.java # Add / edit form with input validation
    │   ├── SuggestedRecipesActivity.java  # Runs strict matching, shows suggestions
    │   ├── RecipeDetailActivity.java      # Full recipe: image, ingredients, method
    │   ├── SettingsActivity.java          # Preferences (SharedPreferences)
    │   ├── PantryAdapter.java             # RecyclerView adapter for pantry items
    │   ├── RecipeAdapter.java             # RecyclerView adapter for recipes
    │   ├── DatabaseHelper.java            # SQLite schema, seed data, CRUD
    │   ├── MatchingUtils.java             # Strict-matching business logic
    │   └── Ingredients / Recipes / RecipeIngredients.java  # Model classes
    └── res/
        ├── layout/    # Activity and list-item layouts
        ├── menu/      # Bottom navigation menus
        ├── drawable/  # Icons and recipe images
        └── values/    # Colours, strings, themes, unit arrays


## Screens
1. Pantry list
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Detail
5. Settings

Navigation between screens uses Intents together with a bottom naviigation bar.

## Tech stack
* Language -> Java
* IDE -> Android Studio
* Min SDK/Target SDK -> 24 (Android 7.0)/34(Android 14)
* Database -> SQLite(SQLiteOpenHelper)
* UI -> RecyclerView, CardView, ConstraintLayout, Material Components, BottomNavigationView
* Libraries -> AppCompat, Material, ConstraintLayout, RecyclerView, CardView

## Setup and run instructions
Prerequisites:
* Android Studio
* JDK (bundled with Android Studio)
* An Android emulator (API 24+) or a physical Android device with USB debugging or wireless debugging enabled.
* Internet connection for the first Gradle sync (to download dependencies)

Steps:
1. Clone the repository
https://github.com/Thembelihle-tech/SmartPantryManager-App.git

3. Open the project in Android Studio
4. Sync Gradle: wait for the automatic sync to finish
5. Select a device: create an emulator in Device Manager or plug in your phone.
6. Run the app by clicking the green Run button.

## Try the strict matching rule
1. Open Recipes
2. Go to Pantry and add bread (2 pcs), cheese (50 g) and butter (10 g).
3. Return to Recipes you will see that Grilled cheese Sandwich now appears.
4. Delete butter from the pantry and return to Recipes. The recipe disappears because one ingredient is now missing.


# Author
## Thembelihle Tili Nkosi
402313573 BSc (Articulation)
Richfield Graduate Institute of Technology
Mobile App Development 700
