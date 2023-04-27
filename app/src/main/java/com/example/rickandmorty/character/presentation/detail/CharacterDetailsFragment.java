package com.example.rickandmorty.character.presentation.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.rickandmorty.R;

import com.example.rickandmorty.character.domain.detail.CharacterDetail;
import com.example.rickandmorty.databinding.FragmentCharacterDetailsBinding;
import com.example.rickandmorty.main.OnNavigationListener;


public class CharacterDetailsFragment extends Fragment {

    private OnNavigationListener onNavigationListener;
    private FragmentCharacterDetailsBinding binding;
    private CharacterDetailViewModel viewModel;
    private CharacterDetail mCharacter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationListener) {
            onNavigationListener = (OnNavigationListener) context;
        } else {
            throw new RuntimeException("Activity must be implements OnNavigationListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int mId = requireArguments().getInt(CharacterDetailsFragment.ARG_PARAM_CHARACTER_ID);

        viewModel = new ViewModelProvider(this, new CharacterDetailsViewModelFactory(mId))
                .get(CharacterDetailViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false);

        setupButtonListeners();
        observeData();

        return binding.getRoot();
    }

    private void setupButtonListeners() {
        binding.btnBack.setOnClickListener(view -> onNavigationListener.toBackStack());


    }


    private void observeData(){
        viewModel.getCharacterLiveData().observe(getViewLifecycleOwner(), characterDetail -> {
            mCharacter = characterDetail;
            updateViewDetail();

        });
    }

    private void updateViewDetail(){
        if (mCharacter != null){
            binding.nameView.setText(mCharacter.getName());
            Glide.with(requireContext())
                    .load(mCharacter.getUrlAvatar())
                    .placeholder(R.drawable.gray_gradient)
                    .into(binding.avatarView);

            binding.genreView.setText(mCharacter.getGender());
            binding.speciesView.setText(mCharacter.getSpecies());
            binding.statusView.setText(mCharacter.getStatus());
            binding.lastLocationView.setText(mCharacter.getLocation().getName());
            binding.originLocationView.setText(mCharacter.getOrigin().getName());
        }
    }




    public static final String ARG_PARAM_CHARACTER_ID = "id";
    public static CharacterDetailsFragment newInstance(int mId) {
        CharacterDetailsFragment fragment = new CharacterDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_CHARACTER_ID, mId);
        fragment.setArguments(args);
        return fragment;
    }
}