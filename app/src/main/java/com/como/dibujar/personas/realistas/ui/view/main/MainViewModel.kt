package com.como.dibujar.personas.realistas.ui.view.main

import androidx.lifecycle.viewModelScope
import com.como.dibujar.personas.realistas.ui.base.BaseViewModel
import com.como.dibujar.personas.realistas.ui.common.ADMOB
import com.como.dibujar.personas.realistas.ui.common.BANNER
import com.como.dibujar.personas.realistas.ui.common.SHOW_BANNER
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    sealed class Event {
        object SetUp : Event()
        data class ShowBanner(val isVisible: Boolean): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    private var db = Firebase.firestore
    private var banner = false

    //region ViewModel Input
    fun initFlow() {
        doAction(Event.SetUp)
        getAdmobBanner()
    }

     private fun getAdmobBanner() {
        viewModelScope.launch {
            val maximum = db.collection(ADMOB).document(BANNER)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get(SHOW_BANNER) != null){
                            banner = document.data?.get(SHOW_BANNER) as Boolean
                        }
                        doAction(Event.ShowBanner(banner))
                    }
                }
        }
    }
    //endregion

    //region ViewModel Output
    private fun doAction(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
    //endregion
}