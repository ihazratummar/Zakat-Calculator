package com.practice.zakatcalculator.domain

import com.practice.zakatcalculator.domain.model.NisabEntity
import com.practice.zakatcalculator.domain.model.ZakatEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface Repository {
    /*
    Nisab
     */
    suspend fun insertNisab(nisabEntity: NisabEntity)
    suspend fun deleteNisab(nisabEntity: NisabEntity)
    fun getNisab(): Flow<NisabEntity>

    /*
    Zakat Details edit get delete
     */
    suspend fun insertZakat(zakatEntity: ZakatEntity)
    suspend fun deleteZakat(zakatId: Int)
    fun getZakatDetails(): Flow<List<ZakatEntity>>
    fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>>
    fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>>

}