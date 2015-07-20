/**
 * 
 */
package com.example.shutterstock.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.example.shutterstock.GridViewAdapter;
import com.example.shutterstock.MainActivity;
import com.example.shutterstock.R;
import com.example.shutterstock.ResponseModel;
import com.example.shutterstock.ResponseModel.Data;
import com.example.shutterstock.ResponseModel.Data.Assets;
import com.example.shutterstock.ResponseModel.Data.Assets.LargeThumb;

/**
 * Test file for MainActivity.java, GridViewAdapter.java & ResponseModel.java
 * 
 * @author VijayK
 * 
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    // Activity
    private MainActivity mActivity;

    // GridView
    private View mGridView;

    // NextButton
    private View mNextButton;

    // ProgessBar
    private View mProgessBar;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mGridView = mActivity.findViewById(R.id.grid_view);
        mNextButton = mActivity.findViewById(R.id.next_btn);
        mProgessBar = mActivity.findViewById(R.id.progress_bar);
    }

    /**
     * Test 1 --> Preconditions. UI should be proper
     */
    public void testPreconditions() {
        assertNotNull("GridView is null", mGridView);
        assertNotNull("Next button is null", mNextButton);
        assertNotNull("Progess bar is null", mProgessBar);
    }

    /**
     * Test 2 --> Loading images
     */
    public void testImageLoading() {
        mActivity.makeNetworkRequest();
        assertEquals(View.VISIBLE, mProgessBar.getVisibility());
        try {
            getActivity().getConnection().get();
        } catch (InterruptedException e) {
            fail("Network operation interrupted.");
        } catch (ExecutionException e) {
            fail("Network operation interrupted.");
        }
        assertEquals(View.GONE, mProgessBar.getVisibility());
    }

    /**
     * Test 3 --> Loading images when clicking on next button
     */
    public void testImageLoadingOnNext() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNextButton.performClick();
            }
        });
        assertEquals(View.VISIBLE, mProgessBar.getVisibility());
        try {
            getActivity().getConnection().get();
        } catch (InterruptedException e) {
            fail("Network operation interrupted.");
        } catch (ExecutionException e) {
            fail("Network operation interrupted.");
        }
        assertEquals(View.GONE, mProgessBar.getVisibility());
    }

    /**
     * Test 4 --> Loading images when clicking on next button -- Multiple page loading
     */
    public void testMultiplePagesLoading() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNextButton.performClick();
            }
        });
        waitForWhile();
        assertEquals(View.VISIBLE, mProgessBar.getVisibility());
        try {
            mActivity.getConnection().get();
        } catch (Exception e) {
            fail("Network operation interrupted.");
        }
        assertEquals(View.GONE, mProgessBar.getVisibility());
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNextButton.performClick();
            }
        });
        waitForWhile();
        assertEquals(View.VISIBLE, mProgessBar.getVisibility());
        try {
            mActivity.getConnection().get();
        } catch (Exception e) {
            fail("Network operation interrupted.");
        }
        assertEquals(View.GONE, mProgessBar.getVisibility());
    }

    /**
     * Test 5 --> Rotate device while network operation.
     */
    public void testOrientationOnRequest() {
        mActivity.makeNetworkRequest();
        assertEquals(View.VISIBLE, mProgessBar.getVisibility());
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        assertEquals(View.VISIBLE, mProgessBar.getVisibility());
    }

    /**
     * Test 6 -- Check adapter and model
     */
    public void testAdapterModel() {

        // fill the model according to the response
        List<ResponseModel.Data> listData = new ArrayList<ResponseModel.Data>();
        Data data = new Data();
        Assets assets = new Assets();
        LargeThumb thumb = new LargeThumb();
        thumb.setUrl(NetworkConnectionTest.CORRECT_URL);
        assets.setLargeThumb(thumb);
        data.setAssets(assets);
        listData.add(data);

        GridViewAdapter adapter = new GridViewAdapter(mActivity, listData);

        assertEquals(1, adapter.getCount());
        assertNotNull(adapter.getItemId(0));
    }

    /**
     * wait
     */
    private void waitForWhile() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // do nothing
        }

    }

}
