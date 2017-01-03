package com.example.q.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by q on 2016-12-27.
 */

public class photoFrag extends AppCompatActivity {

    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private gridViewAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    View view;
    static Activity activity;
    @Override
    public void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);

        setContentView(R.layout.activity_grid);
        activity=this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager()) !=null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });



        gridView = (GridView) findViewById(R.id.grid_view3);
        utils = new Utils(this);

        // Initilizing Grid View
        InitilizeGridLayout();

        // loading all image paths from SD card
        imagePaths = utils.getFilePaths();
        // Gridview adapter
        adapter = new gridViewAdapter(this, imagePaths,
                columnWidth);

        // setting grid view adapter
        gridView.setAdapter(adapter);


    }

    static Bitmap imageBitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");


            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

            alertDlg.setPositiveButton( "예", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which ) {

                    new LongOperation().execute("");


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


                String temp=fullScreenAdapter.convertBitmapToString(imageBitmap);
                System.out.println(temp.length());
                JSONObject json=new JSONObject().put("type", temp);

                //String data="{\"type\":\""+temp+"\"}";
                String data=json.toString();
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
                Toast.makeText(activity.getBaseContext(), "Upload failed", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity.getBaseContext(), "Upload successful:"+result, Toast.LENGTH_SHORT).show();
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











    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        Log.d("MKpadding:", padding+"");

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);
        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                230);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

}
