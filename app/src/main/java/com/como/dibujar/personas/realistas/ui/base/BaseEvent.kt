package com.como.dibujar.personas.realistas.ui.base

sealed class BaseEvent {
    data class ShowMessage(val message: Int): BaseEvent()
    data class ShowLoading(val visibility: Boolean): BaseEvent()
}