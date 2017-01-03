package com.example.q.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by q on 2017-01-02.
 */

public class fullScreenAdapter2 extends PagerAdapter {

    private Activity _activity;
    private LayoutInflater inflater;
    private ArrayList<upload.image> _filePaths;
    // constructor
    public fullScreenAdapter2(Activity activity) {
        this._activity = activity;
        _filePaths=gridViewAdapterBitmap._filePaths;
    }


    @Override
    public int getCount() {
        return _filePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    public class MyLovelyOnClickListener implements View.OnClickListener
    {

        int myLovelyVariable;
        public MyLovelyOnClickListener(int myLovelyVariable) {
            this.myLovelyVariable = myLovelyVariable;
        }

        @Override
        public void onClick(View v)
        {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(fullScreenActivity2.activity);

            alertDlg.setPositiveButton( "예", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    new LongOperation().execute(new Integer(myLovelyVariable).toString());
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });
            alertDlg.setNegativeButton( "아니오", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            String message="이 사진을 겔러리에서 지우시겠습니까?";

            alertDlg.setMessage( message );
            alertDlg.show();
        }

    }
    private class LongOperation extends AsyncTask<String, Void, String> {


        public String getFromURL() {
            HttpURLConnection connection = null;
            try{
                URL url = new URL("http://52.78.108.211:80");
                connection= (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type", "Application/json");
                OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream(), "UTF-8");


                String temp=_filePaths.get(Integer.parseInt(position)).name;



                JSONObject json = new JSONObject().put("type", temp);
                String data=json.toString();
                System.out.println(data);
                System.out.println(data.length());

                writer.write(data);
                writer.flush();
                //         writer.close();


                BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer(); // or StringBuffer if Java version 5+
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.e("Main Activity", line);
                    response.append(line);
                }
                reader.close();
                return response.toString();

            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return "fail";
        }

        String position;

        @Override
        protected String doInBackground(String... params) {
            position=params[0];
            String returnedData = getFromURL();


            return returnedData;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(_activity.getBaseContext(), result, Toast.LENGTH_SHORT).show();

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
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) _activity
                .getSystemService(_activity.getApplicationContext().LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);

        Bitmap bitmap = _filePaths.get(position).bitmap;
        imgDisplay.setImageBitmap(bitmap);

        Button upload= (Button) viewLayout.findViewById(R.id.buttonUpload);
        upload.setText("Delete");
        upload.setOnClickListener(new MyLovelyOnClickListener(position));
        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }


}
