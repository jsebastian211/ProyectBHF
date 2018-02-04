package com.sgd.pawfriends.custom;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.Arrays;

/**
 * Created by Daza on 29/10/2017.
 */

public class LimitArrayAdapter<T> extends ArrayAdapter<T> {

    final int LIMIT = 2;

    //overload other constructors you're using
    public LimitArrayAdapter(Context context, int textViewResourceId,
                             T[] objects) {
        super(context, textViewResourceId, Arrays.asList(objects));
    }

    @Override
    public int getCount() {
        return Math.min(LIMIT, super.getCount());
    }

}