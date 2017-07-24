package com.example.mf_1.demoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int LIST_ID = 0;
    private ResourceCursorAdapter adapter;
    private ListView resourceList;
    private ResourceBroadCastReceiver resourceBroadcast;
    private ProgressBar progressBar;
    private Cursor cursor;
    private ResourceLoader loader;
    private View footerView;
    private int rowCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mutterfly Resources");

        resourceList = (ListView) findViewById(R.id.resource_list);

        DatabaseAccess dbAccess = DatabaseAccess.getInstance(this);
        dbAccess.open();
        rowCount = dbAccess.getRowCount(Constants.TABLE_RESOURCES);
        dbAccess.close();

        if (rowCount != 0) {
            footerView = getLayoutInflater().inflate(R.layout.footer_view, resourceList, false);
            resourceList.addFooterView(footerView);
        }
        startService();
        getSupportLoaderManager().initLoader(LIST_ID, null, this);

        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        resourceBroadcast = new ResourceBroadCastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(resourceBroadcast, intentFilter);

    }

    private void startService() {
        showProgressbar();
        Intent intent = new Intent(this, GetResourceBackgroundService.class);
        startService(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        loader = new ResourceLoader(getBaseContext());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private class ResourceBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            cursor = loader.loadInBackground();
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.changeCursor(loader.loadInBackground());

                }
            });

            if (cursor != null) {

                adapter = new ResourceCursorAdapter(getBaseContext(), cursor, 0);
                resourceList.setAdapter(adapter);
                resourceList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {}

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if(rowCount == (firstVisibleItem+visibleItemCount-1)){
                            resourceList.removeFooterView(footerView);
                        }
                    }
                });



            }

            hideProgressbar();
        }
    }

    private void showProgressbar() {
        if (progressBar == null) {
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void hideProgressbar() {

        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,1,"Add").setIcon(android.R.drawable.ic_input_add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == 0){

            DatabaseAccess db = DatabaseAccess.getInstance(this);
            db.open();
            db.addResoucesToResourceTable(new ResourceModel());
            adapter.changeCursor(cursor);
            db.close();

        }

        return true;
    }*/
}





