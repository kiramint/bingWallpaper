package com.firstgroup.bingwallpaper.saveData;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class ImageSaver {
    private final Context context;

    public ImageSaver(Context context) {
        this.context = context;
    }

    public void saveImageToGallery(String imageUrl,String name) {

        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(resource, name);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void saveImage(Bitmap bitmap, String name) {
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "BingWallpaper");
            values.put(MediaStore.Images.Media.IS_PENDING, true);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DISPLAY_NAME, name);

            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    fos = context.getContentResolver().openOutputStream(uri);
                    if (fos != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();
                        values.put(MediaStore.Images.Media.IS_PENDING, false);
                        context.getContentResolver().update(uri, values, null, null);
                        Toast.makeText(context, "Image saved to album", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(context, "Failed to saved image", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, name + ".jpg");
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    fos = Files.newOutputStream(image.toPath());
                }else {
                    fos = new FileOutputStream(image);
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, image.getAbsolutePath());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Toast.makeText(context, "Image saved to album", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context, "Failed to saved image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

