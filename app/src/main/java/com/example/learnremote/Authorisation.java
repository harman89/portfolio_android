package com.example.learnremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Authorisation extends AppCompatActivity {
    private RequestQueue requestQueue;
    private EditText login, pass;
    SharedPreferences lecture_Settings, auto_auth;
    public static final String APP_PREFERENCES_LECTURE = "lecture_string";

    public interface VolleyCallBack {
        void onSuccess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, String> data= new HashMap<String, String>();
        int counter=0;
        auto_auth = getSharedPreferences("log", MODE_PRIVATE);
        if(auto_auth.contains("log")) {
            data.put("username", auto_auth.getString("log", ""));
            counter++;
        }
        auto_auth = getSharedPreferences("pass", MODE_PRIVATE);
        if(auto_auth.contains("pass")) {
            data.put("password", auto_auth.getString("pass", ""));
            counter++;
        }
        setContentView(R.layout.activity_main);
        if(counter==2)
        {
            JSONObject json = new JSONObject(data);
            Submit(json);
        }
    }
    public void onRegisterClick(View view)
    {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
    public void onExitClick(View view)
    {
        auto_auth = getSharedPreferences("log", MODE_PRIVATE);
        auto_auth.edit().clear().apply();
        auto_auth = getSharedPreferences("pass", MODE_PRIVATE);
        auto_auth.edit().clear().apply();
        Intent intent = new Intent(this, Authorisation.class);
        startActivity(intent);
    }
    public void debugClick(View view) throws JSONException {
        login = (EditText) findViewById(R.id.login_field);
        pass = (EditText) findViewById(R.id.password_field);
        Map<String, String> data= new HashMap<String, String>();
        data.put("username",login.getText().toString());
        data.put("password",pass.getText().toString());
        JSONObject json = new JSONObject(data);
        Submit(json);
    }
    public boolean getLectures(JSONObject data,final VolleyCallBack callBack) throws JSONException {
        String URL=getResources().getString(R.string.url)+"/get_lectures";
        String id = data.getString("user_id");
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(id);
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST, URL,jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                lecture_Settings = getSharedPreferences(APP_PREFERENCES_LECTURE,MODE_PRIVATE);
                SharedPreferences.Editor editor = lecture_Settings.edit();
                editor.putString(APP_PREFERENCES_LECTURE,response.toString());
                editor.apply();
                callBack.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage()+" not responded", Toast.LENGTH_SHORT).show();
                Log.d("json",error.getMessage());
                //Log.v("VOLLEY", error.toString());
            }
        });
        requestQueue.add(jsonRequest);
        return true;
    }
    private void Submit(JSONObject data)
    {
        final JSONObject savedata= data;
        String URL=getResources().getString(R.string.url)+"/login_student";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL,savedata, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                    if(response.getString("message").equals("Welcome"))
                    {
                        Map<String, String> forlectures= new HashMap<String, String>();
                        forlectures.put("user_id",response.getString("user_id"));
                        JSONObject jsonForLectures = new JSONObject(forlectures);

                        auto_auth = getSharedPreferences("log", MODE_PRIVATE);
                        SharedPreferences.Editor editor = auto_auth.edit();
                        editor.putString("log", savedata.getString("username")).apply();

                        auto_auth = getSharedPreferences("pass", MODE_PRIVATE);
                        SharedPreferences.Editor passeditor = auto_auth.edit();
                        passeditor.putString("pass", savedata.getString("password")).apply();

                        lecture_Settings = getSharedPreferences(APP_PREFERENCES_LECTURE,MODE_PRIVATE);
                        if(lecture_Settings.contains(APP_PREFERENCES_LECTURE))
                        {
                            lecture_Settings.edit().clear().apply();
                        }
                        getLectures(jsonForLectures, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(getApplicationContext(), SideMenu.class);
                                try {
                                    intent.putExtra("user_id", response.getString("user_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage()+" not responded", Toast.LENGTH_SHORT).show();

                //Log.v("VOLLEY", error.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }
}
