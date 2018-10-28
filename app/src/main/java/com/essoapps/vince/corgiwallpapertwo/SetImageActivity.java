package com.essoapps.vince.corgiwallpapertwo;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class SetImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);

        final CropImageView cropImageView = findViewById(R.id.cropImageView);

        cropImageView.setGuidelines(CropImageView.Guidelines.ON);

        Intent intent = getIntent();
        Uri bitmapUri = intent.getParcelableExtra("bitmapUri");

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), bitmapUri);
            cropImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button setWallpaperButton = findViewById(R.id.set_wallpaper_btn);
        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //do cropping
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            Bitmap croppedImg = cropImageView.getCroppedImage();
            myWallpaperManager.setBitmap(croppedImg);
            Toast.makeText(v.getContext(), "Wallpaper Set!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(v.getContext(), "Error, wallpaper could not be set!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
});


    }

}
