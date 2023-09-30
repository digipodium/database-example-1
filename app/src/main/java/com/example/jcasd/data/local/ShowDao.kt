package com.example.jcasd.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// step 11 create a dao interface, rename it to your liking
@Dao
interface ShowDao {
    @Upsert
    suspend fun addShow(show: ShowEntity)

    @Delete
    suspend fun deleteShow(show: ShowEntity)

    @Query("SELECT * FROM shows")
    fun getShows(): Flow<List<ShowEntity>>
}