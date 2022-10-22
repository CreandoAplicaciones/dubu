package com.como.dibujar.personas.realistas.ui.view.steps

import androidx.lifecycle.viewModelScope
import com.como.dibujar.personas.realistas.R
import com.como.dibujar.personas.realistas.ui.base.BaseViewModel
import com.como.dibujar.personas.realistas.ui.common.DRAWING
import com.como.dibujar.personas.realistas.ui.common.FACE
import com.como.dibujar.personas.realistas.ui.common.IMAGES
import com.como.dibujar.personas.realistas.ui.common.NAME
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class StepsDrawingViewModel : BaseViewModel() {

    sealed class Event {
        object SetUp: Event()
        data class ShowImage(val image: String, val name: String): Event()
        data class ShowNumberImages(val numberImages: Int, val currentNumber: Int, val restId: Int): Event()
        data class ShowLoad(val isVisible: Boolean): Event()
        data class ShowButtonBack(val isVisible: Boolean): Event()
        data class ShowButtonNext(val isVisible: Boolean): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    private var db = Firebase.firestore
    private var imagesList = listOf<String>()
    private var name = ""
    private var numberMaxImages = 1
    private var currentNumberImage = 0


    //region ViewModel Input
    fun initFlow(id: Int) {
        doAction(Event.SetUp)
        getFaceImage(id)
    }

     private fun getFaceImage(id: Int) {
         viewModelScope.launch {
             doAction(Event.ShowLoad(true))
             db.collection(FACE).document(DRAWING + "$id")
             .get()
                 .addOnSuccessListener { document ->
                     document?.let {
                         if (document.data?.get(IMAGES) != null ) {
                             imagesList = document.data?.get(IMAGES) as List<String>
                         }
                         if (document.data?.get(NAME) != null) {
                             name = document.data?.get(NAME) as String
                         }
                             numberMaxImages = imagesList.size
                             doAction(Event.ShowImage(imagesList[currentNumberImage], name))
                             doAction(Event.ShowNumberImages(numberMaxImages, (currentNumberImage+1), R.string.steps_drawing_number_images))

                         if(currentNumberImage == numberMaxImages) {
                                doAction(Event.ShowButtonNext(false))
                            }
                         }
                     }
             doAction(Event.ShowLoad(false))
         }

     }

    fun didOnClickButtonBack() {
        doAction(Event.ShowButtonNext(true))
        currentNumberImage -= 1
        if (currentNumberImage < 1) {
            doAction(Event.ShowButtonBack(false))
        }
        doAction(Event.ShowImage(imagesList[currentNumberImage], name))
        doAction(Event.ShowNumberImages(numberMaxImages, (currentNumberImage+1), R.string.steps_drawing_number_images))
    }

    fun didOnClickButtonNext() {
       doAction(Event.ShowButtonBack(true))
        currentNumberImage +=1
        if((currentNumberImage + 1) == numberMaxImages) {
            doAction(Event.ShowButtonNext(false))
        }
        doAction(Event.ShowImage(imagesList[currentNumberImage], name))
        doAction(Event.ShowNumberImages(numberMaxImages, (currentNumberImage+1), R.string.steps_drawing_number_images))
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