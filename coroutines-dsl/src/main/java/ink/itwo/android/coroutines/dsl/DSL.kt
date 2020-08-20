package ink.itwo.android.coroutines.dsl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/** Created by wang on 2020/8/20. */
class DSL {
    private lateinit var block: suspend () -> Unit

    private var start: (() -> Unit)? = null

    private var success: (() -> Unit)? = null

    private var error: ((Exception) -> Unit)? = null

    private var complete: (() -> Unit)? = null

    infix fun block(block: suspend () -> Unit) {
        this.block = block
    }

    infix fun onStart(onStart: (() -> Unit)?) {
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

    fun onLaunch() {
        GlobalScope.launch(context = Dispatchers.Main) {
            start?.invoke()
            try {
                val invoke = block.invoke()
                invoke.let { success?.invoke() } ?:error?.invoke(java.lang.Exception(""))
            } catch (e: Exception) {
                error?.invoke(e)
            } finally {
                complete?.invoke()
            }
        }
    }
}

fun  dsl(mDsl: DSL.() -> Unit) {
    DSL().apply(mDsl).onLaunch()
}