package com.essoapps.vince.corgiwallpapertwo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<GalleryItem> mGalleryList;
    String TAG = "MainActivity";
    private Boolean showEndToast;
    private String lastUpdate;
    private static final String AD_UNIT = "ca-app-pub-3940256099942544/6300978111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastUpdate="unknown";
        initAdMob(AD_UNIT);
        populateList();
        showEndToast=true;
        RecyclerView rvGallery = findViewById(R.id.gallery_rv);

        GalleryAdapter adapter = new GalleryAdapter(mGalleryList);

        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        firebasePopulate();

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
                                       Uri bitmapUri =  getLocalBitmapUri(resource,v.getContext());
                                        Intent intent = new Intent(v.getContext(), SetImageActivity.class);
                                        intent.putExtra("bitmapUri", bitmapUri);
                                        startActivity(intent);
                                    }
                                });
                    }
                });

        rvGallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if(showEndToast) {
                        Toast.makeText(MainActivity.this, "Sorry, that's all the corgi images for now. \nCorgi images last updated on: \n"+lastUpdate, Toast.LENGTH_LONG).show();
                        firebasePopulate();
                        showEndToast = false;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showEndToast=true;
    }

    private void initAdMob(String adUnit) {
        MobileAds.initialize(this,
                adUnit);

        AdView mAdView = findViewById(R.id.activity_main_ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void firebasePopulate(){
        Log.d(TAG, "populating database");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("gallery_items");
        DatabaseReference dateRef = database.getReferenceFromUrl("https://corgiwallapertwo.firebaseio.com/info/0/lastUpdated");

        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastUpdate = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "success");
                Log.d(TAG, "count is:" +dataSnapshot.getChildrenCount());
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d(TAG, ds.child("url").getValue().toString());
                    GalleryItem galleryItem = new GalleryItem();
                    galleryItem.setUrl(ds.child("url").getValue().toString());
                    if(!mGalleryList.contains(galleryItem))
                        mGalleryList.add(galleryItem);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "fail");
            }
        });

    }


    public static Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(context.getFilesDir(), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void populateList(){
        mGalleryList = new ArrayList<>();
        //need atleast one dummy holder
        mGalleryList.add(new GalleryItem("https://s3.amazonaws.com/cdn-origin-etr.akc.org/wp-content/uploads/2017/11/12225900/Pembroke-Welsh-Corgi-On-White-07.jpg","desc", true));

    }

}
