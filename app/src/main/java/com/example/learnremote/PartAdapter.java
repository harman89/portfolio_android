package com.example.learnremote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PartAdapter  extends RecyclerView.Adapter<PartAdapter.ViewHolder>{
    //infalter
    private final LayoutInflater inflater;
    //список частей
    private final List<Part> parts;
    //context текущего фрагмента
    private final Context currentContext;

    public PartAdapter(Context context, List<Part> parts) {
        this.parts = parts;
        this.inflater = LayoutInflater.from(context);
        this.currentContext=context;
    }

    //прикрепляем адаптер к соответствующему элементу RecyclerView
    @Override
    public PartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.part_item, parent, false);
        return new ViewHolder(view);
    }

    //действия при создании адаптера
    @Override
    public void onBindViewHolder(PartAdapter.ViewHolder holder, int position) {
        if(parts==null)
        {
            //проверка на пустоту parts
        }
        else {
            final Part part = parts.get(position);
            holder.partView.setText(part.getText());
            holder.numberPartView.setText(part.getNumber() + ".");
            View.OnClickListener partClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(currentContext,TestActivity.class);
                    intent.putExtra(Part.class.getSimpleName(), part);
                    currentContext.startActivity(intent);
                }
            };
            holder.partView.setOnClickListener(partClick);
        }
    }

    //возвращяет количество элементов в RecyclerView
    @Override
    public int getItemCount() {
        return parts.size();
    }

    //класс ViewHolder, отвечающий за сопоставление объекта в разметке и переменной
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView partView, numberPartView;
        ViewHolder(View view){
            super(view);
            partView = (TextView) view.findViewById(R.id.partTask);
            numberPartView = (TextView) view.findViewById(R.id.partNumber);
        }
    }
}
