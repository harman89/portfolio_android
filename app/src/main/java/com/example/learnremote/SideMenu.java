package com.example.learnremote;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SideMenu extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView nameSurname, groupName;
    private RequestQueue requestQueue;
    private View navHeaderView;
    public static final String APP_PREFERENCES_TEST = "test_string";
    private String testString,downloadString;
    SharedPreferences test_Settings, auto_auth;
    String[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);
        Bundle extras = getIntent().getExtras();
        String user_id;
        if(extras == null) {
            user_id= null;
        } else {
            user_id= extras.getString("user_id");
            SharedPreferences user_pref = getSharedPreferences("user_id", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = user_pref.edit();
            editor.putInt("user_id", Integer.parseInt(user_id));
            editor.apply();

        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_lections, R.id.nav_tasks, R.id.nav_notifications,R.id.nav_translate)
                .setDrawerLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        navHeaderView= navigationView.getHeaderView(0);
        nameSurname = navHeaderView.findViewById(R.id.nameSurname);
        groupName = navHeaderView.findViewById(R.id.groupName);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        try {
            getContentFromServer(user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void onExitClick(MenuItem item)
    {
        auto_auth = getSharedPreferences("log", MODE_PRIVATE);
        auto_auth.edit().clear().apply();
        auto_auth = getSharedPreferences("pass", MODE_PRIVATE);
        auto_auth.edit().clear().apply();
        Intent intent = new Intent(this, Authorisation.class);
        startActivity(intent);
        finish();
    }
    public void getContentFromServer(String user_id) throws JSONException {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> data= new HashMap<>();
        test_Settings = getSharedPreferences(APP_PREFERENCES_TEST,MODE_PRIVATE);
        if(test_Settings.contains(APP_PREFERENCES_TEST))
        {
            test_Settings.edit().clear().apply();
        }
        data.put("user_id",user_id);
        JSONObject json = new JSONObject(data);
        getNameAndGroup(json);
        getTests(user_id);
    }
    public void getNameAndGroup(JSONObject data)
    {
        String URL=getResources().getString(R.string.url)+"/get_user_info";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL,data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("message").equals("success"))
                    {
                        nameSurname.setText(response.getString("name")+" "+response.getString("surname"));
                        groupName.setText(response.getString("group_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage()+" not responded", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonRequest);
    }
    public void getTests(String data)
    {
        String URL=getResources().getString(R.string.url)+"/get_tests"+data;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("json","before");
                Log.d("json",response);
                Log.d("json","after");
                test_Settings = getSharedPreferences(APP_PREFERENCES_TEST,MODE_PRIVATE);
                SharedPreferences.Editor editor = test_Settings.edit();
                editor.putString(APP_PREFERENCES_TEST,response);
                editor.apply();
                try {
                    downloadTests();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage()+" not responded", Toast.LENGTH_SHORT).show();
                Log.d("json", Objects.requireNonNull(error.getMessage()));
            }
        });
        requestQueue.add(stringRequest);
    }

    public void downloadTests() throws FileNotFoundException {
        test_Settings = getSharedPreferences(APP_PREFERENCES_TEST, Context.MODE_PRIVATE);
        testString = test_Settings.getString(APP_PREFERENCES_TEST,"");
        array = testString.split(",");
        DownloadManager downloadmanager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        for (String s : array) {
            final File file = new File(getExternalFilesDir(null), s + ".xml");
            if (file.exists()) {
                file.delete();
            }
            Uri uri = Uri.parse(getResources().getString(R.string.url) + "/get_test" + s);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(s + ".xml");
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalFilesDir(this.getApplicationContext(), null, s + ".xml");
            assert downloadmanager != null;
            downloadmanager.enqueue(request);
        }
    }
}