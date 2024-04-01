package com.example.sumrak.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sumrak.Data.playerdb.PlayerViewModel
import com.example.sumrak.Lists.DataPlayer
import com.example.sumrak.Player




class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

}