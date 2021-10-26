package com.example.learnremote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText name,surname,mail,username,password,confirm,code;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText)findViewById(R.id.Name);
        surname = (EditText)findViewById(R.id.Surname);
        mail = (EditText)findViewById(R.id.mail);
        username = (EditText)findViewById(R.id.login);
        password = (EditText)findViewById(R.id.pass);
        confirm = (EditText)findViewById(R.id.passConfirm);
        code = (EditText)findViewById(R.id.code);
        btn_submit = (Button) findViewById(R.id.submitRegister);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirm.getText().toString())) {
                    Map<String, String> data= new HashMap<String, String>();
                    data.put("username",username.getText().toString());
                    data.put("name",name.getText().toString());
                    data.put("surname",surname.getText().toString());
                    data.put("mail",mail.getText().toString());
                    data.put("password",password.getText().toString());
                    data.put("code",code.getText().toString());
                    JSONObject json = new JSONObject(data);
                    Submit(json);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Пароли различаются", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Submit(JSONObject data)
    {
        final JSONObject savedata= data;

        String URL=getResources().getString(R.string.url)+"/registration_student";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL,savedata, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                    finish();
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
