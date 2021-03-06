package com.sgd.pawfriends.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sgd.pawfriends.R;

import java.util.List;

/**
 * Created by Daza on 01/07/2017.
 */

public class HorizontalRecyclerView extends RecyclerView.Adapter<HorizontalRecyclerView.MyViewHolder> {

    private List<String> horizontalList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.txtExample);

        }
    }


    public HorizontalRecyclerView(List<String> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_news, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtView.setText(horizontalList.get(position));

        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, holder.txtView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}