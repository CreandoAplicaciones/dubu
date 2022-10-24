package com.como.dibujar.personas.realistas.ui.common.save

import android.content.Context

class Prefs (val context: Context) {

    private val SHARED_NAME="Mydbt"
    private val SHARED_CLICK="click"
    private val SHARED_DAY="day"

    val storage=context.getSharedPreferences(SHARED_NAME,0)

    fun saveDay(dayNumber: Int){
        storage.edit().putInt(SHARED_DAY,dayNumber).apply()
    }

    fun getDay():Int{
        return  storage.getInt(SHARED_DAY,1)
    }

    fun saveClick(numberClick: Long){
        storage.edit().putLong(SHARED_CLICK, numberClick).apply()
    }

    fun getClick():Long{
        return  storage.getLong(SHARED_CLICK, 10)
    }
}