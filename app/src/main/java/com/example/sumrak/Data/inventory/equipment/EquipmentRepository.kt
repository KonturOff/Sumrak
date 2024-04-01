package com.example.sumrak.Data.inventory.equipment

class EquipmentRepository(
    private val daoEquipmentDb: DaoEquipmentDb
) {

    suspend fun addEquipment(equipmentDbEntity: EquipmentDbEntity): Long{
        return daoEquipmentDb.addEquipment(equipmentDbEntity)
    }

    suspend fun updateEquipment(equipmentDbEntity: EquipmentDbEntity){
        return daoEquipmentDb.updateEquipment(equipmentDbEntity)
    }

    suspend fun updateChargeEquipment(id: Int, charge: Int){
        return daoEquipmentDb.updateEquipmentCharge(TuplesUpdateEquipment(id, charge))
    }

    suspend fun getEquipmentToIdPlayer(id : Int) : List<EquipmentDbEntity>{
        return daoEquipmentDb.getEquipmentToIdPlayer(id)
    }

    suspend fun deleteEquipmentToId(id: Int){
        return daoEquipmentDb.deleteEquipmentToId(id)
    }
}