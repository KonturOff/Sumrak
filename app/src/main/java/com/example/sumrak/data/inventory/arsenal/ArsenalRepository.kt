package com.example.sumrak.data.inventory.arsenal

import kotlinx.coroutines.flow.Flow

class ArsenalRepository(
    private val daoArsenalDb: DaoArsenalDb
) {
    val readActiveWeapon : Flow<ArsenalDbEntity> = daoArsenalDb.getActiveWeapon()

    suspend fun addArsenalItem(arsenalDbEntity: ArsenalDbEntity) : Long{
        return daoArsenalDb.addArsenalItem(arsenalDbEntity)
    }

    suspend fun updateArsenalItem(arsenalDbEntity: ArsenalDbEntity){
        return daoArsenalDb.updateArsemalItem(arsenalDbEntity)
    }

    suspend fun updatePatronsTuples(id: Int, clip1: Int, clip2: Int, clip3: Int){
        return daoArsenalDb.updatePatronsTuples(TuplesArsenalPatrons(id, clip1, clip2, clip3))
    }

    suspend fun getArsenalToIdPlayer(id : Int) : List<ArsenalDbEntity>{
        return daoArsenalDb.getArsenalToIdPlayer(id)
    }

    suspend fun deleteArsenalToId(id: Int){
        return daoArsenalDb.deleteArsenalToId(id)
    }
}