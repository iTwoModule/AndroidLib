package ink.itwo.android.lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ink.itwo.android.common.CommonUtil
import ink.itwo.android.common.ktx.log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView?.setOnClickListener {
            it.log()
            CommonUtil.versionCode.log()
            CommonUtil.versionName.log()
            CommonUtil.deviceHeight.log()
            CommonUtil.deviceWith.log()
            val packageInfo = CommonUtil.getPackageInfo()
            packageInfo?.log()
        }
    }
}