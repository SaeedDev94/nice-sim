package io.github.saeeddev94.nicesim

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var applyOnBoot: Boolean
        get() = prefs.getBoolean(APPLY_ON_BOOT, true)
        set(value) = prefs.edit { putBoolean(APPLY_ON_BOOT, value) }

    var simNumeric: Boolean
        get() = prefs.getBoolean(SIM_NUMERIC, true)
        set(value) = prefs.edit { putBoolean(SIM_NUMERIC, value) }

    var wifiCountryCode: Boolean
        get() = prefs.getBoolean(WIFI_COUNTRY_CODE, true)
        set(value) = prefs.edit { putBoolean(WIFI_COUNTRY_CODE, value) }

    var wifiCountry: String
        get() = prefs.getString(WIFI_COUNTRY, WIFI_COUNTRIES.first())!!
        set(value) = prefs.edit { putString(WIFI_COUNTRY, value) }

    companion object {
        val WIFI_COUNTRIES = listOf("US", "GB", "AU")

        private const val SIM_NUMERIC = "sim_numeric"
        private const val WIFI_COUNTRY_CODE = "wifi_country_code"
        private const val WIFI_COUNTRY = "wifi_country"
        private const val APPLY_ON_BOOT = "apply_on_boot"
    }
}
