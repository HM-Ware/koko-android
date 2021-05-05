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
package com.hmware.android.koko

import android.app.Application
import com.hmware.android.koko.internal.KokoServiceLocatorImpl
import com.hmware.android.koko.internal.KokoInternal
import com.hmware.android.koko.internal.KokoLogger
import kotlin.RuntimeException

object Koko {

    internal lateinit var kokoInternal : KokoInternal
    internal val isInitialised : Boolean
        get() = this::kokoInternal.isInitialized

    /**
     * Build a DefinitionParameters
     *
     * @see parameters
     * return ParameterList
     */
    fun parametersOf(vararg parameters: Any?) =
            if (parameters.size <= KDefinitionParameters.MAX_PARAMS) KDefinitionParameters(*parameters) else error("Can't build DefinitionParameters for more than ${KDefinitionParameters.MAX_PARAMS} arguments")

    fun <T> resolveKObject(
            type: Class<T>,
            scope: Any,
            qualifier: String? = null,
            searchForObjectOutsideCurrentScope: Boolean = true,
            createObjectIfNotFound: Boolean = true,
            requiredObject: Boolean = false,
            parameters: KParametersDefinition? = null
    ): T? {

        if (isInitialised.not()) {
            throw RuntimeException("Koko is not initialised. Please make sure to call startKoko")
        }

        return kokoInternal.resolveKObject(
                type,
                scope,
                qualifier,
                searchForObjectOutsideCurrentScope,
                createObjectIfNotFound,
                requiredObject,
                parameters
        )
    }
}

inline fun <reified T> requiredKObject(
        scope: KScope,
        qualifier: String? = null,
        searchForObjectOutsideCurrentScope: Boolean = true,
        createObjectIfNotFound: Boolean = true,
        noinline parameters: KParametersDefinition? = null
): T {
    return Koko.resolveKObject(
            type = T::class.java,
            scope = scope,
            qualifier = qualifier,
            searchForObjectOutsideCurrentScope = searchForObjectOutsideCurrentScope,
            createObjectIfNotFound = createObjectIfNotFound,
            requiredObject = true,
            parameters = parameters
    )!!
}

inline fun <reified T> optionalKObject(
        scope: KScope,
        qualifier: String? = null,
        searchForObjectOutsideCurrentScope: Boolean = true,
        createObjectIfNotFound: Boolean = true,
        noinline parameters: KParametersDefinition? = null
): T? {
    return Koko.resolveKObject(
            type = T::class.java,
            scope = scope,
            qualifier = qualifier,
            searchForObjectOutsideCurrentScope = searchForObjectOutsideCurrentScope,
            createObjectIfNotFound = createObjectIfNotFound,
            requiredObject = false,
            parameters = parameters
    )
}

fun startKoko(
        application: Application,
        modules: List<KModule>,
        logger: com.hmware.android.koko.api.KLogger? = null
) {

    if (Koko.isInitialised) {
        throw RuntimeException("Koko is already initialised.")
    }

    KokoLogger.logger = logger
    Koko.kokoInternal = KokoInternal(
            application = application,
            serviceLocator = KokoServiceLocatorImpl(),
            modules = modules
    )
}