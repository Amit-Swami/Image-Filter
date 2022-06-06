package com.example.imagefilter.Interface

import android.graphics.Typeface

interface AddTextFragmentListener {
    fun onAddTextListener(typeface:Typeface,text:String,color:Int)
}