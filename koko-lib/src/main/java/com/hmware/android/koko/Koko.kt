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
import com.hmware.android.koko.internal.*
import com.hmware.android.koko.internal.KokoInternal
import com.hmware.android.koko.internal.KokoInternalImpl
import com.hmware.android.koko.internal.KokoInternalMock
import com.hmware.android.koko.internal.KokoLogger
import com.hmware.android.koko.internal.KokoServiceLocatorImpl

@PublishedApi
internal object Koko {

    private var kokoInternal : KokoInternal? = null

    fun startKoko(
        application: Application,
        modules: List<KModule>,
        logger: com.hmware.android.koko.api.KLogger? = null
    ) {

        if (kokoInternal != null) {
            throw RuntimeException("Koko is already initialised.")
        }

        KokoLogger.logger = logger
        kokoInternal = KokoInternalImpl(
            application = application,
            serviceLocator = KokoServiceLocatorImpl(),
            modules = modules
        )
    }

    fun mockKoko() {
        kokoInternal = KokoInternalMock()
    }

    fun terminateKoko() {
        kokoInternal?.reset()
        kokoInternal = null
    }

    fun <T> resolveKObject(
            type: Class<T>,
            scope: KScope,
            qualifier: String? = null,
            searchForObjectOutsideCurrentScope: Boolean = true,
            createObjectIfNotFound: Boolean = true,
            requiredObject: Boolean = false,
            parameters: KParametersDefinition? = null
    ): T? {

        val koko = kokoInternal
            ?: throw RuntimeException("Koko is not initialised. Please make sure to call startKoko")

        return koko.resolveKObject(
                type,
                scope,
                qualifier,
                searchForObjectOutsideCurrentScope,
                createObjectIfNotFound,
                requiredObject,
                parameters
        )
    }

    fun clearKScope(scope: KScope) = kokoInternal?.clearScope(scope)
}

@Suppress("unused")
inline fun <reified T> requiredKObject(
    scope: KScope,
    qualifier: String? = null,
    searchForObjectOutsideCurrentScope: Boolean = true,
    createObjectIfNotFound: Boolean = true,
    noinline parameters: KParametersDefinition? = null
): Lazy<T> = lazy {
    getRequiredKObject(scope, qualifier, searchForObjectOutsideCurrentScope, createObjectIfNotFound, parameters)
}

@Suppress("unused")
inline fun <reified T> optionalKObject(
    scope: KScope,
    qualifier: String? = null,
    searchForObjectOutsideCurrentScope: Boolean = true,
    createObjectIfNotFound: Boolean = true,
    noinline parameters: KParametersDefinition? = null
): Lazy<T?> = lazy {
    getOptionalKObject(scope, qualifier, searchForObjectOutsideCurrentScope, createObjectIfNotFound, parameters)
}

inline fun <reified T> getRequiredKObject(
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

inline fun <reified T> getOptionalKObject(
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

@Suppress("unused")
fun clearKScope(scope: KScope) = Koko.clearKScope(scope)

@Suppress("unused")
fun startKoko(
        application: Application,
        modules: List<KModule>,
        logger: com.hmware.android.koko.api.KLogger? = null
) = Koko.startKoko(application, modules, logger)

@Suppress("unused")
fun mockKoko() = Koko.mockKoko()


@Suppress("unused")
fun terminateKoko() = Koko.terminateKoko()

/**
 * Build a DefinitionParameters
 *
 * @see parameters
 * return ParameterList
 */
@Suppress("unused")
fun kParametersOf(vararg parameters: Any?) =
    if (parameters.size <= KDefinitionParameters.MAX_PARAMS) KDefinitionParameters(*parameters) else error("Can't build DefinitionParameters for more than ${KDefinitionParameters.MAX_PARAMS} arguments")

/**
 * Define a Module
 */
@Suppress("unused")
fun kModule(moduleDeclaration: KModuleDeclaration): KModule = KModule(moduleDeclaration)