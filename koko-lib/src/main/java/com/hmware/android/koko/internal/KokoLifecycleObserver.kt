package com.hmware.android.koko.internal

import android.os.Bundle
import com.hmware.android.koko.KScope
import java.util.*

internal class KokoLifecycleObserver(
    private val onScopeFinished: (KScope) -> Unit,
    private val onScopeTransferred: (oldScope: KScope, newScope: KScope) -> Unit
) {

    private val pendingDestructionLifecycleOwners = mutableMapOf<String, Any>()
    private val lifecycleOwnerIdMap = mutableMapOf<Any, String>()

    enum class LifecycleDestructionReason {
        CONFIGURATION_CHANGED,
        LIFECYCLE_ENDED
    }

    fun onPreCreated(lifecycleOwner: Any, savedInstanceState: Bundle?) {
        if (lifecycleOwnerIdMap.containsKey(lifecycleOwner)) return

        val savedFragmentId = savedInstanceState?.getString(LIFECYCLE_OWNER_ID)

        if (savedFragmentId != null) {
            val oldFragment = pendingDestructionLifecycleOwners.remove(savedFragmentId)

            if(oldFragment != null) {
                onScopeTransferred(oldFragment, lifecycleOwner)
            }

            lifecycleOwnerIdMap[lifecycleOwner] = savedFragmentId
        } else {
            lifecycleOwnerIdMap[lifecycleOwner] = UUID.randomUUID().toString()
        }
    }

    fun ontSaveInstanceState(
        lifecycleOwner: Any,
        outState: Bundle
    ) {
        outState.putString(LIFECYCLE_OWNER_ID, lifecycleOwnerIdMap[lifecycleOwner])
    }

    fun onDestroyed(lifecycleOwner: Any, reason: LifecycleDestructionReason) {
        val fragmentId = lifecycleOwnerIdMap.remove(lifecycleOwner)

        when  {
            reason == LifecycleDestructionReason.CONFIGURATION_CHANGED && fragmentId != null -> {
                pendingDestructionLifecycleOwners[fragmentId] = lifecycleOwner
            }
            reason == LifecycleDestructionReason.LIFECYCLE_ENDED -> {
                onScopeFinished(lifecycleOwner)
            }
        }
    }

    companion object {
        private val LIFECYCLE_OWNER_ID = "KokoLifecycleObserver.LifeCycleOwnerId"
    }
}