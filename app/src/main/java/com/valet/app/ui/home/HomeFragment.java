package com.valet.app.ui.home;

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
import com.valet.app.data.repository.EventRepository;
import com.valet.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FeedEventAdapter adapter;
    private EventRepository eventRepository;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventRepository = new EventRepository(requireContext());
        sessionManager = new SessionManager(requireContext());

        adapter = new FeedEventAdapter(eventId -> {
            Bundle args = new Bundle();
            args.putLong("eventId", eventId);
            Navigation.findNavController(view).navigate(R.id.eventDetailFragment, args);
        });

        binding.rvFeed.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFeed.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadFeed);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFeed();
    }

    private void loadFeed() {
        long userId = sessionManager.getUserId();
        eventRepository.getFeedEvents(userId, events -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
                binding.swipeRefresh.setRefreshing(false);
                adapter.setEvents(events);
                binding.emptyState.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
                binding.swipeRefresh.setVisibility(events.isEmpty() ? View.GONE : View.VISIBLE);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
