package com.valet.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.valet.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                navController.navigate(R.id.searchFragment);
                return true;
            }
            return false;
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            boolean isTopLevel = id == R.id.homeFragment || id == R.id.clubsFragment
                    || id == R.id.eventsFragment || id == R.id.profileFragment;

            binding.bottomNav.setVisibility(isTopLevel ? View.VISIBLE : View.GONE);

            if (isTopLevel) {
                binding.toolbar.setNavigationIcon(null);
                binding.toolbar.setTitle(destination.getLabel());
            } else {
                binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
                binding.toolbar.setTitle(destination.getLabel());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
