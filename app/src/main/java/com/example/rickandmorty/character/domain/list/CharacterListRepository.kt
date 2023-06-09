package com.example.rickandmorty.character.domain.list

import androidx.paging.PagingData
import com.example.rickandmorty.character.domain.list.model.CharacterDomain
import kotlinx.coroutines.flow.Flow

interface CharacterListRepository {

    suspend fun getPagedCharacters(
        name: String = "", status: String = "",
        species: String = "", gender: String = "",
        characterListFilter: List<Int> = emptyList()
    ): Flow<PagingData<CharacterDomain>>

    fun getPagedCharactersCache(
        characterListFilter: List<Int>
    ): Flow<PagingData<CharacterDomain>>
}