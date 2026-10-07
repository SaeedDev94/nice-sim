package io.github.saeeddev94.nicesim

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var simNumeric: Boolean
        get() = prefs.getBoolean(SIM_NUMERIC, true)
        set(value) = prefs.edit { putBoolean(SIM_NUMERIC, value) }

    var wifiCountryCode: Boolean
        get() = prefs.getBoolean(WIFI_COUNTRY_CODE, true)
        set(value) = prefs.edit { putBoolean(WIFI_COUNTRY_CODE, value) }

    private companion object {
        const val SIM_NUMERIC = "sim_numeric"
        const val WIFI_COUNTRY_CODE = "wifi_country_code"
    }
}
