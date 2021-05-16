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
    fun <T> addDefinition(type: Class<T>, qualifier: Any?, definition: KDefinition<T>) {
        definitions.add(
                KokoBeanDefinition(
                        type,
                        definition,
                        qualifier
                )
        )
    }

    /**
     * Declare a Factory definition
     * @param qualifier
     * @param definition - definition function
     */
    inline fun <reified T> creator(
            qualifier: Any? = null,
            noinline definition: KDefinition<T>
    ){
        addDefinition(T::class.java, qualifier, definition)
    }
}

typealias KScope = Any

typealias KDefinition<T> = KScope.(KDefinitionParameters) -> T

typealias KModuleDeclaration = KModule.(Application) -> Unit

/**
 * Define a Module
 */
fun kModule(moduleDeclaration: KModuleDeclaration): KModule = KModule(moduleDeclaration)
