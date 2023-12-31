package com.example.jcasd.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// step 9 create a database class
@Database(
    entities = [ShowEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract val dao: ShowDao
}