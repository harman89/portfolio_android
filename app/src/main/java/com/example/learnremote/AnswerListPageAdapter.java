package com.example.learnremote;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class AnswerListPageAdapter extends FragmentStateAdapter {
    private ArrayList<Question> questions;
    private String task;
    public AnswerListPageAdapter(@NonNull FragmentActivity fragmentActivity,ArrayList<Question> questions,String partTask) {
        super(fragmentActivity);
        this.questions=questions;
        this.task=partTask;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(TestPageFragment.newInstance(questions.get(position),task,questions.size()));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
