package io.github.saeeddev94.nicesim

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.materialswitch.MaterialSwitch
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val prefs by lazy { Prefs(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialSwitch>(R.id.simNumeric).apply {
            isChecked = prefs.simNumeric
            setOnCheckedChangeListener { _, isChecked -> prefs.simNumeric = isChecked }
        }
        findViewById<LinearLayout>(R.id.wifiCountry).apply {
            val countries = Prefs.WIFI_COUNTRIES
            val switches = countries.map { country ->
                MaterialSwitch(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    )
                    text = country
                    isChecked = country == prefs.wifiCountry
                }.also { addView(it) }
            }
            switches.forEachIndexed { index, switch ->
                val country = countries[index]
                switch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        prefs.wifiCountry = country
                        switches.filter { it !== switch }.forEach { it.isChecked = false }
                    } else if (country == prefs.wifiCountry) {
                        // keep exactly one option selected, like a radio group
                        switch.isChecked = true
                    }
                }
            }
        }
        findViewById<MaterialSwitch>(R.id.applyOnBoot).apply {
            isChecked = prefs.applyOnBoot
            setOnCheckedChangeListener { _, isChecked -> prefs.applyOnBoot = isChecked }
        }
        findViewById<Button>(R.id.get).setOnClickListener {
            getProps()
        }
        findViewById<Button>(R.id.set).setOnClickListener {
            setProps()
        }
    }

    override fun onResume() {
        super.onResume()
        getProps()
    }

    private fun getProps(toast: Boolean = true) {
        lifecycleScope.launch {
            val keys = Operator.entries
            val cmd = keys.joinToString(" && ") { "getprop ${it.key}" }
            val result = Shell.cmd(cmd).exec()
            val wifiCountry = Shell.cmd("cmd wifi get-country-code").exec()
                .out.firstOrNull()?.substringAfter("=")?.trim().orEmpty()
            withContext(Dispatchers.Main) {
                val status = findViewById<TextView>(R.id.status)
                status.text = keys
                    .mapIndexed { index, operator -> "${operator.key}: ${result.out[index]}" }
                    .plus("wifi.country-code: $wifiCountry")
                    .joinToString("\n")
                if (toast) {
                    Toast.makeText(
                        applicationContext, "Resolved", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setProps() {
        lifecycleScope.launch {
            Operator.set(prefs)
            withContext(Dispatchers.Main) {
                getProps(false)
                Toast.makeText(
                    applicationContext, "Done!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
