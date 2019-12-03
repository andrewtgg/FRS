package com.hw.frsecurity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityLogAdapter extends BaseAdapter {

    Context context;
    private ArrayList<ActivityLogItem> A = new ArrayList<>();

    // Experimenting
    public ActivityLogAdapter(Context context, ArrayList<ActivityLogItem> arr) {
        this.context = context;
        this.A = (ArrayList<ActivityLogItem>) arr.clone();
    }

    @Override
    public int getCount() {
        return A.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_access_list_item, parent, false);

            viewHolder.employeeId = convertView.findViewById(R.id.access_log_item_id);
            viewHolder.employeeStatus = convertView.findViewById(R.id.access_log_item_status_id);
            viewHolder.employeeTimeSeen = convertView.findViewById(R.id.access_log_item_time_id);
            viewHolder.employeeDateSeen = convertView.findViewById(R.id.access_log_item_date_id);
            viewHolder.employeeImg = convertView.findViewById(R.id.employee_item_img);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        String employeeID = Integer.toString(A.get(position).getId());
        viewHolder.employeeId.setText("ID: " + employeeID);
        if (A.get(position).getStatus() == 0) {
            viewHolder.employeeImg.setBackgroundColor(Color.parseColor("#FF0000"));
            viewHolder.employeeStatus.setText("Status: Unidentified");
        } else {
            viewHolder.employeeImg.setBackgroundColor(Color.parseColor("#FFFFFF"));
            viewHolder.employeeStatus.setText("Status: Identified");
        }
        viewHolder.employeeTimeSeen.setText("Time: " + A.get(position).getTimeSeen());
        viewHolder.employeeDateSeen.setText("Date: " + A.get(position).getDateSeen());
        // decodes byte array into image
        Bitmap img = BitmapFactory.decodeByteArray(A.get(position).getImg(), 0, A.get(position).getImg().length);
        viewHolder.employeeImg.setImageBitmap(img);
        // viewHolder.employeeImg.setImageResource(A.get(position).getImg());

        return convertView;
    }

    private static class ViewHolder {

        TextView employeeId;
        TextView employeeStatus;
        TextView employeeTimeSeen;
        TextView employeeDateSeen;
        ImageView employeeImg;

    }



}
