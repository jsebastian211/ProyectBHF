package com.sgd.pawfriends.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sgd.pawfriends.PetsActivity;
import com.sgd.pawfriends.R;
import com.sgd.pawfriends.custom.HorizontalRecyclerView;
import com.sgd.pawfriends.custom.PawFriendsConstants;
import com.sgd.pawfriends.custom.StartOffsetItemDecoration;
import com.sgd.pawfriends.custom.Utilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.info1)
    TextView mTextView1;
    @BindView(R.id.info2)
    TextView mTextView2;
    @BindView(R.id.info3)
    TextView mTextView3;
    @BindView(R.id.rvRecentNews)
    RecyclerView mRvRecentNews;
    @BindView(R.id.layout_principal_fragment)
    LinearLayout mLayoutPrincipalFragment;
    @BindView(R.id.layout_scroll_view)
    ScrollView mScrollViewPrincipal;
    @BindView(R.id.layout_option_register)
    LinearLayout mLayoutOptionRegister;
    @BindView(R.id.layout_option_vets)
    LinearLayout mLayoutOptionVets;
    @BindView(R.id.layout_option_places)
    LinearLayout mLayoutOptionPlaces;
    @BindView(R.id.layout_option_lawtips)
    LinearLayout mLayoutOptionLawTips;
    @BindView(R.id.layout_option_adopt)
    LinearLayout mLayoutOptionAdopt;
    @BindView(R.id.layout_option_find)
    LinearLayout mLayoutOptionFind;
    private ArrayList<String> mHorizontalList;
    private HorizontalRecyclerView mHorizontalRecyclerView;
    private FirebaseAuth mFirebaseAuth;

    public PrincipalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
        ButterKnife.bind(this, rootView);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //COLOCARLO EN EL FRAGMENTO

        mHorizontalList = new ArrayList<>();
        mHorizontalList.add("horizontal 1");
        mHorizontalList.add("horizontal 2");
        mHorizontalList.add("horizontal 3");
        mHorizontalList.add("horizontal 4");
        mHorizontalList.add("horizontal 5");
        mHorizontalList.add("horizontal 6");
        mHorizontalList.add("horizontal 7");
        mHorizontalList.add("horizontal 8");
        mHorizontalList.add("horizontal 9");
        mHorizontalList.add("horizontal 10");


        mHorizontalRecyclerView = new HorizontalRecyclerView(mHorizontalList, getContext());


        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager verticalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRvRecentNews.setLayoutManager(horizontalLayoutManagaer);

        mLayoutOptionRegister.setOnClickListener(this);


        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRvRecentNews.getContext(),
                horizontalLayoutManagaer.getOrientation());

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.line_divider));

        mRvRecentNews.addItemDecoration(dividerItemDecoration);

        int offsetPx = Math.round(Utilities.convertDpToPixel(10,this));
        */

        mRvRecentNews.addItemDecoration(new StartOffsetItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.line_divider)));

        SnapHelper helper = new LinearSnapHelper();

        helper.attachToRecyclerView(mRvRecentNews);

        mRvRecentNews.setAdapter(mHorizontalRecyclerView);

        method(mFirebaseAuth.getCurrentUser());

        return rootView;
    }


    public void method(FirebaseUser user) {
        String method = Utilities.validateRegisterMethod(user);
        if (method.equals(PawFriendsConstants.FACEBOOK_LOGIN)) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();

            mTextView1.setText(name);
            mTextView2.setText(email);
            mTextView3.setText(uid);
        } else if (method.equals(PawFriendsConstants.EMAIL_LOGIN)) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();

            mTextView1.setText(name);
            mTextView2.setText(email);
            mTextView3.setText(uid);
        } else if (method.equals(PawFriendsConstants.ANONYMOUS_LOGIN)) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();

            mTextView1.setText(name);
            mTextView2.setText(email);
            mTextView3.setText(uid);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.layout_option_register) {
            Intent in = new Intent(getActivity(), PetsActivity.class);
            startActivity(in);
        }
    }
}
