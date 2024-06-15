package com.firstgroup.bingwallpaper;

import android.content.Context;
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

class HomeRecyHolder extends RecyclerView.ViewHolder {
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

    public HomeRecyHolder(@NonNull View itemView) {
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

class HomeRecyAdapter extends RecyclerView.Adapter<HomeRecyHolder> {

    @NonNull
    @Override
    public HomeRecyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View childItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_container, parent, false);
        return new HomeRecyHolder(childItem);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyHolder holder, int position) {
        holder.setWallpaper(HomeFragment.wallpapers.get(position));
    }

    @Override
    public int getItemCount() {
        return HomeFragment.wallpapers.size();
    }
}

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int lastInsert = 0;
    private int day = 0;
    public static ArrayList<Wallpaper> wallpapers = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void addFavorite(Context context, Wallpaper wallpaper) {

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.homeRecycle);
        HomeRecyAdapter adapter = new HomeRecyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        BingWallpaper.getWallpaper(new WallpaperCallback() {
            @Override
            public void Result(ArrayList<Wallpaper> wallpapers) {
                requireActivity().runOnUiThread(() -> {
                    HomeFragment.wallpapers = wallpapers;
                    recyclerView.setAdapter(adapter);
                    lastInsert += wallpapers.size();
                    day += 1;
                });
            }
        }, 8, day);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && day <= 7) {
                    BingWallpaper.getWallpaper(new WallpaperCallback() {
                        @Override
                        public void Result(ArrayList<Wallpaper> wallpapers) {
                            requireActivity().runOnUiThread(() -> {
                                HomeFragment.wallpapers.addAll(wallpapers);
                                adapter.notifyItemInserted(lastInsert);
                                lastInsert += wallpapers.size();
                                day += 1;
                            });
                        }
                    }, 8, day);
                }
            }
        });

        return view;
    }
}