package com.thales.github.utilities.schedulers

import rx.Scheduler
import rx.schedulers.Schedulers

class ImmediateSchedulerProvider : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.immediate()
    }

    override fun io(): Scheduler {
        return Schedulers.immediate()
    }

    override fun ui(): Scheduler {
        return Schedulers.immediate()
    }
}
