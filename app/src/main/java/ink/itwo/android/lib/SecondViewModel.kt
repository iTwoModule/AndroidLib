package ink.itwo.android.lib

import androidx.lifecycle.ViewModel
import ink.itwo.android.common.ktx.log
import ink.itwo.android.coroutines.dsl.dsl
import kotlinx.coroutines.delay

/** Created by wang on 2020/8/21. */
class SecondViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
    }

    fun s() {
        dsl {
            block {
                "block".log()
                delay(1000000)
            }
            onError { "onError".log() }
            onComplete { "onComplete".log() }
        }
    }

}