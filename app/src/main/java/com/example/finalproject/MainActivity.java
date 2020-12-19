package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    TextView temperature, city, humid;
    ImageView wIcon;
    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp= PreferenceManager.getDefaultSharedPreferences(this);


        final String weatherWebserviceURL =
                "https://api.openweathermap.org/data/2.5/weather?q="+sp.getString("city","London")+"&appid=6602f949c47335204ba59921399752fe&units=metric";

        temperature= (TextView) findViewById(R.id.tempView);
        city= (TextView) findViewById(R.id.cityView);
        humid= (TextView) findViewById(R.id.humView);
        wIcon= (ImageView) findViewById(R.id.weatherIcon);

        weather(weatherWebserviceURL);


        Button weatherButton= (Button) findViewById(R.id.weatherButton);
        Button sqlButton= (Button) findViewById(R.id.sqlButton);
        Button firebaseButton= (Button) findViewById(R.id.firebaseButton);



        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, weatherActivity.class));
            }
        });

        sqlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, sqlActivity.class));

            }
        });

        firebaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, fbActivity.class));

            }
        });
    }




    public void weather(String url) {
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("banan", "rsponse received");

                        Log.d("banan", response.toString());
                        try{

                            String town= response.getString("name");
                            JSONObject jMain= response.getJSONObject("main");


                            double temp= jMain.getDouble("temp");
                            int humidity= jMain.getInt("humidity");



                            temperature.setText(String.valueOf(temp)+" °C");
                            city.setText(town);
                            humid.setText("Humidity: "+humidity+"%");


                            JSONArray jsonWeatherArray= response.getJSONArray("weather");

                            chooseBackground(jsonWeatherArray);

                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.e("Receive Error", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("banan", "error retrieving url");

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);
    }

    public void chooseBackground(JSONArray jsonWeatherArray){
        try {
            for(int i=0; i<jsonWeatherArray.length(); i++){
                JSONObject oneObject= jsonWeatherArray.getJSONObject(i);


                String weather= oneObject.getString("main");

                if(weather.equals("Clear")){
                    Glide.with(this)
                            .load(R.drawable.iconfinder_weather_clear)
                            .into(wIcon);
                }
                if(weather.equals("Clouds")){
                    Glide.with(this)
                            .load(R.drawable.iconfinder_cloudy_2995001)
                            .into(wIcon);
                }if(weather.equals("Rainy")){
                    Glide.with(this)
                            .load(R.drawable.iconfinder_rain)
                            .into(wIcon);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}