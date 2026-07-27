package com.valet.app.ui.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valet.app.data.pojo.EventWithClub;
import com.valet.app.databinding.ItemProfileRsvpBinding;
import com.valet.app.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class ProfileRsvpAdapter extends RecyclerView.Adapter<ProfileRsvpAdapter.ViewHolder> {

    private List<EventWithClub> events = new ArrayList<>();
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(long eventId);
    }

    public ProfileRsvpAdapter(OnEventClickListener listener) {
        this.listener = listener;
    }

    public void setEvents(List<EventWithClub> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProfileRsvpBinding binding = ItemProfileRsvpBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventWithClub event = events.get(position);
        holder.binding.tvTitle.setText(event.getTitle());
        holder.binding.tvClubName.setText(event.clubName);
        holder.binding.tvDate.setText(DateUtil.formatRelative(event.getDateTime()));
        holder.itemView.setOnClickListener(v -> listener.onEventClick(event.getId()));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemProfileRsvpBinding binding;

        ViewHolder(ItemProfileRsvpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
