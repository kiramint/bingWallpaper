package com.firstgroup.bingwallpaper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firstgroup.bingwallpaper.bingApi.BingWallpaper;
import com.firstgroup.bingwallpaper.bingApi.Wallpaper;
import com.firstgroup.bingwallpaper.bingApi.WallpaperCallback;
import com.firstgroup.bingwallpaper.saveData.SaveDataStruct;
import com.firstgroup.bingwallpaper.saveData.Savedata;

import java.util.ArrayList;
import java.util.HashSet;


class LoveRecyAdapter extends RecyclerView.Adapter<RecyHolder> {

    @NonNull
    @Override
    public RecyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View childItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_container, parent, false);
        return new RecyHolder(childItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyHolder holder, int position) {
        holder.setWallpaper(LoveFragment.wallpapers.get(position));
    }

    @Override
    public int getItemCount() {
        return LoveFragment.wallpapers.size();
    }
}

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static ArrayList<Wallpaper>wallpapers;

    public LoveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoveFragment newInstance(String param1, String param2) {
        LoveFragment fragment = new LoveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_love, container, false);

        if(Savedata.getInstance().ifExist()){
            wallpapers = Savedata.getInstance().read().wallpapers;
            if(wallpapers != null){
                RecyclerView recyclerView = view.findViewById(R.id.loveRecycle);
                LoveRecyAdapter adapter = new LoveRecyAdapter();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
        }

        return view;
    }
}