package com.example.sumrak.ui.battle.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.ui.battle.recycler.atack.AttackViewModel

abstract class DelegateAdapterB<T : BattleList,VH : RecyclerView.ViewHolder, U> {
    abstract fun onCreateViewHolder(parent: ViewGroup): VH
    abstract fun onBindViewHolder(holder: VH, item: T, attackViewModel: AttackViewModel)
}