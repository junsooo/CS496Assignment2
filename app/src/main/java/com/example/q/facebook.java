package com.example.q.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static android.R.attr.id;
import static com.example.q.myapplication.MyApplication.callbackManager;
import static com.example.q.myapplication.MyApplication.query_string;
import static com.facebook.FacebookSdk.getApplicationContext;

public class facebook extends Fragment{
    private static final int CODE_READ = 42;

    public Button btnLoadContacts;
    public Button btnLoadJSON;
    public ListView contactListView;
    //private static final String TAG = "MainActivity";
    JSONObject json = new JSONObject();
    JSONArray arrJson = new JSONArray();
    JSONObject arrJson1 = new JSONObject();
    public ArrayList<HashMap<String, String>> contactList;
    int check=0;

    View view;
    GridView gridView;

    private gridViewAdapterBitmap adapter;
    private int columnWidth;

    Button button2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        callbackManager = CallbackManager.Factory.create();
        view = inflater.inflate(R.layout.facebook_activity, container, false);
        //btnLoadContacts = (Button) view.findViewById(R.id.loadContact);
        final LoginButton facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        contactListView = (ListView) view.findViewById(R.id.contactList);
        //btnLoadContacts.setOnClickListener(loadContactOnClicked);
        contactList = new ArrayList<>();

        facebookLoginButton.setReadPermissions("user_friends");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                clearAll();
                AccessToken accessToken = loginResult.getAccessToken();
                String user_id = accessToken.getUserId();
                //Log.i(TAG, "User ID: " + loginResult.getAccessToken().getUserId());
                //Log.i(TAG, "Auth Token: " + loginResult.getAccessToken().getToken());
                clearAll();
                LongOperation test1 = new LongOperation("https://graph.facebook.com/v2.8/" + loginResult.getAccessToken().getUserId() + "/" + "taggable_friends?limit=800&access_token=" + loginResult.getAccessToken().getToken());
                String test = null;
                try {
                    test = test1.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println(test);

                try {
                    String readlocationFeed = test;
                    //arrJson = new JSONArray(readlocationFeed);
                    JSONObject jsnobject = new JSONObject(readlocationFeed);
                    JSONArray jsonArray = jsnobject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        String name = explrObject.getString("name");
                        //fb_name_list.add(name);
                        query_string += "b" + "=" + URLEncoder.encode(name) + "&c=emp&";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LongOperation test2 = new LongOperation("http://52.78.108.211:8080?" + query_string);
                try {
                    test = test2.execute().get();
                    System.out.println(test);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                String name = "";
                String mobile = "";
                ArrayList<String> name_list = new ArrayList<String>();
                ArrayList<String> mobile_list = new ArrayList<String>();
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(test.trim());
                    Iterator<?> keys = jObject.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        if (jObject.get(key) instanceof JSONObject) {
                            HashMap<String, String> contact = new HashMap<>();
                            String id = null;
                            name = ((JSONObject) jObject.get(key)).getString("name");
                            mobile = ((JSONObject) jObject.get(key)).getString("mobile");
                            contact.put("id",id);
                            contact.put("name", name);
                            contact.put("mobile", mobile);
                            System.out.println("id: " + id + " / name : " + name + " / mobile : " + mobile);
                            contactList.add(contact);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                displayList();
            }

            @Override
            public void onCancel() {
                // App code
                //Log.w(TAG, "Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                //Log.e(TAG, "Error", exception);
            }
        });
        return view;
    }

    public void readContact(Context context) {
        final MainActivity activity = (MainActivity)getActivity();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //final MainActivity activity = (MainActivity) getActivity();
            int permissionResult = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);

            if (permissionResult == PackageManager.PERMISSION_DENIED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("Need permission").setMessage("To use this function, We need permission for \"READ_CONTACTS\". Continue?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity, "Cancelled the function", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1000);
                }
            } else {
                Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            int id_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                            int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            int mobile_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                            HashMap<String, String> contact = new HashMap<>();
                            String id = cursor.getString(id_idx);
                            String name = cursor.getString(name_idx);
                            String mobile = cursor.getString(mobile_idx);

                            System.out.println("id: " + id + " / name : " + name + " / mobile : " + mobile);
                            contactList.add(contact);
                            query_string += "b" + "=" + URLEncoder.encode(name) + "&c="+URLEncoder.encode(mobile)+"&";

                            /*
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("id", id);
                            contact.put("name", name);
                            contact.put("mobile", mobile);

                            System.out.println("id: " + id + " / name : " + name + " / mobile : " + mobile);
                            contactList.add(contact);
                            */
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                //return contactList;
            }
        }
        else {
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int id_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                        int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        int mobile_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                        HashMap<String, String> contact = new HashMap<>();
                        String id = cursor.getString(id_idx);
                        String name = cursor.getString(name_idx);
                        String mobile = cursor.getString(mobile_idx);

                        System.out.println("id: " + id + " / name : " + name + " / mobile : " + mobile);
                        contactList.add(contact);
                        query_string += "b" + "=" + URLEncoder.encode(name) + "&c="+URLEncoder.encode(mobile)+"&";

                            /*
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("id", id);
                            contact.put("name", name);
                            contact.put("mobile", mobile);

                            System.out.println("id: " + id + " / name : " + name + " / mobile : " + mobile);
                            contactList.add(contact);
                            */
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            //return contactList;
        }
    }
    public void displayList() {
        ListAdapter adapter = new SimpleAdapter(
                getActivity(),
                contactList,
                R.layout.list_item,
                new String[]{"name", "mobile"},
                new int[]{R.id.name, R.id.mobile});
        contactListView.setAdapter(adapter);
    }
    public void clearAll() {
        contactList.clear();
        displayList();
    }

}