package net.mattiascibien.wificonnect

expect suspend fun connectToWifi(ssid: String, type: WiFiType, password: String? = null) : Boolean