package com.example.shutterstock;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shutterstock.NetworkConnection.OnCompletionListener;
import com.example.shutterstock.ResponseModel.Data;
import com.squareup.picasso.Picasso;

/**
 * Main Screen - Showing grid of images
 * 
 * @author VijayK
 * 
 */
public class MainActivity extends FragmentActivity implements OnCompletionListener, OnScrollListener, OnClickListener {

    // per page count
    private static int PER_PAGE = 60;

    // page number
    private int mPageNumber = 0;

    // grid view
    private GridView mGridView;

    // progressBar
    private ProgressBar mProgessBar;

    // next button
    private Button mNextBtn;

    // response data
    private final List<Data> mData = new ArrayList<ResponseModel.Data>();

    // connection
    private NetworkConnection mConnection;

    // GridViewAdapter
    private GridViewAdapter mAdapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNextBtn = (Button) findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setOnScrollListener(this);
        // initialize the adapter
        mAdapter = new GridViewAdapter(this, mData);
        mGridView.setAdapter(mAdapter);
        mProgessBar = (ProgressBar) findViewById(R.id.progress_bar);
        makeNetworkRequest();
    }

    /**
     * Method to make a network request
     */
    public void makeNetworkRequest() {
        if (isNetworkAvailable()) {
            mConnection = new NetworkConnection();
            mConnection.execute(getUrl(++mPageNumber));
            mConnection.setListener(this);
        } else {
            mProgessBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get active connection
     * 
     * @return NetworkConnection
     */
    public NetworkConnection getConnection() {
        return mConnection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.shutterstock.CreateConnection.OnCompletionListener#sendOperationComplete
     */
    @Override
    public void networkOperationComplete(final List<Data> data) {
        if (data != null) {
            mProgessBar.setVisibility(View.GONE);
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
        } else {
            mProgessBar.setVisibility(View.GONE);
            Toast.makeText(this, "Oops..Something went wrong..Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.with(this).cancelTag(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
     */
    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
            final int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount != 0) {
            mNextBtn.setVisibility(View.VISIBLE);
        } else {
            mNextBtn.setVisibility(View.GONE);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
     */
    @Override
    public void onScrollStateChanged(final AbsListView arg0, final int arg1) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(final View arg0) {
        mProgessBar.setVisibility(View.VISIBLE);
        makeNetworkRequest();
    }

    /**
     * get url based on page number
     * 
     * @return url
     */
    private String getUrl(final int pageNumber) {
        StringBuilder builder = new StringBuilder(
                "https://api.shutterstock.com/v2/images/search?license=commercial&&&page=");
        builder.append(pageNumber);
        builder.append("&per_page=");
        builder.append(PER_PAGE);
        builder.append("&&&&&&&&sort=popular&view=minimal");
        return builder.toString();
    }

    /**
     * Check if the netwrok is availbale or not
     * 
     * @param context
     *        current context using this methos
     * @return true if Network available
     */
    public boolean isNetworkAvailable() {
        boolean isNetworkAvailable = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            // test for connection
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnected()) {
                isNetworkAvailable = true;
            }
        } catch (Exception e) {
            // do nothing
        }
        return isNetworkAvailable;
    }
}
