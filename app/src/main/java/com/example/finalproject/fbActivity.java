package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fbActivity extends AppCompatActivity {
    Firebase firebase= new Firebase();
    TextView temperature2, city2, humid2;
    ImageView wIcon2;
    DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();
    EditText editID, editFName, editLName, editPhone, editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);

        final String weatherWebserviceURL =
                "https://api.openweathermap.org/data/2.5/weather?q="+sp.getString("city","London")+"&appid=6602f949c47335204ba59921399752fe&units=metric";

        temperature2= (TextView) findViewById(R.id.temp2);
        city2= (TextView) findViewById(R.id.city2);
        humid2= (TextView) findViewById(R.id.humid2);
        wIcon2= (ImageView) findViewById(R.id.weatherIcon2);

        weather(weatherWebserviceURL);

        editID= (EditText) findViewById(R.id.idEdit);
        editFName= (EditText) findViewById(R.id.fnameEdit);
        editLName= (EditText) findViewById(R.id.lnameEdit);
        editPhone= (EditText) findViewById(R.id.phoneEdit);
        editEmail= (EditText) findViewById(R.id.emailEdit);

        Button insertButton= (Button) findViewById(R.id.addButton);
        Button delButton= (Button) findViewById(R.id.delButton);
        Button updateButton= (Button) findViewById(R.id.updateButton);
        Button findButton= (Button) findViewById(R.id.findButton);

        Button fbList= (Button) findViewById(R.id.fbListButton);

        ImageView homeImg= (ImageView) findViewById(R.id.homeImg);

        homeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fbActivity.this, MainActivity.class));
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString= editID.getText().toString();

                String fnameVal= editFName.getText().toString();
                String lnameVal= editLName.getText().toString();
                String phoneVal= editPhone.getText().toString();
                String emailVal= editEmail.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(fbActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                }
                else if(fnameVal.isEmpty()||lnameVal.isEmpty()|| phoneVal.isEmpty()|| emailVal.isEmpty()){

                    Toast.makeText(fbActivity.this, "Empty fields are required for inserting", Toast.LENGTH_SHORT).show();

                } else{
                    int idVal= Integer.parseInt(idString);

                    User user= new User(idVal, fnameVal, lnameVal, phoneVal,emailVal);

                    firebase.writeWithSuccess(idVal, user);
                    Toast.makeText(fbActivity.this, "Successful insert of new user.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString= editID.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(fbActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                }else {
                    int idVal = Integer.parseInt(idString);

                    firebase.removeUser(idVal);

                    Toast.makeText(fbActivity.this, "Succesful deletion of User#"+idVal, Toast.LENGTH_SHORT).show();
                }


            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= "Successfully updated field(s):";
                String idString= editID.getText().toString();
                String fnameVal= editFName.getText().toString();
                String lnameVal= editLName.getText().toString();
                String phoneVal= editPhone.getText().toString();
                String emailVal= editEmail.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(fbActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                }else{
                    int idVal= Integer.parseInt(idString);
                    if(!fnameVal.isEmpty()){
                        firebase.updateUser(idVal, "firstName", fnameVal);
                            s+="\nFirst Name";
                        }

                        if(!lnameVal.isEmpty()){
                        firebase.updateUser(idVal, "lastName", lnameVal);
                            s+="\nLast Name";
                    }
                        if(!phoneVal.isEmpty()){
                        firebase.updateUser(idVal, "phoneNumber", phoneVal);
                            s+="\nPhone Number";
                    }
                        if(!emailVal.isEmpty()){
                        firebase.updateUser(idVal, "emailAddress", emailVal);
                            s+="\nEmail Address";
                    }

                    Toast.makeText(fbActivity.this, s, Toast.LENGTH_SHORT).show();
                }

            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString= editID.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(fbActivity.this, "Hint: You can find user info by the user ID", Toast.LENGTH_SHORT).show();
                }else {
                    int idVal= Integer.parseInt(idString);
                    specificUserInfo(idVal);


                }


            }
        });


        fbList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fbActivity.this, fbListActivity.class));
            }
        });

    }

    public void specificUserInfo(final int userId) {
        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                DataSnapshot ds = dataSnapshot.child(String.valueOf(userId));

                String name = ds.child("firstName").getValue(String.class);
                String lname = ds.child("lastName").getValue(String.class);
                String phone = ds.child("phoneNumber").getValue(String.class);
                String email = ds.child("emailAddress").getValue(String.class);

                if(name == null){
                    Toast.makeText(fbActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(fbActivity.this, "User#"+userId+" found", Toast.LENGTH_SHORT).show();
                    editID.setText(String.valueOf(userId));
                    editFName.setText(name);
                    editLName.setText(lname);
                    editEmail.setText(email);
                    editPhone.setText(phone);
            }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("BANAN", "Failed to read value.", error.toException());
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



                            temperature2.setText(String.valueOf(temp)+" °C");
                            city2.setText(town);
                            humid2.setText("Humidity: "+humidity+"%");


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
                            .into(wIcon2);
                }
                if(weather.equals("Clouds")){
                    Glide.with(this)
                            .load(R.drawable.iconfinder_cloudy_2995001)
                            .into(wIcon2);
                }if(weather.equals("Rainy")){
                    Glide.with(this)
                            .load(R.drawable.iconfinder_rain)
                            .into(wIcon2);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}