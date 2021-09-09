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
import com.hmware.android.koko.internal.KokoBeanDefinition

class KModule internal constructor(
    val moduleDeclaration: KModuleDeclaration
){
    internal val definitions = arrayListOf<KokoBeanDefinition<*>>()

    /**
     * Declare a definition in current Module
     */
    fun <T> addDefinition(type: Class<T>, qualifier: Any?, overrideScope: KScope? = null, definition: KDefinition<T>) {
        definitions.add(
            KokoBeanDefinition(
                type,
                definition,
                qualifier,
                overrideScope
            )
        )
    }

    /**
     * Declare a Object Factory definition
     * @param qualifier providing a qualifier name allow same object type to be existing more once
     * inside same scope but with different qualifier name.
     * @param overrideScope When set created object would be attached to that scope and not caller scope.
     * @param definition - definition function
     */
    @Suppress("unused")
    inline fun <reified T> creator(
            qualifier: Any? = null,
            overrideScope: KScope? = null,
            noinline definition: KDefinition<T>
    ){
        addDefinition(T::class.java, qualifier, overrideScope, definition)
    }

    /**
     * Declare a Factory definition
     * @param qualifier
     * @param definition - definition function
     */
    @Suppress("unused")
    inline fun <reified T> appSessionCreator(
        qualifier: Any? = null,
        noinline definition: KDefinition<T>
    ){
        addDefinition(T::class.java, qualifier, KDefinedScopes.ApplicationScope, definition)
    }

    /**
     * Declare a Factory definition
     * @param qualifier
     * @param definition - definition function
     */
    @Suppress("unused")
    inline fun <reified T> userSessionCreator(
        qualifier: Any? = null,
        noinline definition: KDefinition<T>
    ){
        addDefinition(T::class.java, qualifier, KDefinedScopes.UserSessionScope, definition)
    }
}

typealias KDefinition<T> = KScope.(KDefinitionParameters) -> T

typealias KModuleDeclaration = KModule.(Application) -> Unit

