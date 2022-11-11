package com.como.dibujar.personas.realistas.ui.view.steps

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.como.dibujar.personas.realistas.R
import com.como.dibujar.personas.realistas.ui.base.BaseViewModel
import com.como.dibujar.personas.realistas.ui.common.*
import com.como.dibujar.personas.realistas.ui.common.save.Prefs
import com.como.dibujar.personas.realistas.ui.view.drawables.DrawableListViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class StepsDrawingViewModel : BaseViewModel() {

    sealed class Event {
        object SetUp: Event()
        object InitialInterstitial: Event()
        data class ShowImage(val image: String, val name: String): Event()
        data class ShowNumberImages(val numberImages: Int, val currentNumber: Int, val restId: Int): Event()
        data class ShowLoad(val isVisible: Boolean): Event()
        data class ShowButtonBack(val isVisible: Boolean): Event()
        data class ShowButtonNext(val isVisible: Boolean): Event()
        data class ShowInterstitial(val isVisible: Boolean): Event()
        data class ShowDifficulty(val difficulty: Int): Event()
        data class ShowDialogRate(val isVisible: Boolean): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    private var db = Firebase.firestore
    private var imagesList = listOf<String>()
    private var name = ""
    private var numberMaxImages = 1
    private var difficulty = 1L
    private var currentNumberImage = 0
    private var numberClick = 10L
    private var numberRate = 27L
    private var interstitial = false
    private var dialogRate = false
    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context



    //region ViewModel Input
    fun initFlow(collection: String, id: Int, cont: Context) {
        context = cont
        doAction(Event.SetUp)
        doAction(Event.InitialInterstitial)
        getFaceImage(collection, id)
        numberClick = Prefs(context).getClick()
        numberRate = Prefs(context).getClickRate()
        getAdmobInterstitial()
        getRateIsTrue()
    }

     private fun getFaceImage(collection: String,id: Int) {
         viewModelScope.launch {
             doAction(Event.ShowLoad(true))
             db.collection(collection).document(DRAWING + "$id")
             .get()
                 .addOnSuccessListener { document ->
                     document?.let {
                         if (document.data?.get(IMAGES) != null ) {
                             imagesList = document.data?.get(IMAGES) as List<String>
                         }
                         if (document.data?.get(NAME) != null) {
                             name = document.data?.get(NAME) as String
                         }
                         if (document.data?.get(DIFFICULTY) != null) {
                         difficulty = document.data?.get(DIFFICULTY) as Long
                         }
                         numberMaxImages = imagesList.size
                         doAction(Event.ShowImage(imagesList[currentNumberImage], name))
                         doAction(Event.ShowNumberImages(numberMaxImages, (currentNumberImage+1), R.string.steps_drawing_number_images))
                         doAction(Event.ShowDifficulty(difficulty.toInt()))

                         if(currentNumberImage == numberMaxImages) {
                                doAction(Event.ShowButtonNext(false))
                            }
                         }
                     }
             doAction(Event.ShowLoad(false))
         }

     }

    private fun getAdmobInterstitial() {
        viewModelScope.launch {
            val maximum = db.collection(ADMOB).document(INTERSTICIAL)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get(SHOW_INTERSTICIAL) != null){
                            interstitial = document.data?.get(SHOW_INTERSTICIAL) as Boolean
                        }
                    }
                }
        }
    }

    private fun getNumberInterstitial() {
        viewModelScope.launch {
            val maximum = db.collection(ADMOB).document(INTERSTICIAL)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get(NUMBER_INTERSTITIAL) != null){
                            numberClick = document.data?.get(NUMBER_INTERSTITIAL) as Long
                            Prefs(context).saveClick(numberClick)
                        }
                    }
                }
        }
    }

    private fun getRateIsTrue() {
        viewModelScope.launch {
            val maximum = db.collection(ADMOB).document(RATE)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get(SHOW_DIALOG_RATE) != null){
                            dialogRate = document.data?.get(SHOW_DIALOG_RATE) as Boolean
                        }
                    }
                }
        }
    }

    private fun getNumberRater() {
        viewModelScope.launch {
            val maximum = db.collection(ADMOB).document(RATE)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get(NUMBER_DIALOG) != null){
                            numberRate = document.data?.get(NUMBER_DIALOG) as Long
                            Prefs(context).saveClickRate(numberRate)
                        }
                    }
                }
        }
    }

    fun showedInterstitial() {
        doAction(Event.InitialInterstitial)
    }

    fun didOnClickButtonBack() {
        doAction(Event.ShowButtonNext(true))
        currentNumberImage -= 1
        if (currentNumberImage < 1) {
            doAction(Event.ShowButtonBack(false))
        }
        doAction(Event.ShowImage(imagesList[currentNumberImage], name))
        doAction(Event.ShowNumberImages(numberMaxImages, (currentNumberImage+1), R.string.steps_drawing_number_images))
        subtractNumber()
    }

    fun didOnClickButtonNext() {
       doAction(Event.ShowButtonBack(true))
        currentNumberImage +=1
        if((currentNumberImage + 1) == numberMaxImages) {
            doAction(Event.ShowButtonNext(false))
        }
        doAction(Event.ShowImage(imagesList[currentNumberImage], name))
        doAction(Event.ShowNumberImages(numberMaxImages, (currentNumberImage+1), R.string.steps_drawing_number_images))
        subtractNumber()
    }

    private fun subtractNumber() {
        if (numberClick <= 0) {
            doAction(Event.ShowInterstitial(interstitial))
            getNumberInterstitial()
        } else {
            numberClick -= 1
            Prefs(context).saveClick(numberClick-1)
        }
        if (numberRate <= 0) {
            doAction(Event.ShowDialogRate(dialogRate))
            getNumberRater()
        } else {
            numberRate -= 1
            Prefs(context).saveClickRate(numberRate)
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