package com.example.learnremote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SharedPreferences pref = getSharedPreferences("testRes", Context.MODE_PRIVATE);
        pref.edit().clear().apply();
        Bundle arguments = getIntent().getExtras();
        Part part=null;
        if(arguments!=null)
        {
            part = (Part) arguments.getParcelable(Part.class.getSimpleName());
        }
        SharedPreferences part_id = getSharedPreferences("part_id",Context.MODE_PRIVATE);
        part_id.edit().clear().apply();
        part_id.edit().putInt("part_id",part.getPart_id()).apply();
        ViewPager2 pager = (ViewPager2)findViewById(R.id.testViewPager);
        pager.setOffscreenPageLimit(part.getQuestions().size());
        FragmentStateAdapter pageAdapter = new AnswerListPageAdapter(this,part.getQuestions(),part.getText());
        pager.setAdapter(pageAdapter);
    }
}