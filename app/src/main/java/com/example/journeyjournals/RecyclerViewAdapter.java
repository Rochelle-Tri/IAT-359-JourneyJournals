package com.example.journeyjournals;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<PhotoModel> photoList;

    public RecyclerViewAdapter(List<PhotoModel> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotoModel photoModel = photoList.get(position);
        holder.imageView.setImageBitmap(photoModel.getPhoto());
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    // method to update the photoList
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<PhotoModel> newPhotoList) {
//        photoList = newPhotoList;
        photoList.clear();
        photoList.addAll(newPhotoList);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}