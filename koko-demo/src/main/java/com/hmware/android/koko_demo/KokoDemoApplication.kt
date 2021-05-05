package com.hmware.android.koko_demo

import android.app.Application
import com.hmware.android.koko.api.KAndroidLogger
import com.hmware.android.koko.startKoko
import com.hmware.android.koko_demo.movies.details.movieDetailsModule
import com.hmware.android.koko_demo.movies.list.moviesListsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KokoDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@KokoDemoApplication)
            this.createEagerInstances()
            this.environmentProperties()
            // use modules
            modules(movieDetailsModule, moviesListsModule)
        }

        startKoko(
                application = this,
                logger = KAndroidLogger(),
                modules = emptyList()
        )
    }
}