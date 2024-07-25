package com.example.websocketclientsample.helper

import android.view.View

interface AlertDialogInterface {
    fun onYesClick() {}
    fun onNoClick() {}
    fun onConnectionConfirm(ipAddress: String,port: String,code: String,) {}
}