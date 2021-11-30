package com.zahra.chatapp

import android.content.BroadcastReceiver
import android.os.Bundle
import android.util.Log
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {

    private lateinit var adapterChat: AdapterChat
    val TAG = "MainActivity"

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val receivedMessage = intent.getStringExtra("message")
            val newData = adapterChat.data.toMutableList()
            newData.add(receivedMessage as String)
            adapterChat.data = newData.toList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, IntentFilter("message"))
        setContentView(R.layout.activity_main)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapterChat = AdapterChat()
        recyclerView.adapter = adapterChat

        val etToken = findViewById<EditText>(R.id.etToken)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            etToken.setText(it)
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val btnSend = findViewById<Button>(R.id.btnSend)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etMessage = findViewById<EditText>(R.id.etMessage)

        btnSend.setOnClickListener {
            val title = etTitle.text.toString()
            val message = etMessage.text.toString()
            val recipientToken = etToken.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}