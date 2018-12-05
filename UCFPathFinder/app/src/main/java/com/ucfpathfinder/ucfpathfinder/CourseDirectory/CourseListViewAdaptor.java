package com.ucfpathfinder.ucfpathfinder.CourseDirectory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ucfpathfinder.ucfpathfinder.R;

import java.util.List;

public class CourseListViewAdaptor extends BaseAdapter {

    private Context context;
    private List<Course> list;

    public CourseListViewAdaptor(Context context, List<Course> list)
    {
        setContext(context);
        setList(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getCourseID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_layout, parent, false);

        }
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.text_view_course_name);
        textViewItemName.setText(list.get(position).getClassName());
        Log.d("building name", list.get(position).getClassName());


        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }


}
