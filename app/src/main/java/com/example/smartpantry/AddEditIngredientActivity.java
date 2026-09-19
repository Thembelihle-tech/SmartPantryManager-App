package com.example.smartpantry;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText nameField, quantityField, expiryField;
    private Spinner unitSpinner;
    private long editingId = -1; //adding new

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        Toolbar toolbar = findViewById(R.id.toolbarAddEdit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(c -> finish());

        dbHelper = new DatabaseHelper(this);

        nameField = findViewById(R.id.editName);
        quantityField = findViewById(R.id.editQuantity);
        expiryField = findViewById(R.id.editExpiry);
        unitSpinner = findViewById(R.id.spinnerUnit);
        Button saveButton = findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.units_array, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        expiryField.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;
                isFormatting = true;

                String digits = s.toString().replaceAll("[^0-9]", "");
                if (digits.length() > 8) digits = digits.substring(0, 8);

                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digits.length(); i++) {
                    if (i == 2 || i == 4) formatted.append('-');
                    formatted.append(digits.charAt(i));
                }
                if (!formatted.toString().equals(s.toString())) {
                    s.replace(0, s.length(), formatted.toString());
                }
                isFormatting = false;
            }
        });

        if (getIntent().hasExtra("ingredient_id")) {
            editingId = getIntent().getLongExtra("ingredient_id", -1);
            nameField.setText(getIntent().getStringExtra("ingredient_name"));
            quantityField.setText(String.valueOf(getIntent().getDoubleExtra("ingredient_quantity", 0)));
            expiryField.setText(getIntent().getStringExtra("ingredient_expiry"));
            String unit = getIntent().getStringExtra("ingredient_unit");
            int pos = unitAdapter.getPosition(unit);
            if (pos >= 0) unitSpinner.setSelection(pos);
            saveButton.setText(R.string.update_item);
        }
        saveButton.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {
        String name = nameField.getText().toString().trim();
        String quantityText = quantityField.getText().toString().trim();
        String unit = unitSpinner.getSelectedItem().toString();
        String expiry = expiryField.getText().toString().trim();

        if (name.isEmpty()) {
            nameField.setError("Ingredient name is required!");
            return;
        }
        if (quantityText.isEmpty()) {
            quantityField.setError("Quantity is required!");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
            if (quantity <= 0) {
                quantityField.setError("Quantity must be greater than 0!");
                return;
            }
        } catch (NumberFormatException e) {
            quantityField.setError("Enter a valid number!");
            return;
        }
        if (!expiry.isEmpty() && !isValidDate(expiry)) {
            expiryField.setError("Enter a valid date (dd-MM-yyyy)");
            return;
        }

        Ingredients ingredient = new Ingredients(editingId, name, quantity, unit, expiry);

        if (editingId == -1) {
            dbHelper.addIngredient(ingredient);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updateIngredient(ingredient);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }


    finish();
}

        private boolean isValidDate(String date){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            sdf.setLenient(false);
            try{
                sdf.parse(date);
                return date.length() == 10;
            }catch(ParseException e){
                return false;
            }
        }
    }

