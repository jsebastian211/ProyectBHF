package com.sgd.pawfriends.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.sgd.pawfriends.R;
import com.sgd.pawfriends.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Daza on 26/08/2017.
 */

public class StartScreen2Fragment extends Fragment {

    @BindView(R.id.start_image_2)
    ImageView imageView;
    @BindView(R.id.rootLayout2)
    RelativeLayout relativeLayout;
    @BindView(R.id.btn_first_launch2)
    ImageButton imageButton;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.imageView6)
    ImageView imageView6;

    public StartScreen2Fragment() {
    }

    public static StartScreen2Fragment newInstance() {
        StartScreen2Fragment fragment = new StartScreen2Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_start_2, container, false);
        ButterKnife.bind(this, rootView);
        Glide.with(this.getContext()).load(R.drawable.start_screen_2).centerCrop().into(imageView);
        imageView.setVisibility(View.VISIBLE);
        //Animation fadeIn = new AlphaAnimation(0, 1);
        //fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
        //fadeIn.setDuration(2500);
        //relativeLayout.startAnimation(fadeIn);
          /*  relativeLayout.animate().alpha(1f).setDuration(1700).withEndAction(
                    new Runnable() {
                        @Override
                        public void run() {
                            imageView.setVisibility(View.VISIBLE);
                            imageView2.setVisibility(View.VISIBLE);
                            imageView3.setVisibility(View.VISIBLE);
                            imageView4.setVisibility(View.VISIBLE);
                            imageView5.setVisibility(View.VISIBLE);
                            imageView6.setVisibility(View.VISIBLE);
                        }
                    }
            ).start();
            */

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