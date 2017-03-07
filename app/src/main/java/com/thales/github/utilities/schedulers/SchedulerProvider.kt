package com.thales.github.utilities.schedulers

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SchedulerProvider private constructor() : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {
        private var instance_1: SchedulerProvider? = null

        fun getInstance(): SchedulerProvider {
            if (instance_1 == null) {
                instance_1 = SchedulerProvider()
            }
            return instance_1 as SchedulerProvider
        }
    }
}
