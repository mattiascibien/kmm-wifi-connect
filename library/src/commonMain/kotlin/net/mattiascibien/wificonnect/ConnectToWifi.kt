package net.mattiascibien.wificonnect

/**
 * Connects to the specified Wi-Fi Network
 *
 * @param ssid The network SSID
 * @param type The network type
 * @param password A password (optional for unsecured networks)
 */
expect suspend fun connectToWifi(ssid: String, type: WiFiType, password: String? = null) : Boolean