package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public class JourneyAlbumRecyclerActivity extends Activity implements AdapterView.OnItemClickListener {

    private RecyclerView myRecycler;
    private MyDataBase db;
    private CustomAdapter customAdapter;
    private MyHelper helper;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_album_recycler);
        myRecycler = (RecyclerView) findViewById(R.id.rvItems);

        db = new MyDataBase(this);
        helper = new MyHelper(this);
        Cursor cursor = db.getData();

       //get the info from each column
        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.LOCATION);
        int index3 = cursor.getColumnIndex(Constants.DATE);

        //put info into the arraylist that the recycler will use to display the info
        ArrayList<String> myArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String journeyName = cursor.getString(index1);
            String journeyLocation = cursor.getString(index2);
            String journeyDate = cursor.getString(index3);
            String s = journeyName +"," + journeyLocation + "," + journeyDate;
            myArrayList.add(s);
            cursor.moveToNext();
        }
        customAdapter = new CustomAdapter(myArrayList);
        myRecycler.setAdapter(customAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}