package com.valet.app.ui.clubs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.valet.app.R;
import com.valet.app.data.entity.Category;
import com.valet.app.data.repository.ClubRepository;
import com.valet.app.databinding.FragmentClubsBinding;

public class ClubsFragment extends Fragment {

    private FragmentClubsBinding binding;
    private ClubAdapter adapter;
    private ClubRepository clubRepository;
    private long selectedCategoryId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentClubsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clubRepository = new ClubRepository(requireContext());

        adapter = new ClubAdapter(clubId -> {
            Bundle args = new Bundle();
            args.putLong("clubId", clubId);
            Navigation.findNavController(view).navigate(R.id.clubDetailFragment, args);
        });

        binding.rvClubs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClubs.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadClubs);

        loadCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClubs();
    }

    private void loadCategories() {
        clubRepository.getCategories(categories -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
                Chip allChip = new Chip(requireContext());
                allChip.setText("All");
                allChip.setCheckable(true);
                allChip.setChecked(true);
                allChip.setOnClickListener(v -> {
                    selectedCategoryId = -1;
                    loadClubs();
                });
                binding.chipGroup.addView(allChip);

                for (Category cat : categories) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(cat.name);
                    chip.setCheckable(true);
                    chip.setOnClickListener(v -> {
                        selectedCategoryId = cat.id;
                        loadClubs();
                    });
                    binding.chipGroup.addView(chip);
                }
            });
        });
    }

    private void loadClubs() {
        if (selectedCategoryId == -1) {
            clubRepository.getAllClubs(clubs -> {
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    binding.swipeRefresh.setRefreshing(false);
                    adapter.setClubs(clubs);
                });
            });
        } else {
            clubRepository.getClubsByCategory(selectedCategoryId, clubs -> {
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    binding.swipeRefresh.setRefreshing(false);
                    adapter.setClubs(clubs);
                });
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
