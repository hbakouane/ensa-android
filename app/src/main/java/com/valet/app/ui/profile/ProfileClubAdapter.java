package com.valet.app.ui.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valet.app.data.pojo.ClubWithMemberCount;
import com.valet.app.databinding.ItemProfileClubBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileClubAdapter extends RecyclerView.Adapter<ProfileClubAdapter.ViewHolder> {

    private List<ClubWithMemberCount> clubs = new ArrayList<>();
    private OnClubClickListener listener;

    public interface OnClubClickListener {
        void onClubClick(long clubId);
    }

    public ProfileClubAdapter(OnClubClickListener listener) {
        this.listener = listener;
    }

    public void setClubs(List<ClubWithMemberCount> clubs) {
        this.clubs = clubs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProfileClubBinding binding = ItemProfileClubBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClubWithMemberCount club = clubs.get(position);
        holder.binding.tvName.setText(club.getName());
        holder.binding.tvCategory.setText(club.categoryName);
        holder.itemView.setOnClickListener(v -> listener.onClubClick(club.getId()));
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemProfileClubBinding binding;

        ViewHolder(ItemProfileClubBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
