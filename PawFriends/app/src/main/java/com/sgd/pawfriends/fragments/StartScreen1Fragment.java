package com.sgd.pawfriends.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sgd.pawfriends.R;
import com.sgd.pawfriends.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daza on 26/08/2017.
 */

public class StartScreen1Fragment extends Fragment {

    @BindView(R.id.start_image_1)
    ImageView imageView;
    @BindView(R.id.start_layout1)
    ConstraintLayout layout;
    @BindView(R.id.btn_first_launch1)
    ImageButton imageButton;
    @BindView(R.id.txt_welcome)
    TextView txtWelcome;

    public StartScreen1Fragment() {
    }

    public static StartScreen1Fragment newInstance() {
        StartScreen1Fragment fragment = new StartScreen1Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_start_1, container, false);
        ButterKnife.bind(this, rootView);
        Glide.with(this.getContext()).load(R.drawable.start_screen_1).centerCrop().into(imageView);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).getmViewPager().setCurrentItem(getItem(+1), true);
            }
        });
        return rootView;
    }

    private int getItem(int i) {
        return ((StartActivity) getActivity()).getmViewPager().getCurrentItem() + i;
    }
}
