package com.example.mf_1.demoproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by MF-1 on 7/20/2017.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "mutten6m_andro.db";
    private static final int DATABASE_VERSION = 14;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, null, DATABASE_VERSION);
    }

    /*@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        if(oldVersion < newVersion){
            db.execSQL("ALTER TABLE "+Constants.TABLE_RESOURCES + " ADD COLUMN "+Constants.ID+ "INTEGER PRIMARY KEY;");
            Log.d("UPGRADE", Constants.TABLE_RESOURCES +" updated");
        }



    }*/
}
