package com.example.desarrollador.museo_ar.activities

import android.app.Application
import com.example.desarrollador.museo_ar.NotificationsManager
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

class MyApplication : Application() {

    val cloudCredentials = EstimoteCloudCredentials("jesus-iesoluciones-com-s-n-k1j", "b4d298efeae630b45ab8d342ed211965")

    fun enableBeaconNotifications() {
        val notificationsManager = NotificationsManager(this)
        notificationsManager.startMonitoring()
    }
}
