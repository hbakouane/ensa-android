package com.valet.app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.valet.app.auth.SessionManager;
import com.valet.app.data.repository.UserRepository;
import com.valet.app.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepository(requireContext());
        sessionManager = new SessionManager(requireContext());

        loadCurrentProfile();

        binding.btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentProfile() {
        long userId = sessionManager.getUserId();
        userRepository.getById(userId, user -> {
            if (getActivity() == null || user == null) return;
            requireActivity().runOnUiThread(() -> {
                binding.etName.setText(user.name);
                binding.etBio.setText(user.bio);
            });
        });
    }

    private void saveProfile() {
        String name = binding.etName.getText().toString().trim();
        String bio = binding.etBio.getText().toString().trim();

        if (name.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Name cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }

        binding.btnSave.setEnabled(false);
        long userId = sessionManager.getUserId();

        userRepository.getById(userId, user -> {
            if (user == null) return;
            user.name = name;
            user.bio = bio;
            userRepository.updateProfile(user, result -> {
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    binding.btnSave.setEnabled(true);
                    Snackbar.make(binding.getRoot(), "Profile updated", Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                });
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
