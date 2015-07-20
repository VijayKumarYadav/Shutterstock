/**
 * 
 */
package com.example.shutterstock;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.shutterstock.ResponseModel.Data;
import com.google.gson.Gson;

/**
 * Class to setup connection in background using AsyncTack
 * 
 * @author VijayK
 * 
 *         NetworkConnection.java
 */
public class NetworkConnection extends AsyncTask<Object, String, List<Data>> {

    /**
     * Constructor to perform n/w operation
     * 
     * @param url
     *        URL to connect
     * 
     */
    public NetworkConnection() {

    }

    /**
     * Excecute after all network operation
     */
    @Override
    synchronized protected void onPostExecute(final List<Data> result) {
        try {
            completionListener.networkOperationComplete(result);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private HttpURLConnection httpURLConnection;

    private String serverRes = null;

    @Override
    protected void onProgressUpdate(final String... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Create n/w operation in background
     */
    @Override
    protected List<Data> doInBackground(final Object... params) {

        if (!isCancelled()) {
            String url = (String) params[0];
            ByteArrayOutputStream byteArrayOutputStream = null;
            DataInputStream dataInputStream = null;
            try {
                InputStream inputStream = null;
                URL urlToConnect = new URL(url);
                httpURLConnection = (HttpURLConnection) urlToConnect.openConnection();
                // do basic Authorization
                httpURLConnection.setRequestProperty("Authorization", "Basic " + getEncodedString());
                httpURLConnection.setRequestProperty("user-agent", "Android");
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(1000 * 1);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    dataInputStream = new DataInputStream(inputStream);
                    int a = -1;
                    byte b[] = new byte[1024];
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    while ((a = dataInputStream.read(b)) != -1) {
                        byteArrayOutputStream.write(b, 0, a);
                    }
                    serverRes = new String(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.close();
                    dataInputStream.close();
                    httpURLConnection.disconnect();
                } else {
                    httpURLConnection.disconnect();

                }

            } catch (Exception e) {
                System.err.println(e);
            } finally {
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                    }
                    byteArrayOutputStream = null;
                }
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                    }
                    dataInputStream = null;
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                    httpURLConnection = null;
                }
            }
        }
        return parseResponse();
    }

    /**
     * Parse the server response
     * 
     * @return List<Data> parsed response
     * 
     */
    private List<Data> parseResponse() {
        if (serverRes != null && serverRes.length() != 0) {
            try {
                final ResponseModel parseForgetPassResponse = new Gson().fromJson(serverRes, ResponseModel.class);
                return parseForgetPassResponse.getData();
            } catch (Exception e) {
                // no need to do anything
            }
        }
        return null;
    }

    /**
     * @return
     */
    private String getEncodedString() {
        String credit = "165d125626071c5389ad:a7f454c4c27ae1f9fe129418c85abeb3bbbe858d";
        return Base64.encodeToString(credit.getBytes(), Base64.NO_WRAP);
    }

    /** The completion listener. */
    private OnCompletionListener completionListener;

    public interface OnCompletionListener {

        public void networkOperationComplete(List<Data> result);
    }

    public void setListener(final OnCompletionListener listener) {
        this.completionListener = listener;
    }

}