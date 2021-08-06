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

import com.hmware.android.koko.KModule
import com.hmware.android.koko.KParametersDefinition
import com.hmware.android.koko.KScope
import kotlin.reflect.KClass

internal class KokoInternalMock (private val mockClassProvider : (KClass<Any>)->Any) : KokoInternal {
    override fun <T: Any> resolveKObject(
        kotlinType: KClass<T>,
        scope: Any,
        qualifier: String?,
        searchForObjectOutsideCurrentScope: Boolean,
        createObjectIfNotFound: Boolean,
        requiredObject: Boolean,
        parameters: KParametersDefinition?
    ): T {
        return mockClassProvider(kotlinType as KClass<Any>) as T //
    }

    override fun addModule(module: KModule) {}

    override fun clearScope(scope: KScope) {}

    override fun reset() {}
}