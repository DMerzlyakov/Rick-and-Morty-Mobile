package com.example.rickandmorty.location.data.list.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.rickandmorty.location.data.list.local.LocationDao
import com.example.rickandmorty.location.data.list.local.model.LocationEntity
import com.example.rickandmorty.location.data.list.remote.LocationListApi

@OptIn(ExperimentalPagingApi::class)
class LocationRemoteMediator(
    private val locationApi: LocationListApi,
    private val locationDao: LocationDao,
    private val name: String,
    private val type: String,
    private val dimension: String
) : RemoteMediator<Int, LocationEntity>() {

    private var pageIndex = 1
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationEntity>
    ): MediatorResult {

        pageIndex =
            getPagedIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize

        return try {

            val locations = getLocationsByRemote(name, type, dimension)

            if (loadType == LoadType.REFRESH){
                locationDao.refresh(locations, name, type, dimension)
            } else{
                locationDao.save(locations)
            }
            MediatorResult.Success(
                endOfPaginationReached = locations.size < limit
            )
        } catch (e: Exception){
            MediatorResult.Error(e)
        }
    }

    private suspend fun getLocationsByRemote(name: String, type: String, dimension: String): List<LocationEntity> {
        return locationApi.getAllLocation(pageIndex, name, type, dimension).body()!!.results.map {
            LocationEntity(it.id, it.name, it.type, it.dimension)
        }
    }

    private fun getPagedIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex

    }
}