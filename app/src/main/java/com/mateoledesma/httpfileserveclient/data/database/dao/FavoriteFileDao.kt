package com.mateoledesma.httpfileserveclient.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mateoledesma.httpfileserveclient.data.database.entities.FavoriteFileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteFileEntity: FavoriteFileEntity)

    @Delete
    suspend fun delete(favoriteFileEntity: FavoriteFileEntity)

    @Update
    suspend fun update(favoriteFileEntity: FavoriteFileEntity)

    @Query("SELECT * FROM favorites_files")
    suspend fun getAll(): List<FavoriteFileEntity>

    @Query("SELECT * FROM favorites_files WHERE id = :id")
    fun getById(id: Int): Flow<FavoriteFileEntity>

    @Query("SELECT id FROM favorites_files WHERE id IN (:ids)")
    suspend fun getIdsByIds(ids: List<Long>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(favoriteFileEntities: List<FavoriteFileEntity>)

    @Delete
    suspend fun deleteMany(favoriteFileEntities: List<FavoriteFileEntity>)
}