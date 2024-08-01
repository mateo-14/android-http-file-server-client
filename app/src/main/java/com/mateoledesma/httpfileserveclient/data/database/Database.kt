package com.mateoledesma.httpfileserveclient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mateoledesma.httpfileserveclient.data.database.dao.FavoriteFileDao
import com.mateoledesma.httpfileserveclient.data.database.entities.FavoriteFileEntity

@Database(entities = [FavoriteFileEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun favoriteFileDao(): FavoriteFileDao
}