package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ink.itwo.android.coroutines.permissions.requestCoroutinesPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView?.setOnClickListener {
            aa()
        }
    }

    private fun aa() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = requestCoroutinesPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

}
