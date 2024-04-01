package com.example.sumrak.Data.inventory.consumables

class ConsumablesRepository(
    private val daoConsumablesDb: DaoConsumablesDb
) {

    suspend fun addConsumables(consumablesDbEntity: ConsumablesDbEntity) : Long{
        return daoConsumablesDb.addConsumables(consumablesDbEntity)
    }


    suspend fun updateValueConsumablesTaples(id: Int, value : Int){
        return daoConsumablesDb.updateValueConsumablesTaples(UpdateTuplesConsunbles(id, value))
    }


    suspend fun getCosumablesToPlayerId(id : Int): List<ConsumablesDbEntity> {
        return daoConsumablesDb.getConsumablesToPlayerId(id)
    }

    suspend fun deleteConsumables(id: Int){
        return daoConsumablesDb.deleteConsumables(id)
    }
}