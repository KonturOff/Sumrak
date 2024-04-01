package com.example.sumrak

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sumrak.Data.playerdb.PlayerDbEntity
import com.example.sumrak.Data.playerdb.PlayerViewModel
import com.example.sumrak.Lists.HistoryRoll
import com.example.sumrak.Lists.HistoryRollManager
import com.example.sumrak.Sound.Sound
import com.example.sumrak.databinding.ActivityMainBinding
import com.example.sumrak.ui.battle.BattleFragment
import com.example.sumrak.ui.calculator.MainCalculatorFragment
import com.example.sumrak.ui.calculator.calculator.CalculatorFragment
import com.example.sumrak.ui.calculator.historyRoll.HirstoryCalculatorFragment
import com.example.sumrak.ui.home.HomeFragment
import com.example.sumrak.ui.inventory.InventoryFragment
import com.example.sumrak.ui.other_fragments.Result_roll
import com.example.sumrak.ui.profile.AddPlayer
import com.example.sumrak.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity :
    AppCompatActivity(),
    CalculatorFragment.Interface,
    MainCalculatorFragment.Interface,
    HirstoryCalculatorFragment.Interface,
    ProfileFragment.Interface,
    HomeFragment.Interface,
    BattleFragment.Interface,
    InventoryFragment.Interface,
    AddPlayer.Interface,
    View.OnTouchListener,
    Result_roll.InterfaceResulRoll {

    private lateinit var b: ActivityMainBinding
    private lateinit var playerViewModel : PlayerViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        Sound.getInstance().loadSound(this)
        load()


        val navView: BottomNavigationView = b.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        // получаем информацию о том какой раздел открыт, при необходимости скрываем Player Bar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainCalculatorFragment -> {
                    //b.playerBar.visibility = View.GONE
                    b.leftPlayer.visibility = View.GONE
                    b.rightPlayer.visibility = View.GONE
                    b.namePlayer.text = Player.getInstance().getRandomText()
                }
                else -> {
                    b.playerBar.visibility = View.VISIBLE
                    if (Player.getInstance().getPlayerCount()>1){
                        b.leftPlayer.visibility = View.VISIBLE
                        b.rightPlayer.visibility = View.VISIBLE
                    }

                }
            }
        }
        if (!isDataSettings()){
            navController.navigate(R.id.addPlayer)

        }
       // Обрабатывает нажатия на панели навигации
        navView.setOnItemSelectedListener {Menuitem ->
            when (Menuitem.itemId){
                R.id.mainCalculatorFragment ->{
                    navController.navigate(R.id.mainCalculatorFragment)
                }
                R.id.navigation_inventory ->{
                    navController.navigate(R.id.navigation_inventory)
                    b.namePlayer.text = Player.getInstance().getNamePlayer()
                }
                R.id.navigation_home ->{
                    navController.navigate(R.id.navigation_home)
                    b.namePlayer.text = Player.getInstance().getNamePlayer()
                }
                R.id.navigation_battle ->{
                    navController.navigate(R.id.navigation_battle)
                    b.namePlayer.text = Player.getInstance().getNamePlayer()
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    b.namePlayer.text = Player.getInstance().getNamePlayer()
                }
            }
            true
        }
        // Обрабатываем повторные нажатия на панели навигации
        navView.setOnItemReselectedListener { item->
            when(item.itemId){
                R.id.navigation_profile ->{navController.navigate(R.id.navigation_profile)}
            }
        }

        //Тут получаем актуальные данные по Активному id игрока (лучше ничего не придумал)
        playerViewModel.readSettings.asLiveData().observe(this){list->
            list.forEach {
                Player.getInstance().activPosPlayer = Player.getInstance().getPosPlayer(it.value)
                playerViewModel.getPlayerVariableToId(it.value)
            }
            navController.currentDestination?.id?.let { it1 -> navController.navigate(it1)
                if (Player.getInstance().getPlayerCount()!=0){
                    b.namePlayer.text = Player.getInstance().getNamePlayer()

                }
            }
            countPlayer()
        }



        b.rightPlayer.setOnClickListener {
            playerViewModel.changeActivePlayer(1)
        }
        b.leftPlayer.setOnClickListener {
            playerViewModel.changeActivePlayer(-1)
        }



    }

    private fun isDataSettings(): Boolean {
        return playerViewModel.getCountSettings() != 0
    }

    private fun countPlayer() {
        b.navView.visibility = View.VISIBLE
        if (Player.getInstance().getPlayerCount() == 1){
            b.playerBar.visibility = View.VISIBLE
            b.leftPlayer.visibility = View.GONE
            b.rightPlayer.visibility = View.GONE
        }
        else if (Player.getInstance().getPlayerCount() ==0){
            b.playerBar.visibility = View.GONE
            b.navView.visibility = View.GONE
        }
        else{
            b.leftPlayer.visibility = View.VISIBLE
            b.rightPlayer.visibility = View.VISIBLE
        }

    }


    private fun load() {
        PlayerViewModel.getInstance(application).loadData()
        //PlayerViewModel.getInstance(application).loadSettings()
    }


    override fun get_result_roll(cube: String, player: Int, mode : String, bonus: Int, position: Int?) {
        val Result_roll = Result_roll()
        remove_fragment_roll()
        lateinit var result : ArrayList<String>
        // Если передается позиция, значит это рерол, и мы меняем переданый результат на новый
        if (position != null){
            val get_reroll_cube = HistoryRollManager.getInstance().getItem(position)
            result = Player.roll_cube(get_reroll_cube.cube)
            var param = Player.getInstance().getPlayerParametr(player, mode)
            if (get_reroll_cube.mode == "Проверка Уклонения" || get_reroll_cube.mode == "Проверка Парирования"|| get_reroll_cube.mode.contains("Убывающий Тест") ){
                param = get_reroll_cube.parameter
            }
            HistoryRollManager.getInstance().updateItem(position, HistoryRoll(result[0],result[1],result[2],result[3], player, mode, param, get_reroll_cube.value, get_reroll_cube.bonus))
        }
        //Если позиция не передана, значит это обычный бросок, записываем результат как новый
        else {
            var value = 0
            result = Player.roll_cube(cube)
            if (mode == "Проверка Инициативы") {
                when (result[1].toInt()) {
                    1 -> value = 8
                    20 -> value = 2
                    else -> value = 4
                }
            }
            var param = Player.getInstance().getPlayerParametr(player, mode)
            if (mode== "Проверка Уклонения"){
                param = Player.getInstance().playerEntity.dodge
            }
            if (mode == "Проверка Парирования"){
                param = Player.getInstance().playerEntity.parrying
            }
            if (mode.contains("Убывающий Тест")){
                param = bonus
            }

            HistoryRollManager.getInstance().addItem(HistoryRoll(result[0],result[1],result[2],result[3], player, mode, param, value,0))
        }

        Sound.getInstance().playSound(result[0], result[1])

        val bundle = Bundle()

        bundle.putString("cube", result[0])
        bundle.putString("result_roll", result[1])
        bundle.putString("text_roll", result[2])
        bundle.putString("color", result[3])
        bundle.putString("mode", mode)
        if (position!=null){
            bundle.putInt("position", position)
        }
        else{bundle.putInt("position", 0)}



        Result_roll.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner_fragment_roll, Result_roll)
            .commit()

    }



    override fun touch_screen() {
        remove_fragment_roll()

    }


    // Наверное нахуй не нужна
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
                remove_fragment_roll()
            }
        }
        return true
    }

    //Проверяем есть ли активный фрагмент в контейнере, если есть - удаляем
    private fun remove_fragment_roll(){
        val oldFragment = supportFragmentManager.findFragmentById(R.id.conteiner_fragment_roll)
        oldFragment?.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
    }

    //Убираем Бар при входе в редактор персонажа
    override fun edit_player() {
        b.playerBar.visibility = View.GONE
    }



    override fun save_player(player: PlayerDbEntity) {
        playerViewModel.addPlayer(player)
    }




}
