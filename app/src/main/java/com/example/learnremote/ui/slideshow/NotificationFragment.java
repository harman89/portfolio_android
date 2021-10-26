package com.example.learnremote.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.learnremote.NotificationAdapter;
import com.example.learnremote.NotificationTextItem;
import com.example.learnremote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {

    private NotificationViewModel notificationViewModel;
    ArrayList<NotificationTextItem> notificationTextItemArrayList = new ArrayList<>();
    private RequestQueue requestQueue;
    public interface VolleyCallBack {
        void onSuccess();
    }
    public NotificationFragment()
    {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationViewModel =
                new ViewModelProvider(this).get(NotificationViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        SharedPreferences userpref = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        int user_id = userpref.getInt("user_id",0);
        Map<String,String> fornotifications = new HashMap<String, String>();
        fornotifications.put("user_id",user_id+"");
        JSONObject jsonObject = new JSONObject(fornotifications);
        try {
            getNotifications(jsonObject, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    RecyclerView recyclerNotification = root.findViewById(R.id.recyclerNotifications);
                    NotificationAdapter adapter = new NotificationAdapter(root.getContext(), notificationTextItemArrayList);
                    recyclerNotification.setAdapter(adapter);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }
    public void getNotifications(JSONObject data, final VolleyCallBack callBack) throws JSONException {
        String URL=getResources().getString(R.string.url)+"/get_notifications";
        String id = data.getString("user_id");
        Log.d("jsonNahui",id);
        requestQueue = Volley.newRequestQueue(getContext());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(id);
        Log.d("jsonNahui",jsonArray.toString());
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST, URL,jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i =0;i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        notificationTextItemArrayList.add(new NotificationTextItem(jsonObject.getString("date"),jsonObject.getString("text")));
                        callBack.onSuccess();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.getMessage()+" not responded", Toast.LENGTH_SHORT).show();
                Log.d("jsonError",error.getMessage());
                //Log.v("VOLLEY", error.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }
}