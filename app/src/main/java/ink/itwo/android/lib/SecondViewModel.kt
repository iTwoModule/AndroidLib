package ink.itwo.android.lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ink.itwo.android.common.ktx.log
import ink.itwo.android.coroutines.ktx.dsl
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Created by wang on 2020/8/21. */
class SecondViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
    }

    fun s() {
        dsl {
            onStart {
                it.log()
                "SecondViewModel"
            }
            block {
                "block".log()
                delay(1000000)
            }
            onError { "onError".log() }
            onComplete { "onComplete".log() }
        }
        dsl {
            onStart {
                it.log()
                "SecondViewModel"
            }
            block {
                "block".log()
                delay(1000000)
            }
            onError { "onError".log() }
            onComplete { "onComplete".log() }
        }
        viewModelScope.launch {
            val job = coroutineContext[Job]
            job.log()
            delay(1000000)
        }

        viewModelScope.launch {
            val job = coroutineContext[Job]
            job.log()
            delay(1000000)
        }
    }

}