package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ink.itwo.android.common.ktx.log
import ink.itwo.android.coroutines.HttpManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView?.setOnClickListener {
            aa()
        }
    }

    private fun aa() {
        GlobalScope.launch {
//           var result= withContext(context = Dispatchers.Main){
//                requestCoroutinesPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            }
//            result.log()
            var resultUser = withContext(Dispatchers.IO) {
                HttpManager.instance.create(API::class.java).userInfo()
            }
            resultUser.log()
            resultUser.log()
        }
    }

}
