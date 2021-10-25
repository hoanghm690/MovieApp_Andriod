package com.example.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.movieapp.R;
import com.example.movieapp.models.Phim;
import com.example.movieapp.ui.MovieDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Phim> mList;
    private MovieItemClickListener movieItemClickListener;
    private ImageView slideImg;
    private TextView slideText;
    private FloatingActionButton fab;

    public SliderPagerAdapter(Context mContext, List<Phim> mList, MovieItemClickListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        movieItemClickListener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item, null);

        slideImg = slideLayout.findViewById(R.id.slide_img);
        slideText = slideLayout.findViewById(R.id.slide_title);
        fab = slideLayout.findViewById(R.id.fab_slide);

        Picasso.with(mContext).load(mList.get(position).getImageUrl()).into(slideImg);
        slideText.setText(mList.get(position).getTitle());

        container.addView(slideLayout);

        slideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieItemClickListener.onMovieClick(mList.get(position), slideImg);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieItemClickListener.onMovieClick(mList.get(position), slideImg);
            }
        });

        return slideLayout;
    }

    @Override
    public int getCount() {
        if(mList != null){
            return mList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
