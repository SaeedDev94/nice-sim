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
        fun set() {
            val keys = entries
            val cmd = keys.joinToString(" && ") { "setprop ${it.key} ${it.value}" }
            Shell.cmd(cmd).exec()
            Shell.cmd("cmd wifi force-country-code enabled US")
        }
    }
}
