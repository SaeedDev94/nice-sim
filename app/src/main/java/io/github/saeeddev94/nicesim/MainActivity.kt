package io.github.saeeddev94.nicesim

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            withContext(Dispatchers.Main) {
                val status = findViewById<TextView>(R.id.status)
                status.text = keys
                    .mapIndexed { index, operator -> "${operator.key}: ${result.out[index]}" }
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
            Operator.set()
            withContext(Dispatchers.Main) {
                getProps(false)
                Toast.makeText(
                    applicationContext, "Done!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
