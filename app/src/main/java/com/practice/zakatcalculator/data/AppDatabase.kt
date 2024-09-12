package com.practice.zakatcalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practice.zakatcalculator.domain.model.NisabEntity
import com.practice.zakatcalculator.domain.model.ZakatEntity

/**
 * @author Hazrat Ummar Shaikh
 */

@Database(entities = [NisabEntity::class, ZakatEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase(){
    abstract fun dao(): Dao
}