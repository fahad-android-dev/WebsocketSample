package com.example.websocketclientsample.mvvm.main.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.example.websocketclientsample.helper.WebSocketClient
import com.example.websocketclientsample.interfaces.MessageListener
import com.example.websocketclientsample.mvvm.comfirmation.model.ConfirmationDataModel
import com.example.websocketclientsample.mvvm.comfirmation.model.Receipt
import com.example.websocketclientsample.mvvm.comfirmation.model.TransactionData
import com.example.websocketclientsample.mvvm.main.model.ClientDataModel
import com.google.gson.Gson
import java.io.OutputStream
import java.net.Socket

class MainViewModel : ViewModel() , MessageListener {


    var isConnected: Boolean = false

    var webSocketClient: WebSocketClient? = null
    var confirmationDataModel: JsonObject? = null

    fun connectWebSocket(ipAddress: String, port: String, code: String) {
        webSocketClient = WebSocketClient("ws://$ipAddress:$port",this)
        webSocketClient?.connect()

        println("Here is websocket connected")

        isConnected = true
        val jsonObject = JsonObject()
        jsonObject.addProperty("code", code)
        jsonObject.addProperty("amount", "")

        if (isConnected) {
            webSocketClient?.sendMessage(jsonObject)
            Log.d("WebSocketViewModel", "Message sent: $jsonObject")
        } else {
            Log.e("WebSocketViewModel", "WebSocket is not connected, cannot send message.")
        }

    }

    fun sendMessage(message: JsonObject) {
        webSocketClient?.sendMessage(message)
    }


    override fun onCleared() {
        super.onCleared()
        isConnected = false
        webSocketClient?.disconnect() // Clean up WebSocket connection
    }

    override fun onMessageJsonReceived(jsonObject: JsonObject) {

        println("here is msg $jsonObject")

        /*val model = ConfirmationDataModel(
            transactionData = TransactionData(
                isNewTransaction = jsonObject.get("isNewTransaction")?.asBoolean ?: false,
                receipts = listOf(Receipt(
                    action_code = jsonObject.get("action_code")?.asString ?: "",
                    amount_authorized = null,
                    amount_other = null,
                    application_cryptogram = null,
                    application_identifier = null,
                    approval_code = null,
                    card_expiration = null,
                    card_scheme = null,
                    card_scheme_sponsor = null,
                    cardholader_verfication_result = null,
                    created_at = null,
                    cryptogram_information_data = null,
                    currency = null,
                    customer_reference_number = null,
                    end_date = null,
                    end_time = null,
                    entry_mode = null,
                    is_approved = null,
                    is_refunded = null,
                    is_reversed = null,
                    kernel_id = null,
                    merchant = null,
                    pan = null,
                    pan_suffix = null,
                    payment_account_reference = null,
                    pos_software_version_number = null,
                    qr_code = null,
                    receipt_id = null,
                    receipt_line_one = null,
                    receipt_line_two = null,
                    retrieval_reference_number = null,
                    save_receipt_message = null,
                    start_date = null,
                    start_time = null,
                    status_message = null,
                    system_trace_audit_number = null,
                    terminal_verification_result = null,
                    thanks_message = null,
                    tid = null,
                    transaction_state_information = null,
                    transaction_type = null,
                    transaction_uuid = null,
                    updated_at = null,
                    verification_method = null
                ))

            )

        )*/
        confirmationDataModel = jsonObject
        println("here is model 2222 $confirmationDataModel")

       /* if (!jsonObject.get("status_message").asString.isNullOrEmpty()){
            confirmationDataModel = ConfirmationDataModel(
                transactionData = TransactionData(
                    isNewTransaction = false,
                    receipts = listOf()
                ), status_message = jsonObject.get("status_message").asString

            )
        }else {

        }*/

    }

    private fun parseJsonToModel(jsonString: String): TransactionData? {
        return try {
            val gson = Gson()
            gson.fromJson(jsonString, TransactionData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}