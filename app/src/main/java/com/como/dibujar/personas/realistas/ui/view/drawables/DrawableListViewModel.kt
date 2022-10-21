package com.como.dibujar.personas.realistas.ui.view.drawables

import androidx.lifecycle.viewModelScope
import com.como.dibujar.personas.realistas.model.MainImage
import com.como.dibujar.personas.realistas.ui.base.BaseViewModel
import com.como.dibujar.personas.realistas.ui.common.DRAWING
import com.como.dibujar.personas.realistas.ui.common.FACE
import com.como.dibujar.personas.realistas.ui.common.ID_IMAGE
import com.como.dibujar.personas.realistas.ui.common.MAIN_IMAGE
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
        data class ShowList(val imagesData: List<MainImage>): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    private var db = Firebase.firestore
    private var number = 3
    private var mainImages = mutableListOf<MainImage>()

    //region ViewModel Input
    fun initFlow() {
        doAction(Event.SetUp)
        getFaceMainImage()
    }

    private fun getFaceMainImage() {
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            for (i in 1..number) {
                db.collection(FACE).document(DRAWING + "$i")
                    .get()
                    .addOnSuccessListener { document ->
                        document?.let {
                            if (document.data?.get(MAIN_IMAGE) != null) {
                                val mainImage = document.data?.get(MAIN_IMAGE) as String
                                val id = document.data?.get(ID_IMAGE) as Long
                                mainImages.add(MainImage(id.toInt(), mainImage))
                            }
                        }
                    }
            }
            doAction(Event.ShowList(mainImages))
            doAction(Event.ShowLoad(false))
        }

    }

    fun didClickAllButton() {}
    fun didClickBodyButton() {}
    fun didClickFaceButton() {}
    fun didClickHandButton() {}


    /* private fun getFaceImage() {
         viewModelScope.launch {
            // doAction(Event.ShowLoad(true))
             db.collection("face").document("drawing1")
             .get()
                 .addOnSuccessListener { document ->
                     document?.let {
                         if (document.data?.get("images") != null ) {
                              val images = document.data?.get("images") as List<String>
                             doAction(Event.ShowList(images))
                         }
                     }
                 }
         }

     }

     */

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