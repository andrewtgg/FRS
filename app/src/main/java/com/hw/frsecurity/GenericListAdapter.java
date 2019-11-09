package com.hw.frsecurity;


// This is just for testing and see if its feasible to abstract the list adapter to a more generic one.
// found it from some dude on github

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class GenericListAdapter extends BaseAdapter {

    private Activity mActivity;
    private int mLayoutId;
    private ArrayList<Object> mObjectList;
    private ArrayList<String> mViewIdList;

    // zero or more objects!
    public GenericListAdapter(Activity mActivity, int mLayoutId, Object... objects) {
        this.mActivity = mActivity;
        this.mLayoutId = mLayoutId;
        mObjectList = new ArrayList<>();
        mViewIdList = new ArrayList<>();
        for (Object obj : objects) {
            mObjectList.add(obj);
        }
    }

    @Override
    public int getCount() {
        return mObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }


}
