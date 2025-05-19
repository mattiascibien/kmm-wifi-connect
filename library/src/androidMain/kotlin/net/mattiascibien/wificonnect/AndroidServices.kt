package net.mattiascibien.wificonnect

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.startup.Initializer

interface AndroidServices

internal class AndroidServicesImpl(val wifiManager: WifiManager, val connectivityManager: ConnectivityManager) :
    AndroidServices {
    companion object {
        internal lateinit var services: AndroidServicesImpl

        fun initialize(context: Context) : AndroidServicesImpl {
            services = AndroidServicesImpl(
                context.getSystemService(Context.WIFI_SERVICE) as WifiManager,
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            )
            return services
        }

        fun getInstance() : AndroidServicesImpl {
            if (!Companion::services.isInitialized) {
                throw Exception("AndroidServices not initialized. You might have forgot to call initialize(ctx: Context) ")
            }

            return services
        }
    }
}

/**
 * Initializes the services required by the library using AndroidX App Startup
 */
class AndroidServicesInitializer : Initializer<AndroidServices> {
    override fun create(context: Context): AndroidServices {
        return AndroidServicesImpl.initialize(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return emptyList<Class<out Initializer<*>>>().toMutableList()
    }
}