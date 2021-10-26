package com.example.learnremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FinishActivity extends AppCompatActivity {
    String resultString;
    int part_id_to_send,user_id_to_send;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        TextView resultText = (TextView)findViewById(R.id.resultText);
        Button back = (Button)findViewById(R.id.buttonBack);
        Bundle arguments =getIntent().getExtras();
        SharedPreferences part_id = getSharedPreferences("part_id", Context.MODE_PRIVATE);
        SharedPreferences user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        part_id_to_send=part_id.getInt("part_id",0);
        user_id_to_send = user_id.getInt("user_id",0);
        if(arguments!=null)
        {
            int result = arguments.getInt("result");
            int out_of = arguments.getInt("outOf");
            int percentage = result*100/out_of;
            int mark;
            if(percentage>=85) mark=5;
            else if (percentage >= 61) mark =4;
            else if (percentage >= 55) mark = 3;
            else mark = 2;
            resultString = "Your result is "+result+"/"+out_of+" ("+mark+")";
            resultText.setText(resultString);
            Map<String, String> data= new HashMap<String, String>();
            data.put("user_id",user_id_to_send+"");
            data.put("part_id",part_id_to_send+"");
            data.put("result",result+"/"+out_of+" ("+mark+")");
            JSONObject json = new JSONObject(data);
            submit(json);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
    private void submit(JSONObject data)
    {
        final JSONObject savedata= data;
        String URL=getResources().getString(R.string.url)+"/recieve_result";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, URL, savedata, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonrequest);
    }
}