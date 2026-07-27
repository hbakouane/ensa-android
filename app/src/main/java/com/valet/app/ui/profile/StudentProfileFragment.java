package com.valet.app.ui.profile;

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
import com.valet.app.data.repository.ClubRepository;
import com.valet.app.data.repository.EventRepository;
import com.valet.app.data.repository.UserRepository;
import com.valet.app.databinding.FragmentStudentProfileBinding;

public class StudentProfileFragment extends Fragment {

    private FragmentStudentProfileBinding binding;
    private UserRepository userRepository;
    private ClubRepository clubRepository;
    private EventRepository eventRepository;
    private ProfileClubAdapter clubAdapter;
    private ProfileRsvpAdapter rsvpAdapter;
    private long studentId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studentId = getArguments() != null ? getArguments().getLong("studentId") : 0;

        userRepository = new UserRepository(requireContext());
        clubRepository = new ClubRepository(requireContext());
        eventRepository = new EventRepository(requireContext());

        clubAdapter = new ProfileClubAdapter(clubId -> {
            Bundle args = new Bundle();
            args.putLong("clubId", clubId);
            Navigation.findNavController(view).navigate(R.id.action_studentProfile_to_clubDetail, args);
        });

        rsvpAdapter = new ProfileRsvpAdapter(eventId -> {
            Bundle args = new Bundle();
            args.putLong("eventId", eventId);
            Navigation.findNavController(view).navigate(R.id.action_studentProfile_to_eventDetail, args);
        });

        binding.rvClubs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClubs.setAdapter(clubAdapter);

        binding.rvRsvps.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRsvps.setAdapter(rsvpAdapter);

        loadProfile();
        loadClubs();
        loadEvents();
    }

    private void loadProfile() {
        userRepository.getById(studentId, user -> {
            if (getActivity() == null || user == null) return;
            requireActivity().runOnUiThread(() -> {
                binding.tvName.setText(user.name);
                binding.tvEmail.setText(user.email);
                binding.tvBio.setText(user.bio != null && !user.bio.isEmpty()
                        ? user.bio : "No bio yet");
            });
        });
    }

    private void loadClubs() {
        clubRepository.getJoinedClubs(studentId, clubs -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
                clubAdapter.setClubs(clubs);
                binding.tvNoClubs.setVisibility(clubs.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    private void loadEvents() {
        eventRepository.getUpcomingRsvps(studentId, events -> {
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
