package com.example.q.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class upload extends Fragment{
    View view;
    GridView gridView;
    ArrayList<image> image_array;

    private gridViewAdapterBitmap adapter;
    private int columnWidth;

    public class image{
        String name;
        Bitmap bitmap;
        public image(String name, Bitmap bitmap){
            this.name=name;
            this.bitmap=bitmap;
        }
    }
    Button button2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.web_activity, container, false);
        Button button = (Button) view.findViewById(R.id.button);
        gridView = (GridView) view.findViewById(R.id.gridview);

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), photoFrag.class);
                startActivity(myIntent);


            }
        });
        button2= (Button) view.findViewById(R.id.load);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new LongOperation().execute("");
            }
        });

        return view;
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        Log.d("MKpadding:", padding+"");

        int screenWidth;
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        screenWidth = point.x;
        columnWidth = (int) ((screenWidth - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);
        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                230);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        public String getFromURL() {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://52.78.108.211:80");
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
                }
                rd.close();
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
            try {
                JSONObject json = new JSONObject(result.trim());
                Iterator<?> keys = json.keys();
                while(keys.hasNext()){
                    String key = (String)keys.next();
                    String value= (String)json.get(key);
                    Bitmap bitmap= fullScreenAdapter.getBitMapFromString(value);
                    image_array.add(new image(key, bitmap));
                }

            }catch(Exception e){
                e.printStackTrace();
            }


            // Initilizing Grid View
            InitilizeGridLayout();


            // Gridview adapter
            adapter = new gridViewAdapterBitmap(getActivity(), image_array,
                    columnWidth);

            // setting grid view adapter
            gridView.setAdapter(adapter);
            /*      Bitmap bitmap = fullScreenAdapter.getBitMapFromString(result);
            ImageView image= (ImageView) view.findViewById(R.id.imageView2);
            image.setImageBitmap(bitmap);

*/
            button2.setEnabled(true);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
            button2.setEnabled(false);
            image_array=new ArrayList<image>();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }







}