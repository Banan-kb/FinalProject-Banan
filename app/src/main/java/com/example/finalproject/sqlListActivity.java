package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class sqlListActivity extends AppCompatActivity {
    ArrayList<String> userArrayList= new ArrayList<>();
    DatabaseHelper myDB= new DatabaseHelper(this);
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_list);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                userArrayList);

        final ListView sqlList= (ListView) findViewById(R.id.sqlList);
        sqlList.setAdapter(adapter);

        Cursor cur= myDB.getListContents();
        String s;
        while(cur.moveToNext()){
            s="UserID: "+cur.getString(0)+"\n";
            s+="First Name: "+cur.getString(1)+"\n";
            s+="Last Name: "+cur.getString(2)+"\n";
            s+="Email: "+cur.getString(4)+"\n";
            s+="Phone Number: "+cur.getString(3);

           userArrayList.add(s);
        }

        sqlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String [] temp= userArrayList.get(position).split("First Name: ");
                String[] fname= temp[1].split("\nLast Name: ");
                String [] lName= fname[1].split("\nEmail: ");



                Toast.makeText(sqlListActivity.this,fname[0]+" "+lName[0].toString(), Toast.LENGTH_LONG).show();

            }
        });

    }
}