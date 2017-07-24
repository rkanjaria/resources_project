package com.example.mf_1.demoproject;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by MF-1 on 7/20/2017.
 */

public class GetResourceBackgroundService extends IntentService {

    private final String END_POINT = "general/get_resources";

    //"https://api.mutterfly.in/rest/general/get_resources"

    public GetResourceBackgroundService() {
        super("Empty Contructor");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            parseResponseAndInsertToDatabase(getDataFromURL(Constants.SERVER_URL + END_POINT));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getDataFromURL(String serverURL) throws IOException {

        HttpURLConnection httpURLConnection = null;
        InputStream ins = null;
        String data = "";

        try {

            URL url = new URL(serverURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            ins = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            bufferedReader.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {

            ins.close();
            httpURLConnection.disconnect();
        }
     return data;
    }


    private void parseResponseAndInsertToDatabase(String responseString){

        Log.d("RESPONSE", responseString);

        DatabaseAccess dbAccess = DatabaseAccess.getInstance(this);
        dbAccess.open();
        dbAccess.emptyTableIfNotEmpty(Constants.TABLE_RESOURCES);

        try{

            JSONObject object = new JSONObject(responseString);
            if(object.getString("resource_error").equalsIgnoreCase("false")){

                JSONArray resourceArray = object.getJSONArray("resource_data");

                for(int i = 0 ; i< resourceArray.length() ; i++){

                    ResourceModel resourceModel = new ResourceModel();

                    JSONObject resourceObject = resourceArray.getJSONObject(i);
                    resourceModel.setResourceId(resourceObject.getInt("resources_id"));
                    resourceModel.setCategory(resourceObject.getInt("resources_category"));
                    resourceModel.setDescription(resourceObject.optString("resources_desc"));
                    resourceModel.setResName(resourceObject.getString("resources_name"));
                    resourceModel.setImageUrl(resourceObject.getString("resources_image"));
                    resourceModel.setPrice(resourceObject.getInt("starting_from"));
                    resourceModel.setAvgPrice(resourceObject.getInt("avg_weekly"));
                    resourceModel.setMinPrice(resourceObject.getInt("starting_from"));

                    dbAccess.addResoucesToResourceTable(resourceModel);

                }
                Log.d("RESULT", "Data inserted");

                Intent broadcastIntent = new Intent(Constants.BROADCAST_ACTION);
                broadcastIntent.putExtra(Constants.BROADCAST_RESULT, responseString);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dbAccess.close();
        }}

}
