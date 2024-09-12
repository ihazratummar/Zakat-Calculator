package com.practice.zakatcalculator.data.repository

import com.practice.zakatcalculator.data.Dao
import com.practice.zakatcalculator.domain.Repository
import com.practice.zakatcalculator.domain.model.NisabEntity
import com.practice.zakatcalculator.domain.model.ZakatEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class RepositoryImpl @Inject  constructor(
    private  val dao: Dao
): Repository{
    override suspend fun insertNisab(nisabEntity: NisabEntity) {

        dao.insertNisab(nisabEntity)
    }

    override suspend fun deleteNisab(nisabEntity: NisabEntity) {
        dao.deleteNisab(nisabEntity)
    }

    override fun getNisab(): Flow<NisabEntity> {
        return dao.getNisab()
    }

    override suspend fun insertZakat(zakatEntity: ZakatEntity) {
        dao.insertZakatDetails(zakatEntity = zakatEntity)
    }

    override suspend fun deleteZakat(zakatId:Int) {
        dao.deleteZakatDetails(zakatId)
    }

    override fun getZakatDetails(): Flow<List<ZakatEntity>> {
        return  dao.getZakatDetails()
    }

    override fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>> {
        return  dao.getZakatDetailsByDateDesc()
    }

    override fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>> {
        return dao.getZakatDetailsByDateAsc()
    }

}