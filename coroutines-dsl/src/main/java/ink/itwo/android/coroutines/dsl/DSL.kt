package ink.itwo.android.coroutines.dsl

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import java.util.concurrent.TimeUnit

/** Created by wang on 2020/8/20. */
class DSL {
    private lateinit var block: suspend () -> Unit

    private var start: ((Job?) -> String)? = null

    private var success: (() -> Unit)? = null

    private var error: ((Exception) -> Unit)? = null

    private var complete: (() -> Unit)? = null

    infix fun block(block: suspend () -> Unit) {
        this.block = block
    }

    infix fun onStart(onStart: ((Job?) -> String)?) {
        this.start = onStart
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

    fun onLaunch(any: Any) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val job = coroutineContext[Job]
            val key = start?.invoke(coroutineContext[Job]) ?: any.toString()
            GlobalScopeJobMap[key] = job
            try {
                val invoke = block.invoke()
                invoke.let { success?.invoke() } ?: error?.invoke(java.lang.Exception(""))
            } catch (e: Exception) {
                error?.invoke(e)
            } finally {
                complete?.invoke()
                GlobalScopeJobMap.remove(key)
            }
        }
    }
}

fun Any.dsl(mDsl: DSL.() -> Unit) {
    DSL().apply(mDsl).onLaunch(this)
}

val GlobalScopeJobMap = mutableMapOf<String, Job?>()
fun cancelGlobalScope() {
    GlobalScopeJobMap.values.forEach { it?.cancel() }
    GlobalScopeJobMap.clear()
}

fun cancelGlobalScope(any: Any) {
    val job = GlobalScopeJobMap[any.toString()]
    job?.cancel()
    GlobalScopeJobMap.remove(any.toString())
}

suspend fun <T> io(block: suspend () -> T): T = withContext(Dispatchers.IO) { block() }
suspend fun <T> ui(block: suspend () -> T): T = withContext(Dispatchers.Main) { block() }
suspend fun <T> poll(delayed:Long=0,interval:Long=3, unit:TimeUnit=TimeUnit.SECONDS, block: suspend () -> T)= coroutineScope {
    delay(delayed)
    while (true) {
        block()
        delay(TimeUnit.MILLISECONDS.convert(interval,unit))
    }
}
