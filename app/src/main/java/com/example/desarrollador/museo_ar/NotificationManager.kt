package com.example.desarrollador.museo_ar

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.example.desarrollador.museo_ar.activities.MyApplication
import com.example.desarrollador.museo_ar.activities.PinturaInfoActivity

class NotificationsManager(private val context: Context) {


    private lateinit var pathSecciones: String
    private lateinit var pathPinturas : String
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val Seccion01 = buildNotification("Hello", "You're near your beacon")
    private val Seccion02 = buildNotification("Bye bye", "You've left the proximity of your beacon")

    private fun buildNotification(title: String, text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "content_channel", "Things near you", NotificationManager.IMPORTANCE_HIGH)
            )
        }

        val intent = Intent(context, PinturaInfoActivity::class.java)
        intent.putExtra("pathSecciones",pathSecciones)
        intent.putExtra("pathPinturas",pathPinturas)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)


        return NotificationCompat.Builder(context, "content_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    fun startMonitoring() {
        val notificationId = 1
        val proximityObserver = ProximityObserverBuilder(context, (context as MyApplication).cloudCredentials)
            .onError { throwable ->
                Log.e("app", "proximity observer error: $throwable")
            }
            .withBalancedPowerMode()
            .build()

        val zone = ProximityZoneBuilder()
            .forTag("jesus-iesoluciones-com-s-n-k1j")
            .inCustomRange(3.0)
            .onEnter {
                notificationManager.notify(notificationId, Seccion01)
            }
            .onExit {
                notificationManager.notify(notificationId, Seccion02)
            }
            .build()
        proximityObserver.startObserving(zone)

    }

}
