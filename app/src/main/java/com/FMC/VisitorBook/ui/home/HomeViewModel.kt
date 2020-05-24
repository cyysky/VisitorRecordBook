package com.FMC.VisitorBook.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "VisitorRecordBook by FMC\n\nWelcome!"
    }
    val text: LiveData<String> = _text
}