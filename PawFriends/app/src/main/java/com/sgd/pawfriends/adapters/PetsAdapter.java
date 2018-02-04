package com.sgd.pawfriends.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sgd.pawfriends.R;
import com.sgd.pawfriends.custom.Utilities;
import com.sgd.pawfriends.entities.NdPets;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Daza on 03/09/2017.
 */

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetsViewHolder> {

    private final Context context;
    List<NdPets> lstPets;
    View.OnClickListener listener;

    public PetsAdapter(Context context, List<NdPets> lstPets, View.OnClickListener listener) {
        this.context = context;
        this.lstPets = lstPets;
        this.listener = listener;
    }

    @Override
    public PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        PetsViewHolder holder = new PetsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PetsViewHolder holder, int position) {
        if (position != 0) {
            NdPets pet = lstPets.get(position);
            String url = pet.getUrlImage();
            holder.petName.setText(pet.getName());
            holder.pet = pet;
            /*
            if(pet.getLocalUriImage() != null || !pet.getLocalUriImage().equals("")){

                Glide.with(context)
                        .load(new File(pet.getLocalUriImage()))
                        .placeholder(R.drawable.add_option)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .animate(R.anim.image_fade_in)
                        .centerCrop()
                        .into(holder.petImage);

                Uri image = Uri.parse("content:/".concat(pet.getLocalUriImage()));
                holder.petImage.setImageURI(image);
        }
            else
                        */
            if (url != null && Utilities.validateUrl(url)) {
                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.add_option)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .animate(R.anim.image_fade_in)
                        .centerCrop()
                        .into(holder.petImage);
            }
        } else {
            holder.petName.setText(context.getString(R.string.add_option));
            Glide.with(context).load(R.drawable.add_option).centerCrop().into(holder.petImage);
        }

        holder.petImage.setTag(R.string.tag_pet, position);
        holder.petImage.setOnClickListener(listener);

    }

    public NdPets getPet(int position) {
        return lstPets.get(position);
    }


    @Override
    public int getItemCount() {
        return lstPets.size();
    }

    class PetsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgPetImage)
        CircularImageView petImage;
        @BindView(R.id.txtPetName)
        TextView petName;
        NdPets pet;

        public PetsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
