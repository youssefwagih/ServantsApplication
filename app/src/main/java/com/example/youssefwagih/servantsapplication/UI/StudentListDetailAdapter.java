package com.example.youssefwagih.servantsapplication.UI;

/**
 * Created by Youssef Wagih on 11/2/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youssefwagih.servantsapplication.Business.CircleTransform;
import com.example.youssefwagih.servantsapplication.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class StudentListDetailAdapter extends ArrayAdapter<HashMap<String,String>> {
Context context;
    public StudentListDetailAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public StudentListDetailAdapter(Context context, int resource, List<HashMap<String,String>> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.student_list_item_layout, null);
        }

        HashMap<String, String> newsDetails = getItem(position);
        TextView newsTitleTV = (TextView) v.findViewById(R.id.newsTitleTV);
        ImageView newsIV = (ImageView) v.findViewById(R.id.newsImageView);
        TextView numOfViewsTV = (TextView) v.findViewById(R.id.numOfViewsTV);
        TextView likesTV = (TextView) v.findViewById(R.id.likesTV);
        TextView postDateTV = (TextView) v.findViewById(R.id.postDateTV);

        newsTitleTV.setText(newsDetails.get("NewsTitle"));
        numOfViewsTV.setText(newsDetails.get("NumofViews") + "views");
        likesTV.setText("Likes(" + newsDetails.get("Likes") + ")");
        postDateTV.setText(newsDetails.get("PostDate"));

        Picasso.with(context).load(newsDetails.get("ImageUrl")).transform(new CircleTransform()).into(newsIV);


        return v;
    }

}