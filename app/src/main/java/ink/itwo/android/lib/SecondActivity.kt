package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ink.itwo.android.coroutines.HttpManager
import ink.itwo.android.coroutines.ktx.cancelJob
import ink.itwo.android.coroutines.ktx.launch
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.coroutines.*

/** Created by wang on 2020/8/21. */
class SecondActivity : AppCompatActivity() /*CoroutineScope by MainScope()  , */ {
    val viewModel: SecondViewModel by lazy { ViewModelProvider(this).get(SecondViewModel::class.java) }

//    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        textView?.setOnClickListener {
//            viewModel.s()
            aa()
        }
        textView2?.setOnClickListener {
            cancelJob("aaa")
        }

    }

    override fun onDestroy() {
//        job?.cancel()
        super.onDestroy()
    }

    private fun aa() {
        launch {
            withContext(Dispatchers.IO) {
                100/0
                1
            }
        }
    }
}


val api: API by lazy { HttpManager.instance.create(API::class.java) }
