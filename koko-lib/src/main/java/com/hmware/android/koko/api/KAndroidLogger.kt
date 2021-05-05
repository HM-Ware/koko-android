package com.hmware.android.koko.api

import android.util.Log

class KAndroidLogger : KLogger {
    override fun log(level: KLogger.Level, message: String) {
        when (level) {
            KLogger.Level.DEBUG -> Log.d(KOKO_LOGGING_TAG, message)
            KLogger.Level.INFO -> Log.i(KOKO_LOGGING_TAG, message)
            KLogger.Level.ERROR -> Log.e(KOKO_LOGGING_TAG, message)
        }
    }

    companion object {
        private val KOKO_LOGGING_TAG = "Koko-Logger"
    }
}