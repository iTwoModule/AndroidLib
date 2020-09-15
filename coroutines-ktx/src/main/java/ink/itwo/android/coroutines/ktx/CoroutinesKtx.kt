package ink.itwo.android.coroutines.ktx

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import ink.itwo.android.common.toast
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

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

    fun onLaunch(coroutineScope:CoroutineScope) {
        coroutineScope.launch(context = Dispatchers.Main) {
            val job = coroutineContext[Job]
            var key = bindLife?.invoke()

            key?.let { job?.let { it1 -> add(it, it1) } }

            start?.invoke(job)
            try {
                 block.invoke()
            } catch (e: Exception) {
                error?.invoke(e)
            } finally {
                complete?.invoke()
                key?.let { cancelJob(it) }
            }
        }
    }
}

fun GlobalScope.dsl(mDsl: DSL.() -> Unit) {
    DSL().apply(mDsl).onLaunch(this)
}
fun ViewModel.dsl(mDsl: DSL.() -> Unit) {
    DSL().apply(mDsl).onLaunch(this.viewModelScope)
}
fun Fragment.dsl(mDsl: DSL.() -> Unit) {
    DSL().apply(mDsl).onLaunch(this.lifecycleScope)
}
fun AppCompatActivity.dsl(mDsl: DSL.() -> Unit) {
    DSL().apply(mDsl).onLaunch(this.lifecycleScope)
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

/**
 *  轮询
 * @param delayed Long  初始时延时时间
 * @param interval Long  间隔时间
 * @param unit TimeUnit
 * @param block SuspendFunction1<Int, Boolean?>
 */
suspend fun poll(delayed: Long = 0, interval: Long = 3, unit: TimeUnit = TimeUnit.SECONDS, block: suspend (Int) -> Boolean) = coroutineScope {
    if (delayed != 0L) delay(delayed)
    var count = -1
    var condition = true
    while (condition) {
        count++
        condition = block(count)
        delay(TimeUnit.MILLISECONDS.convert(interval, unit))
    }
    cancel()
}

/**
 *   计时器
 * @param period Long 间隔时间
 * @param unit TimeUnit
 * @param block SuspendFunction1<Long, T>
 */
suspend fun <T> interval(start: Long = 0, count: Long = 0, period: Long = 1, unit: TimeUnit = TimeUnit.SECONDS, block: suspend (Long) -> T) = coroutineScope {
    for (i in start until start + count) {
        block(i)
        delay(TimeUnit.MILLISECONDS.convert(period, unit))
    }
}


//inline fun AppCompatActivity.launch(toastEnable: Boolean = true, exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable -> if (toastEnable) throwable.message?.toast() }, crossinline block: suspend () -> Unit): Job {
//    return lifecycleScope.launch(exceptionHandler) { block() }
//}
//
//inline fun AppCompatActivity.launchIO(toastEnable: Boolean = true, exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable -> if (toastEnable) throwable.message?.toast() }, crossinline block: suspend () -> Unit): Job {
//    return lifecycleScope.launch(exceptionHandler) { withContext(Dispatchers.IO) { block() } }
//}

fun AppCompatActivity.launch(toastEnable: Boolean = true, start: (() -> Unit)? = null, error: ((Exception) -> Unit)? = null, complete: (() -> Unit)? = null, block: suspend () -> Unit): Job {
    return lifecycleScope.job(Dispatchers.Main,block, error, toastEnable, complete)
}

fun AppCompatActivity.launchIO(toastEnable: Boolean = true, start: (() -> Unit)? = null, error: ((Exception) -> Unit)? = null, complete: (() -> Unit)? = null, block: suspend () -> Unit): Job {
    return lifecycleScope.job(Dispatchers.IO,block, error, toastEnable, complete)
}

fun Fragment.launch(toastEnable: Boolean = true, start: (() -> Unit)? = null, error: ((Exception) -> Unit)? = null, complete: (() -> Unit)? = null, block: suspend () -> Unit): Job {
    return lifecycleScope.job(Dispatchers.Main,block, error, toastEnable, complete)
}

fun Fragment.launchIO(toastEnable: Boolean = true, start: (() -> Unit)? = null, error: ((Exception) -> Unit)? = null, complete: (() -> Unit)? = null, block: suspend () -> Unit): Job {
    return lifecycleScope.job(Dispatchers.IO,block, error, toastEnable, complete)
}

fun ViewModel.launch(toastEnable: Boolean = true, start: (() -> Unit)? = null, error: ((Exception) -> Unit)? = null, complete: (() -> Unit)? = null, block: suspend () -> Unit): Job {
    return viewModelScope.job(Dispatchers.Main,block, error, toastEnable, complete)
}

fun ViewModel.launchIO(toastEnable: Boolean = true, start: (() -> Unit)? = null, error: ((Exception) -> Unit)? = null, complete: (() -> Unit)? = null, block: suspend () -> Unit): Job {
    return viewModelScope.job(Dispatchers.IO,block, error, toastEnable, complete)
}

fun CoroutineScope.job(context: CoroutineContext, block: suspend () -> Unit, error: ((Exception) -> Unit)?, toastEnable: Boolean, complete: (() -> Unit)?): Job {
    lateinit var job: Job
    try {
        job = launch { withContext(context) { block() } }
    } catch (e: Exception) {
        error?.invoke(e)
        if (toastEnable) {
            e.message.toast()
        }

    } finally {
        complete?.invoke()
    }
    return job
}



