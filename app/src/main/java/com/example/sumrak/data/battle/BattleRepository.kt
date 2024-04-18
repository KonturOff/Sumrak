package com.example.sumrak.data.battle

class BattleRepository(
    private val daoBattleItem: DaoBattleItem
) {

    suspend fun addBattleItem(battleItemEntity: BattleItemEntity){
        return daoBattleItem.addBattleItem(battleItemEntity)
    }

    suspend fun getBattleItemToIdPlayer(idPlayer: Int) : List<BattleItemEntity>{
        return daoBattleItem.getBattleItemToPlayer(idPlayer)
    }

    suspend fun updateBattleItemPosition(battleItemEntity: BattleItemEntity){
        return daoBattleItem.updateBattleItemPosition(battleItemEntity)
    }
    suspend fun updateVisibleToId(id : Int, isExpanded: Boolean){
        return daoBattleItem.updateVisibleToId(TuplesBattleItem(id, isExpanded))
    }
}