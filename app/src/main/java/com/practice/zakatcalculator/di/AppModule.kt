package com.practice.zakatcalculator.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.practice.zakatcalculator.data.AppDatabase
import com.practice.zakatcalculator.data.Dao
import com.practice.zakatcalculator.data.repository.RepositoryImpl
import com.practice.zakatcalculator.domain.Repository
import com.practice.zakatcalculator.util.Constants.NISAB_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideNisabDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            NISAB_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: Dao): Repository {
        return RepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase): Dao {
        return appDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}