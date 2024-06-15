package com.firstgroup.bingwallpaper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.firstgroup.bingwallpaper.bingApi.Wallpaper;
import com.firstgroup.bingwallpaper.saveData.ImageSaver;
import com.firstgroup.bingwallpaper.wallpaper.SetWallpaper;

public class InfoPage extends AppCompatActivity {
    public static final String EXTRA_FLAG = "extraFlag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Wallpaper wallpaper;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            wallpaper = intent.getSerializableExtra(EXTRA_FLAG, Wallpaper.class);
        } else {
            wallpaper = (Wallpaper) intent.getSerializableExtra(EXTRA_FLAG);
        }
        assert wallpaper != null;

        ImageView imageView = findViewById(R.id.InfoImage);
        TextView title = findViewById(R.id.InfoTitle);
        TextView copyright = findViewById(R.id.InfoCopyright);
        TextView url = findViewById(R.id.InfoURL);
        Button browser = findViewById(R.id.InfoBrowser);
        Button save = findViewById(R.id.InfoSave);
        Button setWallpaper = findViewById(R.id.InfoWallpaper);

        Glide.with(imageView)
                .load(wallpaper.getUrl())
                .placeholder(R.drawable.bing_logo)
                .error(R.drawable.error_96)
                .centerCrop()
                .into(imageView);

        title.setText(wallpaper.title);
        copyright.setText(wallpaper.copyright);
        url.setText(wallpaper.getUrl());

        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wallpaper.getUrl()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    Toast.makeText(InfoPage.this, "Android version lower than 10(Q) is not supported!", Toast.LENGTH_LONG).show();
                } else {
                    ImageSaver imageSaver = new ImageSaver(InfoPage.this);
                    imageSaver.saveImageToGallery(wallpaper.getUrl(), wallpaper.title);
                }
            }
        });

        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoPage.this, SetWallpaper.class);
                intent.putExtra(SetWallpaper.SET_WALLPAPER_EXTRA,wallpaper);
                startActivity(intent);
            }
        });
    }
}