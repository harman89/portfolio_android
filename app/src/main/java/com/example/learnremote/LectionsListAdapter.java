package com.example.learnremote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LectionsListAdapter extends RecyclerView.Adapter<LectionsListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<LectionsClass> lections;
    private final Context currentContext;
    private int mExpandedPostion;
    public LectionsListAdapter(Context context, ArrayList<LectionsClass> lections)
    {
        this.inflater = LayoutInflater.from(context);
        this.currentContext = context;
        this.lections = lections;
        mExpandedPostion=-1;
    }
    private File getExternalPath(String filename) {
        return new File(currentContext.getExternalFilesDir(null), filename);
    }
    @NonNull
    @Override
    public LectionsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lections_recycler_item, parent, false);
        return new LectionsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectionsListAdapter.ViewHolder holder, final int position) {
        final boolean isExpanded = position==mExpandedPostion;
        final LectionsClass lection = lections.get(position);
        holder.themeView.setText(lection.getTheme()+" - "+lection.getSub_theme());
        holder.themeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File url = getExternalPath(lection.getFilename());
                try {
                    FileOpen.openFile(currentContext,url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lections.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView themeView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            themeView = (TextView) itemView.findViewById(R.id.lectionsTheme);
        }
    }
}
