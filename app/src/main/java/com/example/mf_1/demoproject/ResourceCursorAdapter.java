package com.example.mf_1.demoproject;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by MF-1 on 7/21/2017.
 */

public class ResourceCursorAdapter extends CursorAdapter {

    private RequestManager glide;

    public ResourceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        glide = Glide.with(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resource_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView resImage = (ImageView) view.findViewById(R.id.res_image);
        TextView resName = (TextView) view.findViewById(R.id.res_name);
        TextView resPrice = (TextView) view.findViewById(R.id.res_price);

        resName.setText(cursor.getString(cursor.getColumnIndex(Constants.RESOURCE_NAME)));
        resPrice.setText("Starting from \u20B9 "+cursor.getInt(cursor.getColumnIndex(Constants.RESOURCE_MIN_PRICE)));

        glide.load(Constants.IMAGE_URL + cursor.getString(cursor.getColumnIndex(Constants.RESOURCE_URL)))
                .error(R.color.colorGrey)
        .crossFade().into(resImage);

        //Log.d("IMG_URL", Constants.IMAGE_URL +  cursor.getString(cursor.getColumnIndex(Constants.RESOURCE_URL)));

    }
}
