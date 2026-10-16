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
pantry_items -> id(PK, auto), name, quantity, unit, expiry_date
recipes -> id(PK, auto), name, steps
recipe_ingredients -> id(PK, auto), recipe_id(FK -> recipes.id), name, quantity, unit
