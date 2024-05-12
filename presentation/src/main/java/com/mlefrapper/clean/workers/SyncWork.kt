package com.mlefrapper.clean.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.mlefrapper.data.util.DispatchersProvider
import com.mlefrapper.domain.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext
import timber.log.Timber

const val SYNC_WORK_MAX_ATTEMPTS = 3

@HiltWorker
class SyncWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    val movieRepository: MovieRepository,
    val dispatchers: DispatchersProvider,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(dispatchers.getIO()) {
        return@withContext if (movieRepository.sync()) {
            Timber.d("SyncWork: doWork() called -> success")
            Result.success()
        } else {
            val lastAttempt = runAttemptCount >= SYNC_WORK_MAX_ATTEMPTS
            if (lastAttempt) {
                Timber.d("SyncWork: doWork() called -> failure")
                Result.failure()
            } else {
                Timber.d("SyncWork: doWork() called -> retry")
                Result.retry()
            }
        }
    }

    companion object {
        fun getOneTimeWorkRequest() = OneTimeWorkRequestBuilder<SyncWork>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    }
}