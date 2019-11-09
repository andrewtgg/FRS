package com.hw.frsecurity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// Probably should extract this to handle more listviews instead of just employee

public class ListAdapter extends BaseAdapter {

    Context context;
    private ArrayList<Employee> A = new ArrayList<>();
    // private final String [] employeeId;
    // private final String [] employeeName;
    // private final String [] employeeLastSeen;
    // private final int [] employeeImg;


    // Experimenting
    public ListAdapter(Context context, ArrayList<Employee> arr) {
        this.context = context;
        this.A = (ArrayList<Employee>) arr.clone();
    }
    /*
    public ListAdapter(Context context, String [] employeeId, String [] employeeName, String [] employeeLastSeen, int[] employeeImg){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeLastSeen = employeeLastSeen;
        this.employeeImg =  employeeImg;
    }
    */

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
            convertView = inflater.inflate(R.layout.single_employee_list_item, parent, false);
            viewHolder.employeeId = convertView.findViewById(R.id.employee_item_id_tv);
            viewHolder.employeeName = convertView.findViewById(R.id.employee_item_name_tv);
            viewHolder.employeeLastSeen = convertView.findViewById(R.id.employee_item_last_seen_tv);
            viewHolder.employeeImg = convertView.findViewById(R.id.employee_item_img);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        // String employeeId = String.valueOf(A.get(position).getId());

        viewHolder.employeeId.setText(String.valueOf(A.get(position).getId()));
        viewHolder.employeeName.setText(A.get(position).getName());
        viewHolder.employeeLastSeen.setText(A.get(position).getLastSeen());
        // decodes byte array into image
        Bitmap img = BitmapFactory.decodeByteArray(A.get(position).getImg(), 0, A.get(position).getImg().length);
        viewHolder.employeeImg.setImageBitmap(img);
        // viewHolder.employeeImg.setImageResource(A.get(position).getImg());

        return convertView;
    }

    private static class ViewHolder {

        TextView employeeId;
        TextView employeeName;
        TextView employeeLastSeen;
        ImageView employeeImg;

    }

}