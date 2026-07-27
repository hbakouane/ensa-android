package com.valet.app.ui.clubs;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valet.app.data.pojo.ClubWithMemberCount;
import com.valet.app.databinding.ItemClubBinding;

import java.util.ArrayList;
import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {

    private List<ClubWithMemberCount> clubs = new ArrayList<>();
    private OnClubClickListener listener;

    public interface OnClubClickListener {
        void onClubClick(long clubId);
    }

    public ClubAdapter(OnClubClickListener listener) {
        this.listener = listener;
    }

    public void setClubs(List<ClubWithMemberCount> clubs) {
        this.clubs = clubs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClubBinding binding = ItemClubBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClubWithMemberCount club = clubs.get(position);
        holder.binding.tvName.setText(club.getName());
        holder.binding.tvCategory.setText(club.categoryName);
        holder.binding.tvDescription.setText(club.getDescription());
        holder.binding.tvMemberCount.setText(club.memberCount + " members");
        holder.itemView.setOnClickListener(v -> listener.onClubClick(club.getId()));
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemClubBinding binding;

        ViewHolder(ItemClubBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
