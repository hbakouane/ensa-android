package com.valet.app.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valet.app.data.entity.User;
import com.valet.app.data.pojo.ClubWithMemberCount;
import com.valet.app.data.pojo.EventWithClub;
import com.valet.app.databinding.ItemSearchClubBinding;
import com.valet.app.databinding.ItemSearchEventBinding;
import com.valet.app.databinding.ItemSearchStudentBinding;
import com.valet.app.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CLUB = 0;
    private static final int TYPE_EVENT = 1;
    private static final int TYPE_STUDENT = 2;

    private final List<Object> items = new ArrayList<>();
    private OnResultClickListener listener;

    public interface OnResultClickListener {
        void onClubClick(long clubId);
        void onEventClick(long eventId);
        void onStudentClick(long studentId);
    }

    public SearchResultAdapter(OnResultClickListener listener) {
        this.listener = listener;
    }

    public void setResults(List<ClubWithMemberCount> clubs, List<EventWithClub> events, List<User> students) {
        items.clear();
        items.addAll(clubs);
        items.addAll(events);
        items.addAll(students);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof ClubWithMemberCount) return TYPE_CLUB;
        if (item instanceof EventWithClub) return TYPE_EVENT;
        return TYPE_STUDENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_CLUB) {
            return new ClubViewHolder(ItemSearchClubBinding.inflate(inflater, parent, false));
        } else if (viewType == TYPE_EVENT) {
            return new EventViewHolder(ItemSearchEventBinding.inflate(inflater, parent, false));
        } else {
            return new StudentViewHolder(ItemSearchStudentBinding.inflate(inflater, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ClubViewHolder) {
            ClubWithMemberCount club = (ClubWithMemberCount) items.get(position);
            ClubViewHolder cvh = (ClubViewHolder) holder;
            cvh.binding.tvName.setText(club.getName());
            cvh.binding.tvCategory.setText(club.categoryName);
            cvh.binding.tvMemberCount.setText(club.memberCount + " members");
            cvh.itemView.setOnClickListener(v -> listener.onClubClick(club.getId()));
        } else if (holder instanceof EventViewHolder) {
            EventWithClub event = (EventWithClub) items.get(position);
            EventViewHolder evh = (EventViewHolder) holder;
            evh.binding.tvTitle.setText(event.getTitle());
            evh.binding.tvClubName.setText(event.clubName);
            evh.binding.tvDate.setText(DateUtil.formatRelative(event.getDateTime()));
            evh.itemView.setOnClickListener(v -> listener.onEventClick(event.getId()));
        } else {
            User student = (User) items.get(position);
            StudentViewHolder svh = (StudentViewHolder) holder;
            svh.binding.tvName.setText(student.name);
            svh.binding.tvEmail.setText(student.email);
            svh.itemView.setOnClickListener(v -> listener.onStudentClick(student.id));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ClubViewHolder extends RecyclerView.ViewHolder {
        final ItemSearchClubBinding binding;
        ClubViewHolder(ItemSearchClubBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        final ItemSearchEventBinding binding;
        EventViewHolder(ItemSearchEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        final ItemSearchStudentBinding binding;
        StudentViewHolder(ItemSearchStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
