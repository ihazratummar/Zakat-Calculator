package com.practice.zakatcalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.zakatcalculator.domain.model.NisabEntity
import com.practice.zakatcalculator.domain.model.ZakatEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNisab(nisabEntity: NisabEntity)

    @Delete
    suspend fun deleteNisab(nisabEntity: NisabEntity)

    @Query("SELECT * FROM nisabentity WHERE id = 1")
    fun getNisab(): Flow<NisabEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZakatDetails(zakatEntity: ZakatEntity)

    @Query("DELETE FROM zakatentity WHERE id =:zakatId")
    suspend fun deleteZakatDetails(zakatId: Int)

    @Query("SELECT * FROM zakatentity")
    fun getZakatDetails(): Flow<List<ZakatEntity>>

    @Query("SELECT * FROM zakatentity ORDER BY date DESC")
    fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>>

    @Query("SELECT * FROM zakatentity ORDER BY date ASC")
    fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>>


}