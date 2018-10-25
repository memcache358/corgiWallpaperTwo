package com.essoapps.vince.corgiwallpapertwo;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button cropBtn;
    private List<GalleryItem> mGalleryList;
    String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListeners();
        populateList();

        RecyclerView rvGallery = findViewById(R.id.gallery_rv);

        GalleryAdapter adapter = new GalleryAdapter(mGalleryList);

        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        Log.d(TAG, "size" + adapter.getItemCount());

        ItemClickSupport.addTo(rvGallery)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, final View v) {

                        GlideApp.with(v.getContext())
                                .asBitmap()
                                .load(mGalleryList.get(position).getUrl())
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource,Transition<? super Bitmap> transition) {

                                       Uri bitmapUri =  getLocalBitmapUri(resource);

                                        Intent intent = new Intent(v.getContext(), SetImageActivity.class);
                                        intent.putExtra("bitmapUri", bitmapUri);
                                        startActivity(intent);
                                    }
                                });
                    }
                });
    }


    public void initListeners(){

    cropBtn = findViewById(R.id.crop_btn);
    cropBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SetImageActivity.class);
            startActivity(intent);
        }
    });


    }


    public static Uri getLocalBitmapUri(Bitmap bmp) {
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void populateList(){
        mGalleryList = new ArrayList<>();


        mGalleryList.add(new GalleryItem("https://i.pinimg.com/474x/47/90/2a/47902a355801c38f4009fb72c04365e3--corgi-dog-pembroke-welsh-corgi.jpg","desc", true));
        mGalleryList.add(new GalleryItem("https://i.pinimg.com/474x/b8/37/c0/b837c01f8faf242f515ed4e930b91be2--corgi-dog-corgi-pembroke.jpg","desc", true));
        mGalleryList.add(new GalleryItem("https://i.pinimg.com/474x/ff/a5/63/ffa56317b7e3c956e12f82f9f9ffb0d8.jpg","desc", true));

        for(int i =0; i < 20; i++){
            if(i%2 ==0) {
                mGalleryList.add(new GalleryItem("https://cdn1-www.dogtime.com/assets/uploads/2011/01/file_23192_pembroke-welsh-corgi-460x290.jpg", "desc" + i, true));
            }
            else{
                mGalleryList.add(new GalleryItem("https://vetstreet.brightspotcdn.com/dims4/default/79f1bd2/2147483647/crop/0x0%2B0%2B0/resize/645x380/quality/90/?url=https%3A%2F%2Fvetstreet-brightspot.s3.amazonaws.com%2F83%2F9e8de0a7f411e0a0d50050568d634f%2Ffile%2FPembroke-Welsh-Corgi-3-645mk62711.jpg", "desc" + i, false));
            }
        }
    }

}
