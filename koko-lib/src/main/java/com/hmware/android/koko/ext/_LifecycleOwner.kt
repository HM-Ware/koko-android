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
package com.hmware.android.koko.ext

import androidx.lifecycle.LifecycleOwner
import com.hmware.android.koko.Koko
import com.hmware.android.koko.KParametersDefinition

/**
 * Lazy get an instance
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 * @param clazz
 */
fun <T> LifecycleOwner.kObject(
        clazz: Class<T>,
        qualifier: String? = null,
        parameters: KParametersDefinition? = null
): Lazy<T> = lazy { getOrCreateKObject(clazz, qualifier, parameters) }

/**
 * Lazy getByClass an instance
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T> LifecycleOwner.kObject(
        qualifier: String? = null,
        noinline parameters: KParametersDefinition? = null
): Lazy<T> = lazy { getKObject<T>(qualifier, parameters) }

/**
 * Get an instance
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T> LifecycleOwner.getKObject(
        qualifier: String? = null,
        noinline parameters: KParametersDefinition? = null
): T {
    return getOrCreateKObject(T::class.java, qualifier, parameters)
}


/**
 * This extension tries to find the given type or creates a new one with the given function
 */
fun <U> LifecycleOwner.getOrCreateKObject(
        clazz: Class<U>,
        qualifier: String? = null,
        parameters: KParametersDefinition? = null
): U = Koko.resolveKObject(
        type = clazz,
        scope = this@getOrCreateKObject,
        qualifier = qualifier,
        searchForObjectOutsideCurrentScope = true,
        createObjectIfNotFound = true,
        requiredObject = true,
        parameters
)!!
