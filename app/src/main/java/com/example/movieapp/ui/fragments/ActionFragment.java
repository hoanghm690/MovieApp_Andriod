package com.example.movieapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.R;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.models.Phim;

import java.util.ArrayList;
import java.util.List;


public class ActionFragment extends Fragment {

    RecyclerView recyclerView;
    List<Phim> dataHolder;

    public ActionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAction);
//        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));

        dataHolder = new ArrayList<>();
        dataHolder.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60de6d7faa1bb691e514b6d4_poster-anh-la-mua-xuan-cua-em.jpg", "Anh Là Mùa Xuân Của Em"));
        dataHolder.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60de6d7faa1bb691e514b6d4_poster-anh-la-mua-xuan-cua-em.jpg", "Anh Là Mùa Xuân Của Em"));
        dataHolder.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60de6d7faa1bb691e514b6d4_poster-anh-la-mua-xuan-cua-em.jpg", "Anh Là Mùa Xuân Của Em"));
        dataHolder.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60ddc1a2085d2983a0be88fa_poster-toi-da-lo-yeu-muc-tieu-truy-sat-cua-minh.jpg", "Tôi Đã Lỡ Yêu Mục Tiêu Truy Sát Của Mình"));
        dataHolder.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60d96d5835f810953887a808_poster-meo-hay-cau-nguyen.jpg", "Meo, Hãy Cầu Nguyện"));
        dataHolder.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60d6f009b1a6a7b00d2ceaf4_poster-tham-phan-ac-ma.jpg", "Thẩm Phán Ác Ma"));

        recyclerView.setAdapter(new MovieAdapter(this.getContext(), dataHolder));
        return view;
    }
}