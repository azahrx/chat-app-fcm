package com.zahra.chatapp

import android.os.Bundle
import android.util.Log
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        // dbYBben_RR6VKGDZJwtcfg:APA91bGO94fvK6gw7CWb2swzd8pv1wHwBBAJBC7D-dQvkmqXEt_JzpGjntJAFyH5Iu50_0fIBV0mdjsNCf2qjc5YDPAyFWijg7mXSE_WfNaKT9fjeJ9VPxDVmntlU4xUzJfvLiYDL2_E
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