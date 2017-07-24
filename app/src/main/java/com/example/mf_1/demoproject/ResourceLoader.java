package com.example.mf_1.demoproject;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by MF-1 on 7/21/2017.
 */

public class ResourceLoader extends AsyncTaskLoader<Cursor> {

    private Cursor cursor;
    private int index;

    public ResourceLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {

        index += 10;

        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getContext());
        dbAccess.open();
        cursor = dbAccess.getResourcesFromTable(index);

        Log.d("LOADER_CLASS", "Loading in background thread");

        return cursor;

    }

    @Override
    public void deliverResult(Cursor data) {

        if(isReset()){
            if(cursor != null){
                cursor.close();
            }
            return;
        }

        Cursor oldCursor = cursor;
        cursor = data;

        if(isStarted()){
            super.deliverResult(data);
        }

        if(oldCursor != null && oldCursor != data && !oldCursor.isClosed()){
            oldCursor.close();
        }
    }


}
