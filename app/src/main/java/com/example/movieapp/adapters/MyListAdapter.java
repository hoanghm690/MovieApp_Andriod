package com.example.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.models.Phim;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<Phim> mData;
    List<Phim> mDataOld;
    MovieItemClickListener movieItemClickListener;

    public MyListAdapter(Context context, List<Phim> mData, MovieItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        this.mDataOld = mData;
        movieItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Phim phim = mData.get(position);
        if(phim == null){
            return;
        }
        holder.TvTitle.setText(mData.get(position).getTitle());
        Picasso.with(context).load(mData.get(position).getImageUrl()).into(holder.ImgMovie);
    }

    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView TvTitle;
        private ImageView ImgMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            TvTitle = itemView.findViewById(R.id.item_movie_title_2);
            ImgMovie = itemView.findViewById(R.id.item_movie_img_2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieItemClickListener.onMovieClick(mData.get(getAdapterPosition()), ImgMovie);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()) {
                    mData = mDataOld;
                } else {
                    List<Phim> list = new ArrayList<>();
                    for (Phim movie : mDataOld) {
                        if (movie.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(movie);
                        }
                    }
                    mData = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mData;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mData = (List<Phim>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
