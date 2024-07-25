package com.example.websocketclientsample.interfaces

import com.google.gson.JsonObject
import java.net.Socket

interface MessageListener {
    fun onMessageJsonReceived(jsonObject: JsonObject)
}