package com.example.sit305ass2;

import android.content.Context;
import android.media.metrics.Event;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder>
{
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<EventModel> eventModels;
    public EventRecyclerViewAdapter(Context context, List<EventModel> eventModels,
                                    RecyclerViewInterface recyclerViewInterface)
    {
        this.context = context;
        this.eventModels = eventModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setEventList(List<EventModel> eventModels)
    {
        this.eventModels = eventModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return new EventRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.MyViewHolder holder, int position)
    {
        holder.textViewTitle.setText(eventModels.get(position).getTitle());
        holder.textViewCategory.setText(eventModels.get(position).getCategory());
        holder.textViewLocation.setText(eventModels.get(position).getLocation());
        holder.textViewDate.setText(eventModels.get(position).getDate());
        holder.textViewTime.setText(eventModels.get(position).getTime());
    }

    @Override
    public int getItemCount()
    {
        return eventModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewTitle, textViewCategory, textViewLocation, textViewDate, textViewTime;
        ImageButton editButton, deleteButton;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);

            // TEXT VIEWS
            textViewTitle = itemView.findViewById(R.id.title);
            textViewCategory = itemView.findViewById(R.id.category);
            textViewLocation = itemView.findViewById(R.id.location);
            textViewDate = itemView.findViewById(R.id.date);
            textViewTime = itemView.findViewById(R.id.time);

            // IMAGE BUTTONS
            editButton = itemView.findViewById(R.id.edit);
            deleteButton = itemView.findViewById(R.id.delete);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (recyclerViewInterface != null)
                    {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onEditClick(pos);
                        }
                    }

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (recyclerViewInterface != null)
                    {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onDeleteClick(pos);
                        }
                    }
                }
            });

        }
    }
}
