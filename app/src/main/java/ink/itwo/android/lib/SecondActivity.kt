package ink.itwo.android.lib

import android.os.Bundle
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ink.itwo.android.common.ktx.TIME_PATTERN
import ink.itwo.android.common.ktx.log
import ink.itwo.android.common.ktx.toStr
import ink.itwo.android.coroutines.dsl.cancelGlobalScope
import ink.itwo.android.coroutines.dsl.dsl
import ink.itwo.android.coroutines.dsl.poll
import kotlinx.android.synthetic.main.activity_second.*
import java.util.*

/** Created by wang on 2020/8/21. */
class SecondActivity : AppCompatActivity() /*CoroutineScope by MainScope()  , */ {
    val viewModel: SecondViewModel by lazy { ViewModelProvider(this).get(SecondViewModel::class.java) }

//    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        textView?.setOnClickListener {
            viewModel.s()
            aa()
        }
        textView2?.setOnClickListener { cancelGlobalScope(this@SecondActivity) }

    }

    override fun onDestroy() {
//        job?.cancel()
        super.onDestroy()
    }

    private fun aa() {
        dsl {
            block {
                poll(interval = 1) { Date().toStr(TIME_PATTERN).log() }
            }
            onError { "onError".log() }
            onComplete {"onComplete".log()  }
        }
    }


}