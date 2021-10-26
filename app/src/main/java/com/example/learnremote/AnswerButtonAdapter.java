package com.example.learnremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnswerButtonAdapter extends RecyclerView.Adapter<AnswerButtonAdapter.ViewHolder>  {

    private final LayoutInflater inflater;
    private final ArrayList<Answer> answers;
    private int selected_postion =-1;
    private SharedPreferences testRes;
    private int timesClicked=0;
    boolean flag=false;
    public AnswerButtonAdapter(Context context, ArrayList<Answer> answers) {
        this.inflater = LayoutInflater.from(context);
        Log.d("AnswerButton", "GotInflanter");
        this.answers = answers;
        for(int i =0;i<answers.size();i++) {
            Log.d("AnswerButton", answers.get(i).getText()+answers.size());
        }
    }

    @NonNull
    @Override
    public AnswerButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_answer, parent, false);
        Log.d("AnswerButton", "Created view");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerButtonAdapter.ViewHolder holder, int position) {
        try{
            final Answer answer = answers.get(position);
            holder.answerVariant.setText(answer.getText());
            holder.answerVariant.setBackgroundColor(selected_postion==position? inflater.getContext().getResources().getColor(R.color.seleted_color):Color.TRANSPARENT);
        }
        catch (Exception e)
        {
            Log.e("AnswerButton", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView answerVariant;
        ViewHolder(View view){
            super(view);

            answerVariant = view.findViewById(R.id.answerVariant);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            testRes=v.getContext().getSharedPreferences("testRes",Context.MODE_PRIVATE);
            int temp_res;

            if(getAdapterPosition()==RecyclerView.NO_POSITION) return;
            notifyItemChanged(selected_postion);
            selected_postion=getAdapterPosition();
            notifyItemChanged(selected_postion);
            if(answers.get(selected_postion).getIsTrue()==1)
            {
                if(timesClicked==0|| !flag) {

                    temp_res = testRes.getInt("testRes", 0);
                    temp_res++;
                    testRes.edit().putInt("testRes", temp_res).apply();
                    timesClicked++;
                    flag = true;
                    Log.d("debugRes",timesClicked+" clicked");
                    Log.d("debugRes",temp_res+"");
                }
            }
            else if (flag)
            {
                Log.d("debugRes",timesClicked+" flag");
                if(timesClicked>0) {

                    temp_res = testRes.getInt("testRes", 0);
                    if(temp_res>0) {
                        temp_res--;
                    }
                    testRes.edit().putInt("testRes", temp_res).apply();
                    timesClicked++;
                    flag = false;
                    Log.d("debugRes",timesClicked+" clicked");
                    Log.d("debugRes",temp_res+"");
                }
            }
        }
    }
}
