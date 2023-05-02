package com.example.rickandmorty.episode.di.list.modules

import com.example.rickandmorty.episode.domain.list.GetEpisodeListUseCase
import com.example.rickandmorty.episode.domain.list.GetEpisodeListUseCaseImpl
import com.example.rickandmorty.location.domain.list.GetLocationListUseCase
import com.example.rickandmorty.location.domain.list.GetLocationListUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface EpisodeListUseCaseModule {

    @Binds
    fun bindGetEpisodeListUseCase(getEpisodeListUseCaseImpl: GetEpisodeListUseCaseImpl): GetEpisodeListUseCase

}