package com.example.sumrak.ui.inventory.recycler.effects.item


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryEffectsRecBinding
import com.example.sumrak.ui.inventory.recycler.effects.EffectsViewModel

class EffectsItemAdapter(private val viewModel: EffectsViewModel
) : RecyclerView.Adapter<EffectsItemAdapter.EffectsItemViewHolder>() {

    private val effItemManager = EffectsItemManager.getInstance()

    init {
        effItemManager.setAdapter(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EffectsItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_effects_rec, parent, false)
        return EffectsItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return effItemManager.getItemCount()
    }

    override fun onBindViewHolder(holder: EffectsItemViewHolder, position: Int) {
        val item = effItemManager.getItem(position)
        holder.bind(item, position, viewModel)
    }


    class EffectsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val b = MaketInventoryEffectsRecBinding.bind(itemView)


        fun bind(item: EffectsItem, position: Int, viewModel: EffectsViewModel) {
            b.checkBoxItemEffects.text = item.name
            b.checkBoxItemEffects.isChecked = item.isActive==1

            b.btnSettingsEffects.setOnClickListener { viewModel.updateMode(item.id) }
            b.btnDeleteEffects.setOnClickListener { viewModel.deleteEffect(item.id) }
            b.checkBoxItemEffects.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    viewModel.updateCheckEffectsToPosition(item.id, 1)
                }
                else{
                    viewModel.updateCheckEffectsToPosition(item.id, 0)
                }
            }
        }

    }


}


