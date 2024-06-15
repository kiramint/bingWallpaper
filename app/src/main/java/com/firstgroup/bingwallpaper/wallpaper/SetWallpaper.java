package com.firstgroup.bingwallpaper.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firstgroup.bingwallpaper.R;
import com.firstgroup.bingwallpaper.bingApi.Wallpaper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SetWallpaper extends AppCompatActivity {

    private static final int UCROP_RESULT = 101010;
    public static final String SET_WALLPAPER_EXTRA = "setWallpaperExtra";
    private ImageView imageView;
    private String originalFileName;
    private String croppedFileName;
    private Uri cropUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_wallpaper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Wallpaper wallpaper;
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            wallpaper = intent.getSerializableExtra(SET_WALLPAPER_EXTRA, Wallpaper.class);
        }else {
            wallpaper = (Wallpaper) intent.getSerializableExtra(SET_WALLPAPER_EXTRA);
        }

        imageView = findViewById(R.id.CropImage);
        Button button = findViewById(R.id.SetAsWallpaper);

        assert wallpaper != null;
        Glide.with(imageView)
                .load(wallpaper.getUrl())
                .placeholder(R.drawable.bing_logo)
                .error(R.drawable.error_96)
                .centerCrop()
                .into(imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cropUri != null) {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(SetWallpaper.this);
                    try {
                        wallpaperManager.setBitmap(BitmapFactory.decodeFile(cropUri.getPath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(SetWallpaper.this,"Wallpaper set",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    crop(wallpaper);
                }
            }
        });

        crop(wallpaper);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(originalFileName!=null){
            File delete = new File(originalFileName);
            if(delete.exists()){
                boolean result = delete.delete();
            }
        }
        if(croppedFileName!=null){
            File delete = new File(croppedFileName);
            if(delete.exists()){
                boolean result = delete.delete();
            }
        }
    }

    public void crop(Wallpaper wallpaper) {
        Glide.with(this)
                .asBitmap()
                .load(wallpaper.getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        originalFileName = "tempImage-" + UUID.randomUUID().toString() + ".jpg";
                        croppedFileName = "tempImage-" + UUID.randomUUID().toString() + ".jpg";
                        Uri sourceUri = getTempUri(resource, originalFileName);
                        if(sourceUri == null){
                            return;
                        }
                        File result = new File(getFilesDir(),croppedFileName);
                        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(result));
                        uCrop.start(SetWallpaper.this, UCROP_RESULT);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private Uri getTempUri(Bitmap bitmap, String fileName) {
        File imageFile = new File(getFilesDir(),fileName);
        try {
            if(imageFile.createNewFile()){
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }else {
                throw new IOException();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_LONG).show();
            Log.e("ERROR SET WALLPAPER","ERR:",e);
            return null;
        }
        return Uri.fromFile(imageFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCROP_RESULT && resultCode == RESULT_OK && data != null) {
            cropUri = UCrop.getOutput(data);
            if (cropUri != null) {
                imageView.setImageURI(cropUri);
            }else {
                finish();
            }
        }
    }
}