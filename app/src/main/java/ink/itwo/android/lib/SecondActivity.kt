package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ink.itwo.android.common.ktx.TIME_PATTERN
import ink.itwo.android.common.ktx.log
import ink.itwo.android.common.ktx.toStr
import ink.itwo.android.coroutines.dsl.cancelJob
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
        dsl {
            onBindLife { "aaa" }
            block {
                poll {
                    "aaa ${Date().toStr(TIME_PATTERN)}".log()
                }
            }
        }
        dsl {
            onBindLife { "aaa" }
            block {
                poll {
                    "aaa ${Date().toStr(TIME_PATTERN)}".log()
                }
            }
        }
        dsl {
            onBindLife { "bbb" }
            block {
                poll {
                    "bbb ${Date().toStr(TIME_PATTERN)}".log()
                }
            }
        }
    }


}