package com.example.finalproject;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Firebase extends AppCompatActivity {
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;



    public Firebase(){
        FirebaseApp.initializeApp(this);
        myDatabase= FirebaseDatabase.getInstance();
        myRef= myDatabase.getReference();
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void allUserInfo(){
        // Read from the database
        final ArrayList<String> allInfo= new ArrayList<String>();

        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String id = String.valueOf(ds.child("userId").getValue(Integer.class));
                    String name = ds.child("firstName").getValue(String.class);
                    String s= id+" "+name;
                    Log.d("BANAN", s);
                    allInfo.add(s);
                    Log.d("BANAN", String.valueOf(allInfo.contains(s)));
                }



            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("BANAN", "Failed to read value.", error.toException());
            }
        });

    }

    public void specificUserInfo(final int userId){
        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot ds = dataSnapshot.child(String.valueOf(userId));

                String name = ds.child("firstName").getValue(String.class);
                String lname = ds.child("lastName").getValue(String.class);
                String phone = ds.child("phoneNumber").getValue(String.class);
                String email = ds.child("emailAddress").getValue(String.class);

                Log.d("BANAN ONE VAL", email + " / " + name);



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("BANAN", "Failed to read value.", error.toException());
            }
        });

    }

    public void writeNewUser(int userId, String firstName,String lastName,String phone, String email) {
        Log.d("Banan", "Writting a new user");

        User user = new User( userId,  firstName, lastName, phone,  email);
        myRef.child("users").child(String.valueOf(userId)).setValue(user);
    }

    public void writeWithSuccess(int userId, User user) {

        myRef.child("users").child(String.valueOf(userId)).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Banan", "SUCCESS writing...");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Banan", "Error: "+e);
            }
        });
    }


    public void updateUser(final int userId, final String field, final String data){

       myRef.child("users").child(String.valueOf(userId)).child(field).setValue(data);

    }

    public void removeUser(int userId){
        myRef.child("users").child(String.valueOf(userId)).removeValue();

    }





}
