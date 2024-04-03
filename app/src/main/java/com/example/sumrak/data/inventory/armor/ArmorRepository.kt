package com.example.sumrak.data.inventory.armor

class ArmorRepository(
    private val daoArmorDb: DaoArmorDb
) {

    suspend fun addArmor(armorDbEntity: ArmorDbEntity): Long{
        return daoArmorDb.addArmor(armorDbEntity)
    }

    suspend fun updateArmor(armorDbEntity: ArmorDbEntity){
        return daoArmorDb.updateArmor(armorDbEntity)
    }

    suspend fun updateEnduranceArmor(id: Int, endurance: Int){
        return daoArmorDb.updateEnduranceArmor(TuplesArmorEndurance(id, endurance))
    }

    suspend fun getArmorListToPlayer(idPlayer: Int): List<ArmorDbEntity>{
        return daoArmorDb.getArmorListToPlayer(idPlayer)
    }

    suspend fun getArmorToId(id: Int) : ArmorDbEntity{
        return daoArmorDb.getArmorToId(id)
    }

    suspend fun deleteArmorToId(id : Int){
        return daoArmorDb.deleteArmorToId(id)
    }
}