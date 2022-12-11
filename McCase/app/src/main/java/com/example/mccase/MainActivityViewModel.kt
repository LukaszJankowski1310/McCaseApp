package com.example.mccase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    var code = MutableLiveData<Int>()
    var nrCase : String = "";
 //   var caseOpen = false
    init {
        code.value = 0
    }

    fun randomCode() {
        code.value = (1000..9999).random();
    }

    fun setUpNrCase(value : String) {
        nrCase = value;
    }

}