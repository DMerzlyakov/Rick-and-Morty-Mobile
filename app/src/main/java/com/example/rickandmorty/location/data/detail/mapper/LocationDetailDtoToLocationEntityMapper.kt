package com.example.rickandmorty.location.data.detail.mapper

import com.example.rickandmorty.character.data.detail.remote.model.CharacterDetailDTO
import com.example.rickandmorty.character.data.list.local.model.CharacterEntity
import com.example.rickandmorty.character.domain.detail.model.CharacterDetailDomain
import com.example.rickandmorty.character.domain.detail.model.LocationDetailDomain
import com.example.rickandmorty.location.data.detail.remote.model.LocationDetailDto
import com.example.rickandmorty.location.data.list.local.model.LocationEntity
import javax.inject.Inject




class LocationDetailDtoToLocationEntityMapper @Inject constructor() {

    operator fun invoke(item: LocationDetailDto): LocationEntity {

        val residentList =
            if (item.residents.isEmpty()) emptyList<Int>()
            else item.residents.map { field -> field.split("/").last().toInt() }

        return LocationEntity(
            item.id,
            item.name,
            item.type,
            item.dimension,
            residentList
        )
    }
}