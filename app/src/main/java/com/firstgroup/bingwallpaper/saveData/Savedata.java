package com.firstgroup.bingwallpaper.saveData;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Savedata {
    private static Savedata savedata;
    private Context context = null;
    private Savedata(){

    }
    public void setContext(Context context){
        this.context = context;
    }
    public static Savedata getInstance(){
        if(savedata==null){
            savedata = new Savedata();
            return savedata;
        }else {
            return savedata;
        }
    }
    private final String LIKES = "Likes";
    public void save(SaveDataStruct wallpapers){
        try {
            FileOutputStream fos = context.openFileOutput(LIKES, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wallpapers);//写入
            fos.close();//关闭输入流
            oos.close();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
    public boolean ifExist(){
        try {
            FileInputStream fis = context.openFileInput(LIKES);
            return true;
        }catch (FileNotFoundException ex){
            return false;
        }

    }
    public SaveDataStruct read(){
        try {
            FileInputStream fis = context.openFileInput(LIKES);
            ObjectInputStream ois = new ObjectInputStream(fis);
            SaveDataStruct wallpapers = (SaveDataStruct) ois.readObject();
            fis.close();
            ois.close();
            return wallpapers;
        }catch (IOException | ClassNotFoundException ex){
            return null;
        }
    }
}
