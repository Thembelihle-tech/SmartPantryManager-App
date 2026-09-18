package com.example.smartpantry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity{
    public static final String PREFS_NAME = "smart_pantry_prefs";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enable";
    public static final String KEY_PREFERRED_UNIT_SYSTEM = "preferred_unit_system";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Switch expiryAlertsSwitch = findViewById(R.id.switchExpiryAlerts);
        expiryAlertsSwitch.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true));
        expiryAlertsSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());
        RadioGroup unitGroup = findViewById(R.id.radioGroupUnits);
        String savedUnitSystem = prefs.getString(KEY_PREFERRED_UNIT_SYSTEM, "metric");
        if (savedUnitSystem.equals("metric")) {
            unitGroup.check(R.id.radioButtonMetric);
        } else {
            unitGroup.check(R.id.radioButtonCount);
        }
        unitGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String value = (checkedId == R.id.radioButtonMetric) ? "metric" : "count";
            prefs.edit().putString(KEY_PREFERRED_UNIT_SYSTEM, value).apply();
        });
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                startActivity(new Intent(this, PantryListActivity.class));
                return true;
            } else if (id == R.id.nav_recipes) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                return true;
            }
            return false;
        });
    }
}
