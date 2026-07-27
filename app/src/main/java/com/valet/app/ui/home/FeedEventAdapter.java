package com.valet.app.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valet.app.data.pojo.EventWithClub;
import com.valet.app.databinding.ItemEventBinding;
import com.valet.app.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class FeedEventAdapter extends RecyclerView.Adapter<FeedEventAdapter.ViewHolder> {

    private List<EventWithClub> events = new ArrayList<>();
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(long eventId);
    }

    public FeedEventAdapter(OnEventClickListener listener) {
        this.listener = listener;
    }

    public void setEvents(List<EventWithClub> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding binding = ItemEventBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventWithClub event = events.get(position);
        holder.binding.tvTitle.setText(event.getTitle());
        holder.binding.tvClubName.setText(event.clubName);
        holder.binding.tvDate.setText(DateUtil.formatFull(event.getDateTime()));
        holder.binding.tvLocation.setText(event.getLocation());
        holder.itemView.setOnClickListener(v -> listener.onEventClick(event.getId()));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemEventBinding binding;

        ViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
