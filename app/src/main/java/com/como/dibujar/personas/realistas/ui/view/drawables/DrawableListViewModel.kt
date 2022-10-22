package com.como.dibujar.personas.realistas.ui.view.drawables

import androidx.lifecycle.viewModelScope
import com.como.dibujar.personas.realistas.R
import com.como.dibujar.personas.realistas.model.MainImage
import com.como.dibujar.personas.realistas.ui.base.BaseViewModel
import com.como.dibujar.personas.realistas.ui.common.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DrawableListViewModel : BaseViewModel() {

    sealed class Event {
        object SetUp: Event()
        data class GoToStepsDrawing(val id: Int): Event()
        data class ShowLoad(val isVisibility: Boolean): Event()
        data class ShowListFace(val imagesData: List<MainImage>): Event()
        data class ShowListAll(val imagesData: List<MainImage>): Event()
        data class ShowListHand(val imagesData: List<MainImage>): Event()
        data class ShowListBody(val imagesData: List<MainImage>): Event()
        data class SelectedBody(val background: Int, val white: Int): Event()
        data class SelectedAll(val background: Int, val white: Int): Event()
        data class SelectedHand(val background: Int, val white: Int): Event()
        data class SelectedFace(val background: Int, val white: Int): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    private var db = Firebase.firestore
    private var countFace = 0
    private var countBody = 0
    private var countHand = 0
    private var mainImagesFace = arrayListOf<MainImage>()
    private var mainImagesAll = arrayListOf<MainImage>()
    private var mainImagesHand = arrayListOf<MainImage>()
    private var mainImagesBody = arrayListOf<MainImage>()
    private var selected = ALL

    //region ViewModel Input
    fun initFlow() {
        doAction(Event.SetUp)
        if (mainImagesFace.isEmpty()) {
            getFaceMainImage()
            getBodyImage()
            getHandImage()
        } else {
            when(selected) {
                ALL -> {
                    doAction(Event.ShowListAll(mainImagesAll))
                    doAction(Event.SelectedAll(R.drawable.selected, R.color.white))
                }
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
                                mainImagesAll.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                            }
                        }
                }
                when (selected) {
                    ALL -> doAction(Event.ShowListAll(mainImagesAll))
                    FACE -> doAction(Event.ShowListFace(mainImagesFace))
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
                                mainImagesFace.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                                mainImagesAll.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                            }
                        }
                }
                when (selected) {
                    ALL -> doAction(Event.ShowListAll(mainImagesAll))
                    BODY -> doAction(Event.ShowListBody(mainImagesBody))
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
                                mainImagesFace.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                                mainImagesAll.add(MainImage(id.toInt(), mainImage, name, difficulty.toInt()))
                            }
                        }
                }
                when (selected) {
                    ALL -> doAction(Event.ShowListAll(mainImagesAll))
                    HAND -> doAction(Event.ShowListHand(mainImagesHand))
                }
                doAction(Event.ShowLoad(false))
            }
        }
    }

    fun didClickAllButton() {
        doAction(Event.SelectedAll(R.drawable.selected, R.color.white))
        doAction(Event.ShowListAll(mainImagesAll))
        selected = ALL
    }
    fun didClickBodyButton() {
        doAction(Event.SelectedBody(R.drawable.selected, R.color.white))
        doAction(Event.ShowListBody(mainImagesBody))
        selected = BODY
    }
    fun didClickFaceButton() {
        doAction(Event.SelectedFace(R.drawable.selected, R.color.white))
        doAction(Event.ShowListFace(mainImagesFace))
        selected = FACE
    }
    fun didClickHandButton() {
        doAction(Event.SelectedHand(R.drawable.selected, R.color.white))
        doAction(Event.ShowListHand(mainImagesHand))
        selected = HAND
    }

    fun didSelectedImage(id: Int){
        doAction(Event.GoToStepsDrawing(id))

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