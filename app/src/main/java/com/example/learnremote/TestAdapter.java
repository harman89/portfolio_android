package com.example.learnremote;

import android.content.Context;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter  extends RecyclerView.Adapter<TestAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Test> tests;
    private final Context currentContext;
    private int mExpandedPostion;
    public TestAdapter(Context context, List<Test> tests) {
        this.tests = tests;
        this.inflater = LayoutInflater.from(context);
        this.currentContext = context;
        mExpandedPostion=-1;
    }
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TestAdapter.ViewHolder holder, final int position) {
        final boolean isExpanded = position==mExpandedPostion;
        final Test test = tests.get(position);
        holder.themeView.setText(test.getTheme());
        PartAdapter partAdapter = new PartAdapter(currentContext, test.getParts());
        holder.partViewList.setAdapter(partAdapter);
        holder.partViewList.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.themeView.setActivated(isExpanded);
        holder.themeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPostion=isExpanded?-1:position;
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView themeView;
        final RecyclerView partViewList;
        ViewHolder(View view){
            super(view);
            themeView = (TextView) view.findViewById(R.id.text_theme);
            partViewList = (RecyclerView) view.findViewById(R.id.partsRecycler);
        }
    }
}

