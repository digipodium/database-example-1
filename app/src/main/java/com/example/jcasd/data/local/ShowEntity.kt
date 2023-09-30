package com.example.jcasd.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


// step 10 create an entity data class, rename it to your liking
@Entity(tableName = "shows")
data class ShowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val ott: String = "",
    val priority: Int = 0,
)

