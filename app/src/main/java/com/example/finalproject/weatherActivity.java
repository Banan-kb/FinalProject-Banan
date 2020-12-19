package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class weatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        final EditText newCity= (EditText) findViewById(R.id.newCityEdit);
        Button newCityButton= (Button) findViewById(R.id.cityButton);

        final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);

        newCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityVal= newCity.getText().toString();

                SharedPreferences.Editor editor= sp.edit();

                editor.putString("city", cityVal);
                //editor.putString("cityfb", cityVal);
                editor.commit();

                startActivity(new Intent(weatherActivity.this, MainActivity.class));
            }
        });

    }
}