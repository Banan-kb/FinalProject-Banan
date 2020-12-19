package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class sqlActivity extends AppCompatActivity {
    TextView temperatureSql, citySql, humidSql;
    ImageView wIconSql;
    EditText editSqlID, editSqlFName, editSqlLName, editSqlPhone, editSqlEmail;
    DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();
    DatabaseHelper myDB= new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);

        final String weatherWebserviceURL =
                "https://api.openweathermap.org/data/2.5/weather?q="+sp.getString("city","London")+"&appid=6602f949c47335204ba59921399752fe&units=metric";

        temperatureSql= (TextView) findViewById(R.id.temp3);
        citySql= (TextView) findViewById(R.id.city3);
        humidSql= (TextView) findViewById(R.id.humid3);
        wIconSql= (ImageView) findViewById(R.id.weatherIcon3);

        weather(weatherWebserviceURL);

        editSqlID= (EditText) findViewById(R.id.idSqlEdit);
        editSqlFName= (EditText) findViewById(R.id.fnameSqlEdit);
        editSqlLName= (EditText) findViewById(R.id.lnameSqlEdit);
        editSqlPhone= (EditText) findViewById(R.id.phoneSqlEdit);
        editSqlEmail= (EditText) findViewById(R.id.emailSqlEdit);

        Button insertSqlButton= (Button) findViewById(R.id.insertSqlButton);
        Button delSqlButton= (Button) findViewById(R.id.delSqlButton);
        Button updateSqlButton= (Button) findViewById(R.id.updateSqlButton);
        Button readSqlButton= (Button) findViewById(R.id.readSqlButton);

        Button findSqlButton= (Button) findViewById(R.id.findSqlButton);

        Button sqlList= (Button) findViewById(R.id.sqlListButton);

        ImageView homeImg2= (ImageView) findViewById(R.id.homeImg2);

        homeImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sqlActivity.this, MainActivity.class));
            }
        });

        sqlList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sqlActivity.this, sqlListActivity.class));
            }
        });

        findSqlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString= editSqlID.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(sqlActivity.this, "Hint: You can find user info by the user ID", Toast.LENGTH_SHORT).show();
                }else {
                    int idVal= Integer.parseInt(idString);
                    specificUserInfo(idVal);

                }

            }
        });

        readSqlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString = editSqlID.getText().toString();

                if (idString.isEmpty()) {
                    Toast.makeText(sqlActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                } else {
                    int idVal = Integer.parseInt(idString);

                    Cursor cursor = myDB.getSpecificResult(idVal);
                    if (cursor.getCount() == 0) {
                        Toast.makeText(sqlActivity.this, "User does not exist in SQL", Toast.LENGTH_LONG).show();

                    } else {

                        String cID = cursor.getString(0);
                        String cFName = cursor.getString(1);
                        String cLName = cursor.getString(2);
                        String cPhone = cursor.getString(3);
                        String cEmail = cursor.getString(4);

                        editSqlID.setText(String.valueOf(cID));
                        editSqlFName.setText(cFName);
                        editSqlLName.setText(cLName);
                        editSqlPhone.setText(cPhone);
                        editSqlEmail.setText(cEmail);

                    }


                }

            }

        });

        updateSqlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                String s= "Successfully updated field(s):";
                String idString= editSqlID.getText().toString();
                String fnameVal= editSqlFName.getText().toString();
                String lnameVal= editSqlLName.getText().toString();
                String phoneVal= editSqlPhone.getText().toString();
                String emailVal= editSqlEmail.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(sqlActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                }else{
                    int idVal= Integer.parseInt(idString);
                    if(!fnameVal.isEmpty()){
                         i= myDB.updateUser(idVal, "FIRST_NAME", fnameVal);
                        if(i==0){
                            Toast.makeText(sqlActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                        }else{
                            s+="\nFirst Name";
                        }
                    }
                    if(!lnameVal.isEmpty()){
                         i= myDB.updateUser(idVal, "LAST_NAME", lnameVal);
                        if(i==0){
                            Toast.makeText(sqlActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                        }else
                            s+="\nLast Name";
                    }
                    if(!phoneVal.isEmpty()){
                         i= myDB.updateUser(idVal, "PHONE_NUMBER", phoneVal);
                        if(i==0){
                            Toast.makeText(sqlActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                        }else
                            s+="\nPhone Number";
                    }
                    if(!emailVal.isEmpty()){
                         i= myDB.updateUser(idVal, "EMAIL_ADDRESS", emailVal);
                        if(i==0){
                            Toast.makeText(sqlActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                        }else
                            s+="\nEmail Address";
                    }
                    if(i==0){
                        Toast.makeText(sqlActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(sqlActivity.this, s, Toast.LENGTH_SHORT).show();
                }

            }
        });


        insertSqlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString= editSqlID.getText().toString();

                String fnameVal= editSqlFName.getText().toString();
                String lnameVal= editSqlLName.getText().toString();
                String phoneVal= editSqlPhone.getText().toString();
                String emailVal= editSqlEmail.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(sqlActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                }
                else if(fnameVal.isEmpty()||lnameVal.isEmpty()|| phoneVal.isEmpty()|| emailVal.isEmpty()){

                    Toast.makeText(sqlActivity.this, "Empty fields are required for inserting", Toast.LENGTH_SHORT).show();

                } else{
                    int idVal= Integer.parseInt(idString);

                    boolean insert= myDB.addUser(idVal, fnameVal, lnameVal, phoneVal, emailVal);
                    if(insert)
                        Toast.makeText(sqlActivity.this, "Successful insert of new user.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(sqlActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });


        delSqlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString= editSqlID.getText().toString();

                if(idString.isEmpty()){
                    Toast.makeText(sqlActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                }else {
                    int idVal = Integer.parseInt(idString);

                    int i=myDB.deleteUser(idVal);
                    if(i==0){
                        Toast.makeText(sqlActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(sqlActivity.this, "Succesful deletion of User#"+idVal, Toast.LENGTH_SHORT).show();
                    }


                }
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
                    Toast.makeText(sqlActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                }else {

                    editSqlID.setText(String.valueOf(userId));
                    editSqlFName.setText(name);
                    editSqlLName.setText(lname);
                    editSqlEmail.setText(email);
                    editSqlPhone.setText(phone);

                   boolean insert= myDB.addUser(userId, name, lname, phone, email);

                    if(insert)
                        Toast.makeText(sqlActivity.this, "Firebase User#"+userId+" found and \ninserted into SQL database", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(sqlActivity.this, "User already exists in SQL", Toast.LENGTH_SHORT).show();
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



                            temperatureSql.setText(String.valueOf(temp)+" °C");
                            citySql.setText(town);
                            humidSql.setText("Humidity: "+humidity+"%");


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
                            .into(wIconSql);
                }
                if(weather.equals("Clouds")){
                    Glide.with(this)
                            .load(R.drawable.iconfinder_cloudy_2995001)
                            .into(wIconSql);
                }if(weather.equals("Rainy")){
                    Glide.with(this)
                            .load(R.drawable.iconfinder_rain)
                            .into(wIconSql);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}