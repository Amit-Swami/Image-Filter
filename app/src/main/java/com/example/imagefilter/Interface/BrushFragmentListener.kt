package com.example.imagefilter.Interface

interface BrushFragmentListener {
    fun onBrushSizeChangedListener(size:Float)
    fun onBrushOpacityChangedListener(size:Int)
    fun onBrushColorChangedListener(color:Int)
    fun onBrushStateChangedListener(isEraser:Boolean)
}