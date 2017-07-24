package com.example.mf_1.demoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MF-1 on 7/20/2017.
 */

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }


    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void printLog() {

        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.d("TABLES", "Table Name=> " + cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public int getRowCount(String tableName){

        String count = "SELECT count(*) FROM "+tableName;
        Cursor cursor = database.rawQuery(count, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public void emptyTableIfNotEmpty(String tableName){

        String count = "SELECT count(*) FROM "+tableName;
        Cursor cursor = database.rawQuery(count, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);

        if(rowCount != 0){
            database.execSQL("delete from "+ tableName);
            Log.d("DELETED", rowCount+" number of rows deleted from table "+ tableName );
        }
        cursor.close();

    }


    public void addResoucesToResourceTable(ResourceModel resourceModel) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.RESOURCE_ID, resourceModel.getResourceId());
        contentValues.put(Constants.RESOURCE_CATEGORY, resourceModel.getCategory());
        contentValues.put(Constants.RESOURCE_NAME, resourceModel.getResName());
        contentValues.put(Constants.RESOURCE_DESCRIPTION, resourceModel.getDescription());
        contentValues.put(Constants.RESOURCE_URL, resourceModel.getImageUrl());
        contentValues.put(Constants.RESOURCE_PRICE, resourceModel.getPrice());
        contentValues.put(Constants.RESOURCE_AVG_PRICE, resourceModel.getAvgPrice());
        contentValues.put(Constants.RESOURCE_MIN_PRICE, resourceModel.getMinPrice());

        database.insert(Constants.TABLE_RESOURCES, null, contentValues);
    }

    public Cursor getResourcesFromTable(int limit) {

        List<ResourceModel> modelList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM "+ Constants.TABLE_RESOURCES+" LIMIT "+limit+ ";", null);
        if(cursor.moveToFirst()){

            do{

                ResourceModel model = new ResourceModel();

                model.setResourceId(cursor.getInt(cursor.getColumnIndex(Constants.RESOURCE_ID)));
                model.setCategory(cursor.getInt(cursor.getColumnIndex(Constants.RESOURCE_CATEGORY)));
                model.setResName(cursor.getString(cursor.getColumnIndex(Constants.RESOURCE_NAME)));
                model.setDescription(cursor.getString(cursor.getColumnIndex(Constants.RESOURCE_DESCRIPTION)));
                model.setImageUrl(cursor.getString(cursor.getColumnIndex(Constants.RESOURCE_URL)));
                model.setMinPrice(cursor.getInt(cursor.getColumnIndex(Constants.RESOURCE_MIN_PRICE)));
                model.setAvgPrice(cursor.getInt(cursor.getColumnIndex(Constants.RESOURCE_AVG_PRICE)));
                model.setPrice(cursor.getInt(cursor.getColumnIndex(Constants.RESOURCE_PRICE)));

                modelList.add(model);

            }while (cursor.moveToNext());

        }

        return cursor;
    }


    public void addItemToTable() {



    }
}
