package com.example.learnremote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Objects;

public class TestPageFragment extends Fragment {
    private final int questionNumber;
    private final Question question;
    private final String testTheme;
    private int partNumber;
    private boolean taskPressedStatus;
    private final String taskText="Task";
    private int times_pressed;
    private SharedPreferences testResultPref;
    public static TestPageFragment newInstance(Question outerQuestion,String theme, int pageNumber)
    {
        return new TestPageFragment(outerQuestion,theme,pageNumber);
    }
    public TestPageFragment(Question question,String theme, int pageNumber){
        this.question =  question;
        this.testTheme=theme;
        this.questionNumber=pageNumber;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        taskPressedStatus=false;
        final View result=inflater.inflate(R.layout.page_fragment, container, false);
        times_pressed=0;
        Button finishButton = result.findViewById(R.id.finishButton);
        TextView textQuestion= result.findViewById(R.id.textQuestion);
        final TextView textTask = result.findViewById(R.id.textTaskTest);
        TextView numberPage = result.findViewById(R.id.pageNumberTest);
        textTask.setText(taskText);
        if(question.getNumber()==questionNumber)
        {
            finishButton.setVisibility(View.VISIBLE);
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(times_pressed==0) {
                        times_pressed++;
                        Toast.makeText(result.getContext(), "Press button again to finish the test", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        times_pressed=0;
                        testResultPref =result.getContext().getSharedPreferences("testRes", Context.MODE_PRIVATE);
                        Intent intent= new Intent(result.getContext(), FinishActivity.class);
                        intent.putExtra("result", testResultPref.getInt("testRes",0));
                        intent.putExtra("outOf",questionNumber);
                        result.getContext().startActivity(intent);
                        testResultPref.edit().clear().apply();
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
            });
        }
        else{finishButton.setVisibility(View.INVISIBLE);}
        View.OnClickListener onTaskClcik = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!taskPressedStatus)
                {
                    textTask.setText(testTheme);
                    taskPressedStatus=true;
                }
                else
                {
                    textTask.setText(taskText);
                    taskPressedStatus=false;
                }
            }
        };
        textTask.setOnClickListener(onTaskClcik);
        RecyclerView recyclerViewButton = result.findViewById(R.id.answerRecycler);
        textQuestion.setText(question.getText());
        numberPage.setText(question.getNumber()+"/"+questionNumber);
        try {
            AnswerButtonAdapter adapter = new AnswerButtonAdapter(result.getContext(), question.getAnswers());
            Log.d("AnswerButton", "CreatedAdapter");
            recyclerViewButton.setAdapter(adapter);
            Log.d("AnswerButton", "AdpterSet");
        }
        catch (Exception e)
        {
            Log.e("AnswerButton",e.toString());
        }
        return result;
    }
}
