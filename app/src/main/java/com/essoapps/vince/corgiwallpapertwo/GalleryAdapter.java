package com.essoapps.vince.corgiwallpapertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;



public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<GalleryItem> mGalleryItems;
    private String TAG="GalleryAdapter";

    public GalleryAdapter(List<GalleryItem> galleryItems){
        mGalleryItems = galleryItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView galleryImageView;

        public ViewHolder(View itemView){
            super(itemView);
            galleryImageView = itemView.findViewById(R.id.gallery_iv);

        }
    }


    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View galleryView = inflater.inflate(R.layout.gallery_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(galleryView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder viewHolder, int i) {

        GalleryItem galleryItem = mGalleryItems.get(i);

        ImageView imageView = viewHolder.galleryImageView;

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.spinner);

        Log.d(TAG, "url:" + galleryItem.getUrl());


            GlideApp.with(imageView.getContext())
                    .load(galleryItem.getUrl())
                    .apply(options)
                    .into(imageView);

    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }
}
