package com.example.sumrak.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sumrak.data.playerdb.PlayerViewModel
import com.example.sumrak.lists.HistoryRollManager
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var viewModel: ProfileViewModel? = null
    private var viewBinding: FragmentProfileBinding? = null
    private var myInterfaceProfile: Interface? = null
    private var navController: NavController? = null
    private var playerViewModel: PlayerViewModel? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceProfile = context as Interface
    }

    interface Interface{
        fun editPlayer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProfileBinding.inflate(layoutInflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Player.getInstance().getPlayerCount()==1){
            viewBinding?.deletePers?.isEnabled = false
        }
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        viewModel?.getProfileTextViewToPlayer(Player.getInstance().getIdActivePlayer())

        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        navController = Navigation.findNavController(view)

        val idPlayer = Player.getInstance().getIdActivePlayer()


        viewBinding?.apply {
            viewModel!!.playerLv.observe(viewLifecycleOwner) {
                editClassPers.setText(it.classPers)
                editRank.setText(it.rank)
                editBirthPlace.setText(it.bitchPlace)
                editMarkFate.setText(it.markFate)
                editSkills.setText(it.skills)
                editProfile.setText(it.profile)
                tvInfluence.text = it.influence.toString()
                tvXp.text = it.xp.toString()
            }

            viewModel?.xpLv?.observe(viewLifecycleOwner) {
                tvXp.text = it.toString()
                btnRemXp.isEnabled = it > 0
            }

            viewModel?.influenceLv?.observe(viewLifecycleOwner) {
                tvInfluence.text = it.toString()
                btnRemInfluence.isEnabled = it > 0
            }
            val soundArray = resources.getStringArray(R.array.SoundSpiner)
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, soundArray)
            spinnerSound.adapter = adapter

            val selectedIndex = soundArray.indexOf(Player.getInstance().getActivePlayer().sound)
            if (selectedIndex != -1) {
                spinnerSound.setSelection(selectedIndex)
            }

            spinnerSound.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val newlySelectedSound = soundArray[position]
                    viewModel?.updateSoundPlayer(idPlayer, newlySelectedSound)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Обработка ситуации, когда ничего не выбрано
                }
            }

            newPers.setOnClickListener {
                navController?.navigate(R.id.action_navigation_profile_to_addPlayer2)
                myInterfaceProfile?.editPlayer()
            }

            editPers.setOnClickListener {
                navController?.navigate(R.id.action_navigation_profile_to_updatePlayer)
                myInterfaceProfile?.editPlayer()
            }

            deletePers.setOnClickListener { deletePlayer() }

            btnAddInfluence.setOnClickListener {
                stepInfluence(
                    idPlayer,
                    tvInfluence.text.toString().toInt() + 1,
                    viewModel!!
                )
            }
            btnRemInfluence.setOnClickListener {
                stepInfluence(
                    idPlayer,
                    tvInfluence.text.toString().toInt() - 1,
                    viewModel!!
                )
            }

            btnAddXp.setOnClickListener {
                stepXp(
                    idPlayer,
                    tvXp.text.toString().toInt() + 1,
                    viewModel!!
                )
            }
            btnRemXp.setOnClickListener {
                stepXp(
                    idPlayer,
                    tvXp.text.toString().toInt() - 1,
                    viewModel!!
                )
            }

            saveDataWithTextWatcher(idPlayer, editRank) { it.toString() }
            saveDataWithTextWatcher(idPlayer, editClassPers) { it.toString() }
            saveDataWithTextWatcher(idPlayer, editProfile) { it.toString() }
            saveDataWithTextWatcher(idPlayer, editBirthPlace) { it.toString() }
            saveDataWithTextWatcher(idPlayer, editSkills) { it.toString() }
            saveDataWithTextWatcher(idPlayer, editMarkFate) { it.toString() }
        }


    }

    private fun stepInfluence(id: Int, influense: Int, viewModel: ProfileViewModel){
        viewModel.updateInfluencePlayer(id, influense)
    }

    private fun stepXp(id: Int, xp:Int, viewModel: ProfileViewModel){
        viewModel.updateXpPlayer(id, xp)
    }

    fun saveDataWithTextWatcher(idPlayer: Int, editText: EditText, getValue: (Editable) -> String) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Ничего не делаем здесь, так как мы хотим сохранить значение только после завершения ввода
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Этот метод вызывается до изменения текста
            }

            override fun afterTextChanged(s: Editable) {
                // Сохраняем значение введенного текста в базу данных, используя Room Persistence Library
                viewBinding?.apply {
                    viewModel?.saveDataProfilePlayer(
                        idPlayer,
                        editClassPers.text.toString(),
                        editRank.text.toString(),
                        editBirthPlace.text.toString(),
                        editMarkFate.text.toString(),
                        editSkills.text.toString(),
                        editProfile.text.toString()
                    )
                }
            }
        })
    }

    private fun deletePlayer() {
        val name = Player.getInstance().getActivePlayer().namePlayer
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Да") { dialog, which ->
            HistoryRollManager.getInstance().removeItemToIdPlayer(Player.getInstance().getIdActivePlayer())
            playerViewModel?.deletePlayer(Player.getInstance().getIdActivePlayer())
        }
        builder.setNegativeButton(
            "Нет"
        ) { dialog, which ->
            // Stuff to do
        }

        builder.setMessage("Внимание! В случае подтвержения персонаж $name будет удален\nВместе с персонажем будет удалено всё его снаряжение")
        builder.setTitle("Удалить персонажа $name?")

        val d = builder.create()
        d.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
