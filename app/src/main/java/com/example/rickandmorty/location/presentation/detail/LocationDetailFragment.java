package com.example.rickandmorty.location.presentation.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rickandmorty.character.presentation.list.CharacterListFragment;
import com.example.rickandmorty.databinding.FragmentLocationDetailBinding;
import com.example.rickandmorty.location.di.DaggerLocationComponent;
import com.example.rickandmorty.location.di.LocationComponent;
import com.example.rickandmorty.location.presentation.detail.model.LocationDetailUi;
import com.example.rickandmorty.main.presentation.OnNavigationListener;
import com.example.rickandmorty.main.presentation.RickAndMortyApp;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;


public class LocationDetailFragment extends Fragment {

    private OnNavigationListener onNavigationListener;
    private FragmentLocationDetailBinding binding;
    private LocationDetailViewModel viewModel;
    private LocationDetailUi mLocation;

    @Inject
    LocationDetailViewModelFactory viewModelFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocationComponent component = DaggerLocationComponent
                .factory()
                .create(((RickAndMortyApp) requireActivity().getApplication()).getComponent());

        component.inject(this);
        if (context instanceof OnNavigationListener) {
            onNavigationListener = (OnNavigationListener) context;
        } else {
            throw new RuntimeException("Activity must be implements OnNavigationListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelFactory)
                .get(LocationDetailViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false);
        setupButtonListeners();
        observeData(requireArguments().getInt(LocationDetailFragment.ARG_PARAM_CHARACTER_ID));
        return binding.getRoot();
    }

    private void setupButtonListeners() {
        binding.btnBack.setOnClickListener(view -> onNavigationListener.toBackStack());
        binding.refreshLayout.setOnRefreshListener(() -> {
                    if (mLocation != null) {
                        viewModel.getLocation(mLocation.getId());
                    }
                }
        );
    }


    private void observeData(int mId) {
        viewModel.getLocation(mId);
        viewModel.getLocationLiveData().observe(getViewLifecycleOwner(), characterDetail -> {
            mLocation = characterDetail;
            updateViewDetail();
            setupCharacterList();
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), value -> {
            Snackbar.make(binding.getRoot(), value, Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setupCharacterList() {
        getChildFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(binding.characterList.getId(), CharacterListFragment.newInstance(CharacterListFragment.getTypeListOnly(), mLocation.getResidents()))
                .commit();
    }

    private void updateViewDetail() {
        binding.refreshLayout.setRefreshing(false);
        binding.circularProgressBar.setVisibility(View.INVISIBLE);
        binding.mainLayout.setVisibility(View.VISIBLE);
        if (mLocation != null) {
            binding.nameView.setText(mLocation.getName());
            binding.dimensionView.setText(mLocation.getDimension());
            binding.typeView.setText(mLocation.getType());
        }
    }


    public static final String ARG_PARAM_CHARACTER_ID = "id";

    public static LocationDetailFragment newInstance(int mId) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_CHARACTER_ID, mId);
        fragment.setArguments(args);
        return fragment;
    }
}
