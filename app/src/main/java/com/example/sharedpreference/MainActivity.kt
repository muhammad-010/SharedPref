package com.example.sharedpreference

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sharedpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private val usernameData = "admin"
    private val passwordData = "12345"
    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        checkLoginStatus()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        with(binding){
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(this@MainActivity, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    if(username == usernameData && password == passwordData){
                        prefManager.setLoggedIn(true)
                        prefManager.saveUsername(username)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(this@MainActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                checkLoginStatus()
            }

            btnClear.setOnClickListener {
                prefManager.clear()
                checkLoginStatus()
            }

            btnNotif.setOnClickListener {
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                }
                else {
                    0
                }
                val intent = Intent(this@MainActivity, NotifReceiver::class.java).apply {
                    putExtra("ACTION", "LOGOUT")
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    this@MainActivity,
                    0,
                    intent,
                    flag
                )

                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifikasi Logout")
                    .setContentText("Anda dapat mengklik tombol \"Logout\" dibawah ini untuk melakukan logout.")
                    .setAutoCancel((true))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(0, "Logout", pendingIntent)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    val notificationChannel = NotificationChannel(
                        channelId,
                        "Notifikasi Logout",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    with(notificationManager){
                        createNotificationChannel((notificationChannel))
                        notify(notifId, builder.build())
                    }
                } else {
                    notificationManager.notify(notifId, builder.build())
                }
            }

            btnUpdate.setOnClickListener {
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                }
                else {
                    0
                }
                val intent = Intent(this@MainActivity, MainActivity::class.java).apply {
                    putExtra("ACTION", "LOGOUT")
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    this@MainActivity,
                    0,
                    intent,
                    flag
                )
                val notifImage = BitmapFactory.decodeResource(resources,
                    R.drawable.img)
                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifikasi Logout")
                    .setContentText("Anda dapat mengklik tombol \"Logout\" dibawah ini untuk melakukan logout.")
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(notifImage)
                    )
                    .setAutoCancel((true))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(0, "Logout", pendingIntent)
                with(notificationManager){
                    notify(notifId, builder.build())
                }
            }


        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkLoginStatus(){
        val isLoggedIn = prefManager.isLoggedIn()
        if(isLoggedIn){
            binding.llLogin.visibility = View.GONE
            binding.llLogged.visibility = View.VISIBLE
        } else {
            binding.llLogin.visibility = View.VISIBLE
            binding.llLogged.visibility = View.GONE
        }
    }
}