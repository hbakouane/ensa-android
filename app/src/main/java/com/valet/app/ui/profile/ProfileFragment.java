package com.valet.app.ui.profile;

import android.content.Intent;
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
import com.valet.app.auth.LoginActivity;
import com.valet.app.auth.SessionManager;
import com.valet.app.data.repository.ClubRepository;
import com.valet.app.data.repository.EventRepository;
import com.valet.app.data.repository.UserRepository;
import com.valet.app.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private UserRepository userRepository;
    private ClubRepository clubRepository;
    private EventRepository eventRepository;
    private SessionManager sessionManager;
    private ProfileClubAdapter clubAdapter;
    private ProfileRsvpAdapter rsvpAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepository(requireContext());
        clubRepository = new ClubRepository(requireContext());
        eventRepository = new EventRepository(requireContext());
        sessionManager = new SessionManager(requireContext());

        clubAdapter = new ProfileClubAdapter(clubId -> {
            Bundle args = new Bundle();
            args.putLong("clubId", clubId);
            Navigation.findNavController(view).navigate(R.id.clubDetailFragment, args);
        });

        rsvpAdapter = new ProfileRsvpAdapter(eventId -> {
            Bundle args = new Bundle();
            args.putLong("eventId", eventId);
            Navigation.findNavController(view).navigate(R.id.eventDetailFragment, args);
        });

        binding.rvClubs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClubs.setAdapter(clubAdapter);

        binding.rvRsvps.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRsvps.setAdapter(rsvpAdapter);

        binding.btnEdit.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.editProfileFragment));

        binding.btnBookmarks.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.bookmarksFragment));

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
        loadJoinedClubs();
        loadUpcomingRsvps();
    }

    private void loadProfile() {
        long userId = sessionManager.getUserId();
        userRepository.getById(userId, user -> {
            if (getActivity() == null || user == null) return;
            requireActivity().runOnUiThread(() -> {
                binding.tvName.setText(user.name);
                binding.tvEmail.setText(user.email);
                binding.tvBio.setText(user.bio != null && !user.bio.isEmpty()
                        ? user.bio : "No bio yet");
            });
        });
    }

    private void loadJoinedClubs() {
        long userId = sessionManager.getUserId();
        clubRepository.getJoinedClubs(userId, clubs -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
                clubAdapter.setClubs(clubs);
                binding.tvNoClubs.setVisibility(clubs.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    private void loadUpcomingRsvps() {
        long userId = sessionManager.getUserId();
        eventRepository.getUpcomingRsvps(userId, events -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
                rsvpAdapter.setEvents(events);
                binding.tvNoRsvps.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
