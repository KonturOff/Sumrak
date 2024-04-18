package com.example.sumrak.ui.inventory.recycler.note

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.data.inventory.note.NoteDbEntity
import com.example.sumrak.databinding.MaketInventoryNoteBinding
import com.example.sumrak.ui.inventory.InventoryFragment
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter

class NoteAdapter(
    private val inventoryViewModel: InventoryViewModel,
    private val inventoryFragment: InventoryFragment,
    private val lifecycleOwner: LifecycleOwner
) : DelegateAdapter<Note, NoteAdapter.NoteViewHolder, InventoryViewModel>()  {


    override fun onCreateViewHolder(parent: ViewGroup): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_note, parent, false)
        return NoteViewHolder(view, inventoryViewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, item: Note) {
        holder.bind(item, inventoryViewModel, inventoryFragment)
    }

    class NoteViewHolder(
        itemView: View,
        inventoryViewModel: InventoryViewModel,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView){
        val viewBinding = MaketInventoryNoteBinding.bind(itemView)

        init {
            inventoryViewModel.noteVisibility.observe(lifecycleOwner){
                viewBinding.editNote.isVisible = it
            }

            inventoryViewModel.note.observe(lifecycleOwner){
                viewBinding.editNote.setText(it)
            }
        }
        fun bind(
            item: Note,
            inventoryViewModel: InventoryViewModel,
            inventoryFragment: InventoryFragment
        ) {
            val idPlayer = Player.getInstance().getIdActivePlayer()
            inventoryViewModel.getNotePlayer(idPlayer)
            viewBinding.apply {
                inventoryViewModel.getVisibleView(item.name)
                tvNote.setOnClickListener { inventoryViewModel.updateVisibleView(item.name) }

                editNote.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        // Ничего не делаем здесь, так как мы хотим сохранить значение только после завершения ввода
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                        // Этот метод вызывается до изменения текста
                    }
                    override fun afterTextChanged(s: Editable) {
                        val enteredText = s.toString()
                        // Сохраняем значение введенного текста в базу данных, используя Room Persistence Library
                        inventoryViewModel.updateNotePlayer(NoteDbEntity(
                            idPlayer,
                            enteredText
                        ))
                    }
                })
            }

        }

    }
}