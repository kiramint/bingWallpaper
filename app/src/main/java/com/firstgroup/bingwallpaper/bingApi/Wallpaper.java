package com.firstgroup.bingwallpaper.bingApi;

import androidx.annotation.Nullable;

import java.io.Serial;
import java.io.Serializable;

public class Wallpaper implements Serializable {
    public String stardate;
    public String fullstartdate;
    public String enddate;
    public String url;
    public String urlbase;
    public String copyright;
    public String copyrightlink;
    public String title;
    public String quiz;

    public String getUrl() {
        return "https://cn.bing.com" + url;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            Wallpaper wallpaper = (Wallpaper) obj;
            if (wallpaper == null) {
                return false;
            } else {
                return wallpaper.url.equals(url);
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
