package com.mateoledesma.httpfileserveclient.di

import android.content.Context
import androidx.room.Room
import com.mateoledesma.httpfileserveclient.data.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(
            appContext,
            Database::class.java,
            "database"
        )
        .build()
    }

    @Singleton
    @Provides
    fun provideFileEntryDao(database: Database) = database.favoriteFileDao()

}