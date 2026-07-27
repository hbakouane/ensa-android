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

import com.valet.app.R;
import com.valet.app.auth.SessionManager;
import com.valet.app.data.repository.ClubRepository;
import com.valet.app.data.repository.EventRepository;
import com.valet.app.databinding.FragmentClubDetailBinding;

public class ClubDetailFragment extends Fragment {

    private FragmentClubDetailBinding binding;
    private ClubRepository clubRepository;
    private EventRepository eventRepository;
    private SessionManager sessionManager;
    private ClubEventAdapter eventAdapter;
    private long clubId;
    private boolean isMember = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentClubDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clubRepository = new ClubRepository(requireContext());
        eventRepository = new EventRepository(requireContext());
        sessionManager = new SessionManager(requireContext());

        clubId = getArguments() != null ? getArguments().getLong("clubId") : -1;

        eventAdapter = new ClubEventAdapter(eventId -> {
            Bundle args = new Bundle();
            args.putLong("eventId", eventId);
            Navigation.findNavController(view).navigate(R.id.eventDetailFragment, args);
        });

        binding.rvEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvEvents.setAdapter(eventAdapter);

        binding.btnJoinLeave.setOnClickListener(v -> toggleMembership());

        loadClubDetail();
        loadEvents();
        checkMembership();
    }

    private void loadClubDetail() {
        clubRepository.getClubById(clubId, club -> {
            if (getActivity() == null || club == null) return;
            requireActivity().runOnUiThread(() -> {
                binding.tvClubName.setText(club.name);
                binding.tvDescription.setText(club.description);
            });

            clubRepository.getCategories(categories -> {
                if (getActivity() == null) return;
                for (var cat : categories) {
                    if (cat.id == club.categoryId) {
                        requireActivity().runOnUiThread(() ->
                                binding.tvCategory.setText(cat.name));
                        break;
                    }
                }
            });
        });

        updateMemberCount();
    }

    private void updateMemberCount() {
        clubRepository.getMemberCount(clubId, count -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() ->
                    binding.tvMemberCount.setText(count + " members"));
        });
    }

    private void loadEvents() {
        eventRepository.getUpcomingByClub(clubId, events -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
                eventAdapter.setEvents(events);
                binding.tvNoEvents.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    private void checkMembership() {
        long userId = sessionManager.getUserId();
        clubRepository.isMember(userId, clubId, member -> {
            if (getActivity() == null) return;
            isMember = member;
            requireActivity().runOnUiThread(this::updateJoinButton);
        });
    }

    private void toggleMembership() {
        long userId = sessionManager.getUserId();
        binding.btnJoinLeave.setEnabled(false);

        if (isMember) {
            clubRepository.leaveClub(userId, clubId, result -> {
                isMember = false;
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    binding.btnJoinLeave.setEnabled(true);
                    updateJoinButton();
                    updateMemberCount();
                });
            });
        } else {
            clubRepository.joinClub(userId, clubId, result -> {
                isMember = true;
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    binding.btnJoinLeave.setEnabled(true);
                    updateJoinButton();
                    updateMemberCount();
                });
            });
        }
    }

    private void updateJoinButton() {
        binding.btnJoinLeave.setText(isMember ? "Leave Club" : "Join Club");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
