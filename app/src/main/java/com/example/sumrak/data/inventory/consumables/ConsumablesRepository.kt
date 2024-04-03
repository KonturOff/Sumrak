package com.example.sumrak.data.inventory.consumables

class ConsumablesRepository(
    private val daoConsumablesDb: DaoConsumablesDb
) {

    suspend fun addConsumables(consumablesDbEntity: ConsumablesDbEntity) : Long{
        return daoConsumablesDb.addConsumables(consumablesDbEntity)
    }


    suspend fun updateValueConsumablesTuples(id: Int, value : Int){
        return daoConsumablesDb.updateValueConsumablesTaples(UpdateTuplesConsumables(id, value))
    }


    suspend fun getConsumablesToPlayerId(id : Int): List<ConsumablesDbEntity> {
        return daoConsumablesDb.getConsumablesToPlayerId(id)
    }

    suspend fun deleteConsumables(id: Int){
        return daoConsumablesDb.deleteConsumables(id)
    }
}