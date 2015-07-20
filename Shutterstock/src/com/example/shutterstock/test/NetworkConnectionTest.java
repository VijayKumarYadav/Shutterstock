/**
 * 
 */
package com.example.shutterstock.test;

import java.util.List;

import android.test.InstrumentationTestCase;

import com.example.shutterstock.NetworkConnection;
import com.example.shutterstock.NetworkConnection.OnCompletionListener;
import com.example.shutterstock.ResponseModel.Data;

/**
 * @author vyrock
 * 
 */
public class NetworkConnectionTest extends InstrumentationTestCase {

    public static final String CORRECT_URL = "https://api.shutterstock.com/v2/images/search?license=commercial&&&page=1&per_page=1&&&&&&&&sort=popular&view=minimal";

    public static final String INCORRECT_URL_FOR_DATA = "http://www.google.com";

    /**
     * Test 1 -- connection check on correct url
     */
    public void testConnectionOnCorrectUrl() {
        NetworkConnection connection = new NetworkConnection();
        connection.execute(CORRECT_URL);
        connection.setListener(new OnCompletionListener() {

            @Override
            public void networkOperationComplete(final List<Data> result) {
                assertNotNull(result);
                assertTrue(result.size() > 0);
            }
        });
    }

    /**
     * Test 2 -- connection check on incorrect url for data
     */
    public void testConnectionOnIncorrectUrl() {
        NetworkConnection connection = new NetworkConnection();
        connection.execute(INCORRECT_URL_FOR_DATA);
        connection.setListener(new OnCompletionListener() {

            @Override
            public void networkOperationComplete(final List<Data> result) {
                assertNull(result);
            }
        });
    }

    /**
     * Test 3 --- Test for parse error
     */
    public void testForParseError() {
        NetworkConnection connection = new NetworkConnection();
        connection.execute(CORRECT_URL);
        connection.setListener(new OnCompletionListener() {

            @Override
            public void networkOperationComplete(final List<Data> result) {
                assertNotNull(result);
                assertTrue(result.size() > 0);
                assertNotNull(result.get(0).getAssets().getLargeThumb());
            }
        });
    }

}
