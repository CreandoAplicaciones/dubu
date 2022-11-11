package com.como.dibujar.personas.realistas.ui.view.drawables

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.como.dibujar.personas.realistas.R
import com.como.dibujar.personas.realistas.model.MainImage
import com.como.dibujar.personas.realistas.ui.base.BaseViewModel
import com.como.dibujar.personas.realistas.ui.common.*
import com.como.dibujar.personas.realistas.ui.common.save.Prefs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DrawableListViewModel : BaseViewModel() {

    sealed class Event {
        object SetUp: Event()
        object InitialInterstitial: Event()
        data class GoToStepsDrawing(val collection: String, val id: Int): Event()
        data class ShowLoad(val isVisibility: Boolean): Event()
        data class ShowListFace(val imagesData: List<MainImage>): Event()
        data class ShowListHand(val imagesData: List<MainImage>): Event()
        data class ShowListBody(val imagesData: List<MainImage>): Event()
        data class SelectedBody(val background: Int, val white: Int): Event()
        data class SelectedHand(val background: Int, val white: Int): Event()
        data class SelectedFace(val background: Int, val white: Int): Event()
        data class ShowInterstitial(val isVisible: Boolean): Event()
        data class ShowDialogRate(val isVisible: Boolean): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    private var db = Firebase.firestore
    private var countFace = 0
    private var countBody = 0
    private var countHand = 0
    private var mainImagesFace = arrayListOf<MainImage>()
    private var mainImagesHand = arrayListOf<MainImage>()
    private var mainImagesBody = arrayListOf<MainImage>()
    private var selected = FACE
    private var numberClick = 10L
    private var numberRate = 27L
    private var interstitial = false
    private var dialogRate = false
    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context

    //region ViewModel Input
    fun initFlow(cont: Context) {
        context = cont
        doAction(Event.SetUp)
        doAction(Event.InitialInterstitial)
        initSelected()
        numberClick = Prefs(context).getClick()
        numberRate = Prefs(context).getClickRate()
        getAdmobInterstitial()
        getRateIsTrue()
    }

    private fun getFaceMainImage() {
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            db.collection(FACE).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        for (snapshot in it) {
                            countFace++
                        }
                    }
                } else {
                    countFace = 1

                }
                for (i in 1..countFace) {
                    db.collection(FACE).document(DRAWING + "$i")
                        .get()
                        .addOnSuccessListener { document ->
                            document?.let {
                                var mainImage = ""
                                var id = 1L
                                var name = ""
                                var difficulty = 1L
                                if (document.data?.get(MAIN_IMAGE) != null) {
                                    mainImage = document.data?.get(MAIN_IMAGE) as String
                                }
                                if (document.data?.get(ID_IMAGE) != null) {
                                    id = document.data?.get(ID_IMAGE) as Long
                                }
                                if (document.data?.get(NAME) != null) {
                                    name = document.data?.get(NAME) as String
                                }
                                if (document.data?.get(DIFFICULTY) != null) {
                                    difficulty = document.data?.get(DIFFICULTY) as Long
                                }
                                mainImagesFace.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                                when (selected) {
                                    FACE -> doAction(Event.ShowListFace(mainImagesFace))
                                }
                            }
                        }
                }

                doAction(Event.ShowLoad(false))
            }
        }
    }

    private fun getBodyImage() {
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            db.collection(BODY).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        for (snapshot in it) {
                            countBody++
                        }
                    }
                } else {
                    countBody = 1

                }
                for (i in 1..countBody) {
                    db.collection(BODY).document(DRAWING + "$i")
                        .get()
                        .addOnSuccessListener { document ->
                            document?.let {
                                var mainImage = ""
                                var id = 1L
                                var name = ""
                                var difficulty = 1L
                                if (document.data?.get(MAIN_IMAGE) != null) {
                                    mainImage = document.data?.get(MAIN_IMAGE) as String
                                }
                                if (document.data?.get(ID_IMAGE) != null) {
                                    id = document.data?.get(ID_IMAGE) as Long
                                }
                                if (document.data?.get(NAME) != null) {
                                    name = document.data?.get(NAME) as String
                                }
                                if (document.data?.get(DIFFICULTY) != null) {
                                    difficulty = document.data?.get(DIFFICULTY) as Long
                                }
                                mainImagesBody.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                                 when (selected) {
                                     BODY -> doAction(Event.ShowListBody(mainImagesBody))
                                }
                            }
                        }
                }

                doAction(Event.ShowLoad(false))
            }
        }
    }

    private fun getHandImage() {
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            db.collection(HAND).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        for (snapshot in it) {
                            countHand++
                        }
                    }
                } else {
                    countHand = 1

                }
                for (i in 1..countHand) {
                    db.collection(HAND).document(DRAWING + "$i")
                        .get()
                        .addOnSuccessListener { document ->
                            document?.let {
                                var mainImage = ""
                                var id = 1L
                                var name = ""
                                var difficulty = 1L

                                if (document.data?.get(MAIN_IMAGE) != null) {
                                    mainImage = document.data?.get(MAIN_IMAGE) as String
                                }
                                if (document.data?.get(ID_IMAGE) != null) {
                                    id = document.data?.get(ID_IMAGE) as Long
                                }
                                if (document.data?.get(NAME) != null) {
                                    name = document.data?.get(NAME) as String
                                }
                                if (document.data?.get(DIFFICULTY) != null) {
                                    difficulty = document.data?.get(DIFFICULTY) as Long
                                }
                                mainImagesHand.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                                 when (selected) {
                                     HAND -> doAction(Event.ShowListHand(mainImagesHand))
                                }
                            }
                        }
                }

                doAction(Event.ShowLoad(false))
            }
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
    fun didClickBodyButton() {
        doAction(Event.SelectedBody(R.drawable.selected, R.color.white))
        doAction(Event.ShowListBody(mainImagesBody))
        selected = BODY
        subtractNumber()
    }
    fun didClickFaceButton() {
        doAction(Event.SelectedFace(R.drawable.selected, R.color.white))
        doAction(Event.ShowListFace(mainImagesFace))
        selected = FACE
        subtractNumber()
    }
    fun didClickHandButton() {
        doAction(Event.SelectedHand(R.drawable.selected, R.color.white))
        doAction(Event.ShowListHand(mainImagesHand))
        selected = HAND
        subtractNumber()
    }

    fun didSelectedImage(id: Int){
        doAction(Event.GoToStepsDrawing(selected, id))
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

    private fun initSelected() {
        if (mainImagesFace.isEmpty()) {
            getFaceMainImage()
            getBodyImage()
            getHandImage()
        } else {
            when(selected) {
                BODY -> {
                    doAction(Event.ShowListBody(mainImagesBody))
                    doAction(Event.SelectedBody(R.drawable.selected, R.color.white))

                }
                HAND -> {
                    doAction(Event.ShowListHand(mainImagesHand))
                    doAction(Event.SelectedHand(R.drawable.selected, R.color.white))

                }
                FACE -> {
                    doAction(Event.ShowListFace(mainImagesFace))
                    doAction(Event.SelectedFace(R.drawable.selected, R.color.white))
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