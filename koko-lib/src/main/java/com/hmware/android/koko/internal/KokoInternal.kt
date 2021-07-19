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

import android.app.Application
import com.hmware.android.koko.KDefinitionParameters
import com.hmware.android.koko.KModule
import com.hmware.android.koko.KParametersDefinition
import com.hmware.android.koko.KScope
import com.hmware.android.koko.api.KLogger
import java.lang.RuntimeException

internal class KokoInternal(
    internal val application: Application,
    private val serviceLocator: KokoServiceLocator,
    modules : List<KModule>
) {

    private val _modules: MutableList<KModule> = modules.toMutableList()

    @Suppress("unused")
    private val lifecycleListener = KokoLifecycleListener(application) {
        clearScope(it)
    }

    init {
        modules
            .forEach { it.moduleDeclaration.invoke(it, application) }

        modules
            .flatMap { it.definitions }
            .forEach {
                serviceLocator.registerBeanDefinition(it)
            }
    }

    fun <T> resolveKObject(
        type: Class<T>,
        scope: Any,
        qualifier: String?,
        searchForObjectOutsideCurrentScope: Boolean,
        createObjectIfNotFound: Boolean,
        requiredObject: Boolean,
        parameters: KParametersDefinition?
    ): T? {

        KokoLogger.log(
            level = KLogger.Level.DEBUG,
            message = "Resolving object\n" +
                    "\t\t Of type: ${type.name}" +
                    "\t\t For scope : ${scope::class.java.name}" +
                    "\t\t With qualifier : $qualifier" +
                    "\t\t SearchForObjectOutsideCurrentScope = $searchForObjectOutsideCurrentScope" +
                    "\t\t CreateObjectIfNotFound = $createObjectIfNotFound" +
                    "\t\t RequiredObject = $requiredObject" +
                    "\t\t Has Parameters = ${parameters != null}"

        )

        val existingObject = if (searchForObjectOutsideCurrentScope)
            serviceLocator.optional(type = type, scope = null, key = qualifier)
        else
            serviceLocator.optional(type = type, scope = scope, key = qualifier)


        val result = if (existingObject == null && createObjectIfNotFound) {
            val definition = serviceLocator.findBeanDefinition(type, qualifier) ?: throw RuntimeException(
                "Unable to resolve required factory\n" +
                        "\t\tOf type [${type.name}]\n" +
                        "\t\tWith qualifier [$qualifier]"
            )
            val factory = definition.factory
            val newObject =
                scope.factory(parameters?.invoke() ?: KDefinitionParameters.EmptyParameters)
            serviceLocator.add(type = type, obj = newObject, forScope = definition.overrideScope ?: scope, beanDefinition = definition)
            newObject
        } else {
            existingObject
        }


        if (requiredObject && result == null) {
            throw RuntimeException(
                "Unable to resolve required object\n" +
                        "\t\tOf type [${type.name}]\n" +
                        "\t\tFor scope [$scope]\n" +
                        "\t\tWith qualifier [$qualifier]"
            )
        }

        return result
    }

    fun addModule(module: KModule) {
        module.moduleDeclaration.invoke(module, application)
        module.definitions.forEach {
            serviceLocator.registerBeanDefinition(it)
        }
        _modules.add(module)
    }

    fun clearScope(scope: KScope) {
        serviceLocator.clearScope(scope)

    }

    fun reset() {
        serviceLocator.reset()
        _modules.clear()
    }
}