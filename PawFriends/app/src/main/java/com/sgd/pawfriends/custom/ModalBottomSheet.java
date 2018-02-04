package com.sgd.pawfriends.custom;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgd.pawfriends.R;

/**
 * Created by Daza on 09/09/2017.
 */

public class ModalBottomSheet
        extends BottomSheetDialogFragment {

    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.bottom_add_pets, container, false);

        return v;
    }
}