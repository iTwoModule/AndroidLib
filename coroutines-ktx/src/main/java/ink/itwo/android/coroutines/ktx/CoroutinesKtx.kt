package ink.itwo.android.coroutines.ktx

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import ink.itwo.android.common.handlerException
import ink.itwo.android.common.toast
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/** Created by wang on 2020/8/20. */
class DSL {
    private lateinit var block: suspend () -> Unit

    private var start: ((Job?) -> Unit)? = null
    private var bindLife: (() -> String)? = null

    private var success: (() -> Unit)? = null

    private var error: ((Exception) -> Unit)? = null

    private var complete: (() -> Unit)? = null

    infix fun block(block: suspend () -> Unit) {
        this.block = block
    }

    infix fun onStart(onStart: ((Job?) -> Unit)?) {
        this.start = onStart
    }

    /** 绑定生命周期,需要自行接触结束 Job*/
    infix fun onBindLife(bindLife: (() -> String)) {
        this.bindLife = bindLife
    }

    infix fun onSuccess(onSuccess: (() -> Unit)?) {
        this.success = onSuccess
    }

    infix fun onError(onError: ((Exception) -> Unit)?) {
        this.error = onError
    }

    infix fun onComplete(onComplete: (() -> Unit)?) {
        this.complete = onComplete
    }

    fun onLaunch() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val job = coroutineContext[Job]
            var key = bindLife?.invoke()

            key?.let { job?.let { it1 -> add(it, it1) } }

            start?.invoke(job)
            try {
                val invoke = block.invoke()
                invoke.let { success?.invoke() } ?: error?.invoke(java.lang.Exception(""))
            } catch (e: Exception) {
                error?.invoke(e)
            } finally {
                complete?.invoke()
                key?.let { cancelJob(it) }
            }
        }
    }
}

fun dsl(mDsl: DSL.() -> Unit) {
    DSL().apply(mDsl).onLaunch()
}


val GlobalScopeJobMap = mutableMapOf<String, Job?>()
private const val key_suffix: String = "_GlobalScopeJobMap"
fun Job.bindLife(key: String): Job {
    add(key, this)
    invokeOnCompletion { cancelJob(key) }
    return this

}

fun cancelJobAll() {
    GlobalScopeJobMap.values.forEach { it?.cancel() }
    GlobalScopeJobMap.clear()
}

fun cancelJob(key: String) {
    val filterKeys = GlobalScopeJobMap.filterKeys { it.startsWith("$key$key_suffix") }
    filterKeys.values.forEach { it?.cancel() }
    filterKeys.keys.forEach { GlobalScopeJobMap.remove(it) }
}

private fun add(key: String, job: Job) {
    GlobalScopeJobMap["$key$key_suffix${System.currentTimeMillis()}"] = job
}


suspend fun <T> io(block: suspend () -> T): T = withContext(Dispatchers.IO) { block() }
suspend fun <T> ui(block: suspend () -> T): T = withContext(Dispatchers.Main) { block() }
suspend fun <T> poll(delayed: Long = 0, interval: Long = 3, unit: TimeUnit = TimeUnit.SECONDS, block: suspend () -> T) = coroutineScope {
    delay(delayed)
    while (true) {
        block()
        delay(TimeUnit.MILLISECONDS.convert(interval, unit))
    }
}


inline fun AppCompatActivity.launch(toastEnable:Boolean=true,exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->if(toastEnable) throwable.message?.toast() },crossinline block: suspend () -> Unit): Job {
    return lifecycleScope.launch(exceptionHandler) { block() }
}

inline fun AppCompatActivity.launchIO(toastEnable:Boolean=true,exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->if(toastEnable) throwable.message?.toast() },crossinline block: suspend () -> Unit): Job {
    return lifecycleScope.launch(exceptionHandler) { withContext(Dispatchers.IO) { block() } }
}

inline fun Fragment.launch(toastEnable:Boolean=true,exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->if(toastEnable) throwable.message?.toast() },crossinline block: suspend () -> Unit): Job {
    return lifecycleScope.launch(exceptionHandler) { block() }
}

inline fun Fragment.launchIO(toastEnable:Boolean=true,exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->if(toastEnable) throwable.message?.toast() },crossinline block: suspend () -> Unit): Job {
    return lifecycleScope.launch(exceptionHandler) { withContext(Dispatchers.IO) { block() } }
}

inline fun ViewModel.launch(toastEnable:Boolean=true,exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->if(toastEnable) throwable.message?.toast() },crossinline block: suspend () -> Unit): Job {
    return viewModelScope.launch(exceptionHandler) { block() }
}

inline fun ViewModel.launchIO(toastEnable:Boolean=true,exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->if(toastEnable) throwable.message?.toast() },crossinline block: suspend () -> Unit): Job {
    return viewModelScope.launch(exceptionHandler) { withContext(Dispatchers.IO) { block() } }
}
