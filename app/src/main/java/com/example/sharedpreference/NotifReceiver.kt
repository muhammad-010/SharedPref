package com.example.sharedpreference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotifReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val msg = intent?.getStringExtra("MESSAGE")
        if (msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
        val action = intent?.getStringExtra("ACTION")
        if (action == "LOGOUT") {
            val prefManager = PrefManager.getInstance(context!!)
            prefManager.clear()

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)

            Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()
        }
    }
}