package ink.itwo.android.common

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ScheduledThreadPoolExecutor
import kotlin.math.max
import kotlin.math.min

/** Created by wang on 1/24/21. */


// 线程

/** CPU数量*/
val Common.CPU_COUNT: Int by lazy { Runtime.getRuntime().availableProcessors() }

/** 核心线程数量大小*/
val Common.corePoolSize: Int by lazy { max(2, min(Common.CPU_COUNT - 1, 4)) }

/** 线程池最大容纳线程数*/
val Common.maximumPoolSize: Int by lazy { Common.CPU_COUNT * 2 + 1 }

val Common.executorCoroutineDispatcher: ExecutorCoroutineDispatcher by lazy { ScheduledThreadPoolExecutor(Common.maximumPoolSize).asCoroutineDispatcher() }

