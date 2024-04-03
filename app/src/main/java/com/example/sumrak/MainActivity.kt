package com.example.sumrak

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.data.playerdb.PlayerViewModel
import com.example.sumrak.lists.HistoryRoll
import com.example.sumrak.lists.HistoryRollManager
import com.example.sumrak.sound.Sound
import com.example.sumrak.databinding.ActivityMainBinding
import com.example.sumrak.ui.battle.BattleFragment
import com.example.sumrak.ui.calculator.MainCalculatorFragment
import com.example.sumrak.ui.calculator.calculator.CalculatorFragment
import com.example.sumrak.ui.calculator.historyRoll.HistoryCalculatorFragment
import com.example.sumrak.ui.home.HomeFragment
import com.example.sumrak.ui.inventory.InventoryFragment
import com.example.sumrak.ui.other_fragments.ResultRoll
import com.example.sumrak.ui.profile.AddPlayer
import com.example.sumrak.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity :
    AppCompatActivity(),
    CalculatorFragment.Interface,
    MainCalculatorFragment.Interface,
    HistoryCalculatorFragment.Interface,
    ProfileFragment.Interface,
    HomeFragment.Interface,
    BattleFragment.Interface,
    InventoryFragment.Interface,
    AddPlayer.Interface,
    View.OnTouchListener,
    ResultRoll.InterfaceResulRoll {

    private var viewBinding: ActivityMainBinding? = null
    private var playerViewModel : PlayerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        Sound.getInstance().loadSound(this)
        load()

        val navView: BottomNavigationView? = viewBinding?.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView?.setupWithNavController(navController)
        // получаем информацию о том какой раздел открыт, при необходимости скрываем Player Bar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainCalculatorFragment -> {
                    viewBinding?.apply {
                        //playerBar.isVisible = false
                        leftPlayer.isVisible = false
                        rightPlayer.isVisible = false
                        namePlayer.text = Player.getInstance().getRandomText()
                    }
                }
                else -> {
                    viewBinding?.apply {
                       playerBar.isVisible = true
                        if (Player.getInstance().getPlayerCount() > 1) {
                            leftPlayer.isVisible = true
                            rightPlayer.isVisible = true
                        }
                    }
                }
            }
        }
        if (!isDataSettings()){
            navController.navigate(R.id.addPlayer)

        }
       // Обрабатывает нажатия на панели навигации
        navView?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainCalculatorFragment ->{
                    navController.navigate(R.id.mainCalculatorFragment)
                }
                R.id.navigation_inventory ->{
                    navController.navigate(R.id.navigation_inventory)
                    viewBinding?.namePlayer?.text = Player.getInstance().getNamePlayer()
                }
                R.id.navigation_home ->{
                    navController.navigate(R.id.navigation_home)
                    viewBinding?.namePlayer?.text = Player.getInstance().getNamePlayer()
                }
                R.id.navigation_battle ->{
                    navController.navigate(R.id.navigation_battle)
                    viewBinding?.namePlayer?.text = Player.getInstance().getNamePlayer()
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    viewBinding?.namePlayer?.text = Player.getInstance().getNamePlayer()
                }
            }
            true
        }
        // Обрабатываем повторные нажатия на панели навигации
        navView?.setOnItemReselectedListener { item->
            when(item.itemId){
                R.id.navigation_profile ->{navController.navigate(R.id.navigation_profile)}
            }
        }

        //Тут получаем актуальные данные по Активному id игрока (лучше ничего не придумал)
        playerViewModel?.readSettings?.asLiveData()?.observe(this) { list->
            list.forEach {
                Player.getInstance().activePosPlayer = Player.getInstance().getPosPlayer(it.value)
                playerViewModel?.getPlayerVariableToId(it.value)
            }
            navController.currentDestination?.id?.let { it1 -> navController.navigate(it1)
                if (Player.getInstance().getPlayerCount()!=0){
                    viewBinding?.namePlayer?.text = Player.getInstance().getNamePlayer()

                }
            }
            countPlayer()
        }

        viewBinding?.apply {
            rightPlayer.setOnClickListener {
                playerViewModel?.changeActivePlayer(1)
            }
            leftPlayer.setOnClickListener {
                playerViewModel?.changeActivePlayer(-1)
            }
        }
    }

    private fun isDataSettings(): Boolean {
        return playerViewModel?.getCountSettings() != 0
    }

    private fun countPlayer() {
        viewBinding?.apply {
            navView.isVisible = true
            when(Player.getInstance().getPlayerCount()) {
                1 -> {
                    playerBar.isVisible = true
                    leftPlayer.isVisible = false
                    rightPlayer.isVisible = false
                }
                0 -> {
                    playerBar.isVisible = false
                    navView.isVisible = false
                }
                else -> {
                    leftPlayer.isVisible = true
                    rightPlayer.isVisible = true
                }
            }
        }
    }

    private fun load() {
        PlayerViewModel.getInstance(application).loadData()
        //PlayerViewModel.getInstance(application).loadSettings()
    }

    override fun get_result_roll(cube: String, player: Int, mode : String, bonus: Int, position: Int?) {
        val resultRoll = ResultRoll()
        removeFragmentRoll()
        var result : ArrayList<String>? = null
        // Если передается позиция, значит это рерол, и мы меняем переданый результат на новый
        if (position != null){
            val getRerollCube = HistoryRollManager.getInstance().getItem(position)
            result = Player.rollCube(getRerollCube.cube)
            var param = Player.getInstance().getPlayerParameter(player, mode)
            if (getRerollCube.mode == "Проверка Уклонения" || getRerollCube.mode == "Проверка Парирования"|| getRerollCube.mode.contains("Убывающий Тест") ){
                param = getRerollCube.parameter
            }
            HistoryRollManager.getInstance().updateItem(position, HistoryRoll(result[0],result[1],result[2],result[3], player, mode, param, getRerollCube.value, getRerollCube.bonus))
        }
        //Если позиция не передана, значит это обычный бросок, записываем результат как новый
        else {
            var value = 0
            result = Player.rollCube(cube)
            if (mode == "Проверка Инициативы") {
                when (result[1].toInt()) {
                    1 -> value = 8
                    20 -> value = 2
                    else -> value = 4
                }
            }
            var param = Player.getInstance().getPlayerParameter(player, mode)
            if (mode== "Проверка Уклонения"){
                param = Player.getInstance().playerEntity?.dodge ?: 0
            }
            if (mode == "Проверка Парирования"){
                param = Player.getInstance().playerEntity?.parrying ?: 0
            }
            if (mode.contains("Убывающий Тест")){
                param = bonus
            }

            HistoryRollManager.getInstance().addItem(
                HistoryRoll(
                    result[0],
                    result[1],
                    result[2],
                    result[3],
                    player,
                    mode,
                    param,
                    value,
                    0
                )
            )
        }

        Sound.getInstance().playSound(result[0], result[1])

        val bundle = Bundle()

        bundle.putString("cube", result[0])
        bundle.putString("result_roll", result[1])
        bundle.putString("text_roll", result[2])
        bundle.putString("color", result[3])
        bundle.putString("mode", mode)

        if (position!=null) {
            bundle.putInt("position", position)
        } else {
            bundle.putInt("position", 0)
        }

        resultRoll.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner_fragment_roll, resultRoll)
            .commit()

    }

    override fun touch_screen() {
        removeFragmentRoll()
    }


    // Наверное нахуй не нужна
    // TODO похоже, что реально нахуй не нужна
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // Обработка нажатия на экран
            }
            MotionEvent.ACTION_MOVE -> {
                // Обработка перемещения пальца по экрану
            }
            MotionEvent.ACTION_UP -> {
                // Обработка отпускания пальца с экрана
                // удаляем фрагмент "Результат броска"
                removeFragmentRoll()
            }
        }
        return true
    }

    //Проверяем есть ли активный фрагмент в контейнере, если есть - удаляем
    private fun removeFragmentRoll(){
        val oldFragment = supportFragmentManager.findFragmentById(R.id.conteiner_fragment_roll)
        oldFragment?.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
    }

    //Убираем Бар при входе в редактор персонажа
    override fun editPlayer() {
        viewBinding?.playerBar?.isVisible = false
    }



    override fun savePlayer(player: PlayerDbEntity) {
        playerViewModel?.addPlayer(player)
    }
}
