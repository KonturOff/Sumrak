package com.example.sumrak.data.inventory.effects

class EffectsRepository(
    private val daoEffectsDb: DaoEffectsDb
) {

    suspend fun addEffects(effectsDbEntity: EffectsDbEntity): Long{
        return daoEffectsDb.addEffects(effectsDbEntity)
    }

    suspend fun updateEffects(effectsDbEntity: EffectsDbEntity){
        return daoEffectsDb.updateEffects(effectsDbEntity)
    }

    suspend fun updateCheckEffects(tuplesEffectsCheck: TuplesEffectsCheck){
        return daoEffectsDb.updateCheckEffects(tuplesEffectsCheck)
    }

    suspend fun getEffectsToIdPlayer(id: Int): List<EffectsDbEntity>{
        return daoEffectsDb.getEffectsToIdPlayer(id)
    }

    suspend fun deleteEffectsToId(id: Int){
        return daoEffectsDb.deleteEffectsToId(id)
    }
}