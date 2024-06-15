package com.firstgroup.bingwallpaper;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firstgroup.bingwallpaper.bingApi.Wallpaper;
import com.firstgroup.bingwallpaper.saveData.SaveDataStruct;
import com.firstgroup.bingwallpaper.saveData.Savedata;

import java.util.ArrayList;
import java.util.HashSet;

class RecyHolder extends RecyclerView.ViewHolder {
    View thisView;
    private final ImageView imageView;
    private final Button likeButton;
    private Wallpaper wallpaper;
    private Boolean ifLike = false;
    private int textColor;

    public void setWallpaper(Wallpaper wallpaper) {
        this.wallpaper = wallpaper;
        if(Savedata.getInstance().ifExist()){
            SaveDataStruct saveCheck = Savedata.getInstance().read();
            if(saveCheck.exist.contains(wallpaper.getUrl())){
                textColor=likeButton.getCurrentTextColor();
                likeButton.setTextColor(0xffff0038);
                ifLike = true;
            }
        }
        Glide.with(thisView)
                .load(wallpaper.getUrl())
                .placeholder(R.drawable.bing_logo)
                .error(R.drawable.error_96)
                .centerCrop()
                .into(imageView);
    }

    public RecyHolder(@NonNull View itemView) {
        super(itemView);
        thisView = itemView;
        imageView = itemView.findViewById(R.id.RecyImage);
        likeButton = itemView.findViewById(R.id.LikeButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoPage.class);
                intent.putExtra(InfoPage.EXTRA_FLAG,wallpaper);
                v.getContext().startActivity(intent);
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDataStruct saveDataStruct;
                Savedata savedata = Savedata.getInstance();
                if (savedata.ifExist()) {
                    saveDataStruct = savedata.read();
                } else {
                    saveDataStruct = new SaveDataStruct();
                    saveDataStruct.wallpapers = new ArrayList<>();
                    saveDataStruct.exist = new HashSet<>();
                }
                String id = wallpaper.getUrl();
                if (!ifLike) {
                    if (!saveDataStruct.exist.contains(id)) {
                        saveDataStruct.wallpapers.add(wallpaper);
                        saveDataStruct.exist.add(id);
                        savedata.save(saveDataStruct);
                    }
                    textColor=likeButton.getCurrentTextColor();
                    likeButton.setTextColor(0xffff0038);
                    ifLike = true;
                }else {
                    if (saveDataStruct.exist.contains(id)) {
                        saveDataStruct.wallpapers.remove(wallpaper);
                        saveDataStruct.exist.remove(id);
                        savedata.save(saveDataStruct);
                    }
                    likeButton.setTextColor(textColor);
                    ifLike = false;
                }
            }
        });
    }
}
