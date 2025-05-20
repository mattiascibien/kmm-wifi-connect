package net.mattiascibien.wificonnect

import platform.NetworkExtension.NEHotspotConfiguration
import platform.NetworkExtension.NEHotspotConfigurationManager
import platform.NetworkExtension.NEHotspotNetwork
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("unused")
actual suspend fun connectToWifi(ssid: String, type: WiFiType, password: String?) : Boolean {
    return suspendCoroutine { continuation ->
        val config = if (password == null)
            NEHotspotConfiguration(sSID = ssid)
        else NEHotspotConfiguration(sSID = ssid, passphrase = password, isWEP = type == WiFiType.Wep)

        NEHotspotConfigurationManager.sharedManager.applyConfiguration(config) { err ->
            if(err != null)
                continuation.resumeWithException(Exception(err.localizedDescription))
            else {
                NEHotspotNetwork.fetchCurrentWithCompletionHandler { network ->
                    if(network?.SSID == ssid)
                        continuation.resume(true)
                    else
                        continuation.resume(false)
                }
            }
        }
    }
}