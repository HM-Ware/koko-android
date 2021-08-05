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
import kotlin.reflect.KClass

/**
 * Lazy get an instance, if new object is created it will be scoped to LifecycleOwner
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 * @param clazz
 */
fun <T: Any> LifecycleOwner.scopedKObject(
        clazz: KClass<T>,
        qualifier: String? = null,
        parameters: KParametersDefinition? = null
): Lazy<T> = lazy { getOrCreateKObject(clazz, qualifier, parameters) }

/**
 * Lazy get an instance, if new object is created it will be scoped to LifecycleOwner
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T: Any> LifecycleOwner.scopedKObject(
        qualifier: String? = null,
        noinline parameters: KParametersDefinition? = null
): Lazy<T> = lazy { getScopedKObject<T>(qualifier, parameters) }

/**
 * Get an instance, if new object is created it will be scoped to LifecycleOwner
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T: Any> LifecycleOwner.getScopedKObject(
        qualifier: String? = null,
        noinline parameters: KParametersDefinition? = null
): T {
    return getOrCreateKObject(T::class, qualifier, parameters)

}

/**
 * This extension tries to find the given type or creates a new one with the given function
 */
fun <U: Any> LifecycleOwner.getOrCreateKObject(
        clazz: KClass<U>,
        qualifier: String? = null,
        parameters: KParametersDefinition? = null
): U = Koko.resolveKObject(
        kotlinType = clazz,
        scope = this@getOrCreateKObject,
        qualifier = qualifier,
        searchForObjectOutsideCurrentScope = true,
        createObjectIfNotFound = true,
        requiredObject = true,
        parameters
)!!
