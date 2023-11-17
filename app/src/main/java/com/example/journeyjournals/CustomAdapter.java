package com.example.journeyjournals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//this class creates the actual view from the recycler.
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    public ArrayList<String> list;

    public CustomAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_album_layout,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    //display the thumbnail info from the database into the recycler arraylist
    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, int position) {

        String[]  results = (list.get(position).toString()).split(",");
        holder.albumJourneyNameTextView.setText(results[0]);
        holder.albumJourneyLocationTextView.setText(results[1]);
        holder.albumDateTextView.setText(results[2]);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView albumJourneyNameTextView;
        public TextView albumJourneyLocationTextView;
        public TextView albumDateTextView;
        public LinearLayout myLayout;

        Context context;

        //display the "thumbnail" info of each journey entry in the album view
        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            albumJourneyNameTextView = (TextView) itemView.findViewById(R.id.albumJourneyNameTextView);
            albumJourneyLocationTextView = (TextView) itemView.findViewById(R.id.albumJourneyLocationTextView);
            albumDateTextView = (TextView) itemView.findViewById(R.id.albumDateTextView);

            itemView.setOnClickListener(this);
            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    "You have clicked: " + ((TextView)view.findViewById(R.id.albumJourneyNameTextView)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
