/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hmware.android.koko.internal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.hmware.android.koko.KScope

internal class KokoLifecycleListener (
        application: Application,
        private val onScopeFinished: (KScope)->Unit
) {
    private val fragmentListener = object : FragmentLifecycleCallbacks() {
        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            onScopeFinished(f)
        }
    }

    private val activityListener = object: Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(fragmentListener, true)
        }

        override fun onActivityStarted(p0: Activity) {}

        override fun onActivityResumed(p0: Activity) {}

        override fun onActivityPaused(p0: Activity) {}

        override fun onActivityStopped(p0: Activity) {}

        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            if (activity.isFinishing) {
                onScopeFinished(activity)
            }
        }
    }

    init {
        application.registerActivityLifecycleCallbacks(activityListener)
    }
}