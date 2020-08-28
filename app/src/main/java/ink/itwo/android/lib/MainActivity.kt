package ink.itwo.android.lib

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView?.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
//            aa()
        }
    }
    private fun aa() {
       /* dsl {
            block {

                withTimeout(5000){
                    repeat(100){
                        it.toString().log()
                    }
                }

                var storagePermission = withContext(context = Dispatchers.Main) {
                    requestCoroutinesPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                storagePermission.log()
               var a= io { HttpManager.instance.create(API::class.java).userInfo() }
                var resultUser = withContext(Dispatchers.IO) {
                    HttpManager.instance.create(API::class.java).userInfo()
                }
                var resultBanner = withContext(context = Dispatchers.IO) {
                    HttpManager.instance.create(API::class.java).banner().take()
                }
                resultUser.data?.log()
                resultBanner?.log()
            }
            onError {
                it.log()
            }
        }*/

    }
}
