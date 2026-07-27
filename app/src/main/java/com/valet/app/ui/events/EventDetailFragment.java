package com.valet.app.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.valet.app.R;
import com.valet.app.auth.SessionManager;
import com.valet.app.data.repository.BookmarkRepository;
import com.valet.app.data.repository.EventRepository;
import com.valet.app.databinding.FragmentEventDetailBinding;
import com.valet.app.util.DateUtil;
import com.valet.app.util.ReminderScheduler;

public class EventDetailFragment extends Fragment {

    private FragmentEventDetailBinding binding;
    private EventRepository eventRepository;
    private BookmarkRepository bookmarkRepository;
    private SessionManager sessionManager;
    private long eventId;
    private boolean isBookmarked = false;
    private String currentRsvpStatus = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventRepository = new EventRepository(requireContext());
        bookmarkRepository = new BookmarkRepository(requireContext());
        sessionManager = new SessionManager(requireContext());

        eventId = getArguments() != null ? getArguments().getLong("eventId") : -1;

        binding.btnGoing.setOnClickListener(v -> setRsvp("GOING"));
        binding.btnNotGoing.setOnClickListener(v -> setRsvp("NOT_GOING"));
        binding.btnBookmark.setOnClickListener(v -> toggleBookmark());

        loadEventDetail();
        loadRsvpStatus();
        loadBookmarkStatus();
    }

    private void loadEventDetail() {
        eventRepository.getUpcomingEvents(events -> {
            if (getActivity() == null) return;
            for (var e : events) {
                if (e.getId() == eventId) {
                    requireActivity().runOnUiThread(() -> {
                        binding.tvTitle.setText(e.getTitle());
                        binding.tvClubName.setText(e.clubName);
                        binding.tvDateTime.setText(DateUtil.formatFull(e.getDateTime()));
                        binding.tvLocation.setText(e.getLocation());
                        binding.tvDescription.setText(e.getDescription());

                        int hours = e.event.durationMinutes / 60;
                        int mins = e.event.durationMinutes % 60;
                        String duration = hours > 0
                                ? hours + "h" + (mins > 0 ? " " + mins + "min" : "")
                                : mins + " min";
                        binding.tvDuration.setText(duration);
                    });
                    break;
                }
            }
        });

        loadAttendeeCount();
    }

    private void loadAttendeeCount() {
        eventRepository.getGoingCount(eventId, count -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() ->
                    binding.tvAttendees.setText(count + " going"));
        });
    }

    private void loadRsvpStatus() {
        long userId = sessionManager.getUserId();
        eventRepository.getRsvpStatus(userId, eventId, status -> {
            currentRsvpStatus = status;
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(this::updateRsvpButtons);
        });
    }

    private void setRsvp(String status) {
        long userId = sessionManager.getUserId();

        if (status.equals(currentRsvpStatus)) {
            eventRepository.removeRsvp(userId, eventId, result -> {
                currentRsvpStatus = null;
                if (status.equals("GOING")) {
                    ReminderScheduler.cancel(requireContext(), eventId);
                }
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    updateRsvpButtons();
                    loadAttendeeCount();
                });
            });
        } else {
            eventRepository.setRsvp(userId, eventId, status, result -> {
                currentRsvpStatus = status;
                if (status.equals("GOING")) {
                    scheduleReminder();
                } else {
                    ReminderScheduler.cancel(requireContext(), eventId);
                }
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    updateRsvpButtons();
                    loadAttendeeCount();
                });
            });
        }
    }

    private void scheduleReminder() {
        eventRepository.getUpcomingEvents(events -> {
            for (var e : events) {
                if (e.getId() == eventId) {
                    ReminderScheduler.schedule(requireContext(), eventId,
                            e.getTitle(), e.getDateTime());
                    break;
                }
            }
        });
    }

    private void updateRsvpButtons() {
        boolean going = "GOING".equals(currentRsvpStatus);
        boolean notGoing = "NOT_GOING".equals(currentRsvpStatus);

        binding.btnGoing.setAlpha(going ? 1.0f : 0.6f);
        binding.btnNotGoing.setAlpha(notGoing ? 1.0f : 0.6f);

        if (going) {
            binding.btnGoing.setIconResource(R.drawable.ic_check_circle);
            binding.btnGoing.setText("Going");
        } else {
            binding.btnGoing.setIcon(null);
            binding.btnGoing.setText("Going");
        }

        if (notGoing) {
            binding.btnNotGoing.setText("Not Going");
        } else {
            binding.btnNotGoing.setText("Not Going");
        }
    }

    private void loadBookmarkStatus() {
        long userId = sessionManager.getUserId();
        bookmarkRepository.isBookmarked(userId, eventId, bookmarked -> {
            isBookmarked = bookmarked;
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(this::updateBookmarkButton);
        });
    }

    private void toggleBookmark() {
        long userId = sessionManager.getUserId();
        bookmarkRepository.toggleBookmark(userId, eventId, nowBookmarked -> {
            isBookmarked = nowBookmarked;
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(this::updateBookmarkButton);
        });
    }

    private void updateBookmarkButton() {
        binding.btnBookmark.setIconResource(
                isBookmarked ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_border);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
