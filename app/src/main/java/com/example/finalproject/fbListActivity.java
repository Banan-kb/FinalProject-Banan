package com.example.finalproject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fbListActivity extends AppCompatActivity {

    ArrayList<String> userArrayList= new ArrayList<>();
    DatabaseReference dbRef;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_list);



        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                userArrayList);


        final ListView fbList= (ListView) findViewById(R.id.fbList);
        fbList.setAdapter(adapter);

        dbRef= FirebaseDatabase.getInstance().getReference();

        dbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
                userArrayList.clear();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                String id = String.valueOf(ds.child("userId").getValue(Integer.class));
                String name = ds.child("firstName").getValue(String.class);
                String lname = ds.child("lastName").getValue(String.class);
                String phone = ds.child("phoneNumber").getValue(String.class);
                String email = ds.child("emailAddress").getValue(String.class);

                String s= "UserID: "+id+"\nFirst Name: "+name+"\nLast Name: "+lname+"\nEmail: "+email+"\nPhone Number: "+phone;
                //Log.d("BANAN", s);
                userArrayList.add(s);
                //Log.d("BANAN", String.valueOf(userArrayList.contains(s)));
                adapter.notifyDataSetChanged();
            }

        }


        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w("BANAN", "Failed to read value.", error.toException());
        }
    });

        fbList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0; i<userArrayList.size(); i++){
                    if(i== position){
                        specificUserInfo(position);
                    }
                }
            }
        });

    }

    public void specificUserInfo(final int userId){
        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot ds = dataSnapshot.child(String.valueOf(userId));

                String name = ds.child("firstName").getValue(String.class);
                String lname = ds.child("lastName").getValue(String.class);


               // Log.d("BANAN ONE VAL", lname + " / " + name);
                String s= name+" "+lname;

                Toast.makeText(fbListActivity.this, s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("BANAN", "Failed to read value.", error.toException());
            }
        });

    }


}