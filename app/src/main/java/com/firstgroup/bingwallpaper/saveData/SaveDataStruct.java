package com.firstgroup.bingwallpaper.saveData;

import com.firstgroup.bingwallpaper.bingApi.Wallpaper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class SaveDataStruct implements Serializable {
    public ArrayList<Wallpaper>wallpapers;
    public HashSet<String>exist;
}
