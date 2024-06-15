package com.firstgroup.bingwallpaper.bingApi;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class WallpaperCallback implements Callback {
    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        ArrayList<Wallpaper> ResultList = new ArrayList<>();
        ResultList.add(new Wallpaper());
        Result(ResultList);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        ArrayList<Wallpaper> ResultList = new ArrayList<>();
        assert response.body() != null;
        String responseJson = response.body().string();
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject imageObject = jsonArray.getJSONObject(i);
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.stardate = imageObject.getString("startdate");
                wallpaper.fullstartdate = imageObject.getString("fullstartdate");
                wallpaper.enddate = imageObject.getString("enddate");
                wallpaper.url = imageObject.getString("url");
                wallpaper.urlbase = imageObject.getString("urlbase");
                wallpaper.copyright = imageObject.getString("copyright");
                wallpaper.copyrightlink = imageObject.getString("copyrightlink");
                wallpaper.title = imageObject.getString("title");
                wallpaper.quiz = imageObject.getString("quiz");
                ResultList.add(wallpaper);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Result(ResultList);
    }
    public abstract void Result(ArrayList<Wallpaper> wallpapers);
}
