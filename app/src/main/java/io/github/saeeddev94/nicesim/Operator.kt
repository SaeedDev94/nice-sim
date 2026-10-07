package io.github.saeeddev94.nicesim

import com.topjohnwu.superuser.Shell

enum class Operator(val key: String, val value: String) {
    Alpha("gsm.operator.alpha", "T-Mobile,T-Mobile"),
    IsoCountry("gsm.operator.iso-country", "us,us"),
    Numeric("gsm.operator.numeric", "310260,310260"),

    SimAlpha("gsm.sim.operator.alpha", "T-Mobile,T-Mobile"),
    SimIsoCountry("gsm.sim.operator.iso-country", "us,us"),
    SimNumeric("gsm.sim.operator.numeric", "310260,310260");

    companion object {
        fun set(prefs: Prefs) {
            buildList {
                if (prefs.simNumeric) addAll(entries.map { "setprop ${it.key} ${it.value}" })
                if (prefs.wifiCountryCode) add("cmd wifi force-country-code enabled US")
            }.takeIf { it.isNotEmpty() }?.joinToString(" && ")?.let { cmd ->
                Shell.cmd(cmd).exec()
            }
        }
    }
}
