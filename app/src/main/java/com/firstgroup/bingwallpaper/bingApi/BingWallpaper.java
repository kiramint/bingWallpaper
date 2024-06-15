package com.firstgroup.bingwallpaper.bingApi;

import okhttp3.*;

class GetWallpaperLinks {
    static public final String JSON = "js";
    static public final String XML = "xml";
    static public final String CHINA = "zh-CN";
    static public final String US = "en-US";

    static void getLink(String format, String market, int n, Callback callback, int day) {
        Request request = new Request.Builder()
                .url("https://cn.bing.com/HPImageArchive.aspx?format=" + format +
                        "&idx=0" + day + "&n=" + n +
                        "1&mkt=" + market).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}

public class BingWallpaper {
    public static void getWallpaper(WallpaperCallback wallpaperCallback, int n,int day) {
        GetWallpaperLinks.getLink(GetWallpaperLinks.JSON, GetWallpaperLinks.CHINA, n, wallpaperCallback,day);
    }
}
