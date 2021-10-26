package com.example.learnremote.ui.gallery;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.example.learnremote.LectionsClass;
import com.example.learnremote.PartAdapter;
import com.example.learnremote.R;
import com.example.learnremote.Test;
import com.example.learnremote.TestActivity;
import com.example.learnremote.TestAdapter;
import com.example.learnremote.XmlParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class TaskFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private final ArrayList<Test> tests=new ArrayList<>();
    SharedPreferences test_Settings;
    public static final String APP_PREFERENCES_TEST = "test_string";
    String[] array;
    private String testString,downloadString;
    public TaskFragment()
    {

    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taskViewModel =
                new ViewModelProvider(this).get(TaskViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        XmlParser parser = new XmlParser();
        test_Settings = this.getContext().getSharedPreferences(APP_PREFERENCES_TEST, Context.MODE_PRIVATE);
        testString = test_Settings.getString(APP_PREFERENCES_TEST,"");
        array = testString.split(",");
        for(int i = 0;i<array.length;i++)
        {
            File file = new File(this.getContext().getExternalFilesDir(null),array[i]+".xml");
            try {
                FileInputStream fis = new FileInputStream(file);
                parser.parse(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        tests.addAll(parser.getTests());
        RecyclerView recyclerViewTests = root.findViewById(R.id.listoftests);
        TestAdapter adapter = new TestAdapter(root.getContext(), tests);
        recyclerViewTests.setAdapter(adapter);
        return root;
    }
}