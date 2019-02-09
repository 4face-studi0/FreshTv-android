package studio.forface.freshtv.commonandroid.utils

import androidx.work.BackoffPolicy.*
import androidx.work.ExistingPeriodicWorkPolicy.*
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.threeten.bp.Duration
import java.util.concurrent.TimeUnit

/*
 * Author: Davide Giuseppe Farella.
 * Utilities for WorkManager
 */

/** @return an instance of [WorkManager] by [WorkManager.getInstance] */
val workManager get() = WorkManager.getInstance()

/** Enqueue an unique periodic Work without any optional parameters */
inline fun <reified W: ListenableWorker> WorkManager.enqueueUniquePeriodicWork(
    uniqueWorkName: String,
    repeatInterval: Duration,
    flexTimeInterval: Duration? = null,
    replace: Boolean = false,
    exponentialBackoff: Boolean = false,
    backoffDelay: Duration = repeatInterval.dividedBy( if ( exponentialBackoff ) 1000 else 100 )
) {
    val timeUnit = TimeUnit.MINUTES
    val repeatMin = repeatInterval.toMinutes()
    val flexMin = flexTimeInterval?.toMinutes()
    val replacePolicy = if ( replace ) REPLACE else KEEP
    val backoffPolicy = if ( exponentialBackoff ) EXPONENTIAL else LINEAR

    val builder = if ( flexMin != null )
        PeriodicWorkRequestBuilder<W>( repeatMin, timeUnit, flexMin, timeUnit )
    else PeriodicWorkRequestBuilder<W>( repeatMin, timeUnit )

    builder.setBackoffCriteria( backoffPolicy, backoffDelay.toMinutes(), timeUnit )

    enqueueUniquePeriodicWork( uniqueWorkName, replacePolicy, builder.build() )
}