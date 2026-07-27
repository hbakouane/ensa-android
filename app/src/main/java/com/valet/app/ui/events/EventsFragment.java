package com.valet.app.ui.events;

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
import com.valet.app.data.repository.EventRepository;
import com.valet.app.databinding.FragmentEventsBinding;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;
    private EventAdapter adapter;
    private EventRepository eventRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventRepository = new EventRepository(requireContext());

        adapter = new EventAdapter(eventId -> {
            Bundle args = new Bundle();
            args.putLong("eventId", eventId);
            Navigation.findNavController(view).navigate(R.id.eventDetailFragment, args);
        });

        binding.rvEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvEvents.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadEvents);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
    }

    private void loadEvents() {
        eventRepository.getUpcomingEvents(events -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
                binding.swipeRefresh.setRefreshing(false);
                adapter.setEvents(events);
                binding.emptyState.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
