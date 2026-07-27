package com.valet.app.ui.bookmarks;

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
import com.valet.app.data.repository.BookmarkRepository;
import com.valet.app.databinding.FragmentBookmarksBinding;

public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;
    private BookmarkAdapter adapter;
    private BookmarkRepository bookmarkRepository;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookmarkRepository = new BookmarkRepository(requireContext());
        sessionManager = new SessionManager(requireContext());

        adapter = new BookmarkAdapter(eventId -> {
            Bundle args = new Bundle();
            args.putLong("eventId", eventId);
            Navigation.findNavController(view).navigate(R.id.eventDetailFragment, args);
        });

        binding.rvBookmarks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBookmarks.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookmarks();
    }

    private void loadBookmarks() {
        long userId = sessionManager.getUserId();
        bookmarkRepository.getBookmarkedEvents(userId, events -> {
            if (getActivity() == null) return;
            requireActivity().runOnUiThread(() -> {
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
