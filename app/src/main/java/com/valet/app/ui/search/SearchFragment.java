package com.valet.app.ui.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.valet.app.R;
import com.valet.app.data.entity.User;
import com.valet.app.data.pojo.ClubWithMemberCount;
import com.valet.app.data.pojo.EventWithClub;
import com.valet.app.data.repository.ClubRepository;
import com.valet.app.data.repository.EventRepository;
import com.valet.app.data.repository.UserRepository;
import com.valet.app.databinding.FragmentSearchBinding;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchResultAdapter adapter;
    private ClubRepository clubRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clubRepository = new ClubRepository(requireContext());
        eventRepository = new EventRepository(requireContext());
        userRepository = new UserRepository(requireContext());

        adapter = new SearchResultAdapter(new SearchResultAdapter.OnResultClickListener() {
            @Override
            public void onClubClick(long clubId) {
                Bundle args = new Bundle();
                args.putLong("clubId", clubId);
                Navigation.findNavController(view).navigate(R.id.clubDetailFragment, args);
            }

            @Override
            public void onEventClick(long eventId) {
                Bundle args = new Bundle();
                args.putLong("eventId", eventId);
                Navigation.findNavController(view).navigate(R.id.eventDetailFragment, args);
            }

            @Override
            public void onStudentClick(long studentId) {
                Bundle args = new Bundle();
                args.putLong("studentId", studentId);
                Navigation.findNavController(view).navigate(R.id.studentProfileFragment, args);
            }
        });

        binding.rvResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvResults.setAdapter(adapter);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> performSearch(s.toString().trim());
                handler.postDelayed(searchRunnable, 300);
            }
        });

        binding.etSearch.requestFocus();
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            adapter.setResults(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
            return;
        }

        final List<ClubWithMemberCount>[] clubResults = new List[]{Collections.emptyList()};
        final List<EventWithClub>[] eventResults = new List[]{Collections.emptyList()};
        final List<User>[] studentResults = new List[]{Collections.emptyList()};
        final AtomicInteger remaining = new AtomicInteger(3);

        Runnable onComplete = () -> {
            if (remaining.decrementAndGet() == 0) {
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() ->
                        adapter.setResults(clubResults[0], eventResults[0], studentResults[0]));
            }
        };

        clubRepository.searchClubs(query, clubs -> {
            clubResults[0] = clubs;
            onComplete.run();
        });

        eventRepository.searchEvents(query, events -> {
            eventResults[0] = events;
            onComplete.run();
        });

        userRepository.searchStudents(query, students -> {
            studentResults[0] = students;
            onComplete.run();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable);
        }
        binding = null;
    }
}
