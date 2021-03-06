package com.example.imagefilter.Interface

interface EditImageFragmentListener {
    fun onBrightnessChanged(brightness:Int)
    fun onSaturationChanged(saturation:Float)
    fun onConstrantChanged(constrant:Float)
    fun onEditStarted()
    fun onEditCompleted()
}