package com.example.learnremote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnremote.ui.slideshow.NotificationFragment;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final Context currentContext;
    private final ArrayList<NotificationTextItem> notifications;
    public NotificationAdapter(Context context, ArrayList<NotificationTextItem> notifications)
    {
        this.inflater=LayoutInflater.from(context);
        this.currentContext=context;
        this.notifications=notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotificationTextItem notification = notifications.get(position);
        holder.date.setText(notification.getDate());
        holder.notificationText.setText(notification.getNotificationText());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final TextView notificationText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=(TextView)itemView.findViewById(R.id.notificationDate);
            notificationText=(TextView)itemView.findViewById(R.id.notificationText);
        }
    }
}
