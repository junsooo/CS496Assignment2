package com.example.q.myapplication;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by q on 2017-01-02.
 */

public class LongOperation extends AsyncTask<String, Void, String> {
    String str=null;
    LongOperation(String str){
        this.str = str;
    }
    public String getFromURL() {
        HttpURLConnection connection = null;
        try {


            URL url = new URL(str);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //Get Response
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader rd = new BufferedReader(isr);
            StringBuffer response = new StringBuffer(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                Log.e("Main Activity", line);
                response.append(line);
                //response_string=line;
                response.append('\n');
            }
            rd.close();
            MyApplication.response_string = response.toString();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String returnedData = getFromURL();


        return returnedData;
    }

    @Override
    protected void onPostExecute(String result) {


        //tt.setText(result);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}