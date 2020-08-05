package com.kronos.diffutil.utils

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class DiffThreadFactory : ThreadFactory {
    var group: ThreadGroup? = null
    var namePrefix: String? = null

    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = "diff-pool-" + poolNumber.getAndIncrement()

    }

    override fun newThread(r: Runnable?): Thread? {
        val t = Thread(group, r, namePrefix, 0)
        if (t.isDaemon) t.isDaemon = false
        if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
        return t
    }

    companion object {
        private val poolNumber = AtomicInteger(1)
    }
}
