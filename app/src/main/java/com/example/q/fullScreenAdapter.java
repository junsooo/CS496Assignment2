package com.example.q.myapplication;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by q on 2016-12-28.
 */

public class fullScreenAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    // constructor
    public fullScreenAdapter(Activity activity,
                             ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;

    }

    public static String convertBitmapToString(Bitmap src){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap getBitMapFromString(String src)
    {
        try {
            byte [] encodeByte=Base64.decode(src,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
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
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(fullScreenActivity.activity);

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

            String message="업로드 하시겠습니까?";

            alertDlg.setMessage( message );
            alertDlg.show();
        }

    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);

        Button upload= (Button) viewLayout.findViewById(R.id.buttonUpload);

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


    private class LongOperation extends AsyncTask<String, Void, String> {
        public String getFromURL() {

            HttpURLConnection connection = null;
            try{
                URL url = new URL("http://52.78.108.211:80");
                connection= (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "Application/json");
               OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream(), "UTF-8");


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(Integer.parseInt(position)), options);
               String temp=convertBitmapToString(bitmap);



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
            if(result.equals("fail")){
                Toast.makeText(_activity.getBaseContext(), "Upload failed", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(_activity.getBaseContext(), "Upload successful:"+result, Toast.LENGTH_SHORT).show();
            }
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

}
