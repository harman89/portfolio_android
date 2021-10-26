package com.example.learnremote.ui.home;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnremote.LectionsClass;
import com.example.learnremote.LectionsListAdapter;
import com.example.learnremote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class LectionFragment extends Fragment {

    private LectionsViewModel lectionsViewModel;
    private final ArrayList<LectionsClass> lections = new ArrayList<>();
    SharedPreferences lecture_Settings;
    public static final String APP_PREFERENCES_LECTURE = "lecture_string";
    private String lectionString,downloadString;
    public LectionFragment()
    {

    }
    public void makeLectionsList() throws JSONException {
        lecture_Settings = this.getActivity().getSharedPreferences(APP_PREFERENCES_LECTURE, Context.MODE_PRIVATE);
        lectionString = lecture_Settings.getString(APP_PREFERENCES_LECTURE,"");
        JSONArray jsonArray = new JSONArray(lectionString);
        DownloadManager downloadmanager = (DownloadManager)this.getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        for(int i = 0; i<jsonArray.length();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            lections.add(new LectionsClass(jsonObject.getInt("id"),jsonObject.getString("title"),jsonObject.getString("sub_title"),jsonObject.getString("path_to_file")));
            File file = new File(this.getContext().getExternalFilesDir(null),jsonObject.getString("path_to_file"));
            if (!file.exists()) {
                Uri uri = Uri.parse(getResources().getString(R.string.url)+"/get_lecture" + jsonObject.getInt("id"));
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(jsonObject.getString("path_to_file"));
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                request.setDestinationInExternalFilesDir(this.getContext(), null, jsonObject.getString("path_to_file"));
                downloadmanager.enqueue(request);
            }
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lectionsViewModel =
                new ViewModelProvider(this).get(LectionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lections, container, false);
        try {
            makeLectionsList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerViewLections = root.findViewById(R.id.lectionsRecycler);
        LectionsListAdapter adapter = new LectionsListAdapter(root.getContext(),lections);
        recyclerViewLections.setAdapter(adapter);
        return root;
    }
}