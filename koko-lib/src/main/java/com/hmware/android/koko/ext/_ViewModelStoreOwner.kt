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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.hmware.android.koko.Koko
import com.hmware.android.koko.KParametersDefinition
import kotlin.reflect.KClass

/**
 * Lazy get a viewModel instance, if new object is created it will be scoped to ViewModelStoreOwner
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 * @param clazz
 */
fun <T : ViewModel> ViewModelStoreOwner.scopedKViewModel(
        clazz: KClass<T>,
        qualifier: String? = null,
        parameters: KParametersDefinition? = null
): Lazy<T> = lazy { getOrCreateKViewModel(clazz, qualifier, parameters) }

/**
 * Lazy getByClass a viewModel instance
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> ViewModelStoreOwner.scopedKViewModel(
        qualifier: String? = null,
        noinline parameters: KParametersDefinition? = null
): Lazy<T> = lazy { getScopedKViewModel<T>(qualifier, parameters) }

/**
 * Get a viewModel instance
 *
 * @param qualifier - Koko BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> ViewModelStoreOwner.getScopedKViewModel(
        qualifier: String? = null,
        noinline parameters: KParametersDefinition? = null
): T {
    return getOrCreateKViewModel(T::class, qualifier, parameters)
}


/**
 * This extension tries to find the given ViewModel or creates a new one with the given function
 * Usage:
 * `getOrCreateViewModel<YourCustomViewModelInterface> { YourCustomViewModelImpl() }`
 */
fun <U : ViewModel> ViewModelStoreOwner.getOrCreateKViewModel(
        clazz: KClass<U>,
        qualifier: String? = null,
        parameters: KParametersDefinition? = null
): U {
    return ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return Koko.resolveKObject(
                            kotlinType = clazz,
                            scope = this@getOrCreateKViewModel,
                            qualifier = qualifier,
                            searchForObjectOutsideCurrentScope = false,
                            createObjectIfNotFound = true,
                            requiredObject = true,
                            parameters = parameters
                    ) as T
                }
            }
    ).let {
        if (qualifier.isNullOrBlank())
            it.get(clazz.java)
        else
            it.get(qualifier, clazz.java)
    }
}
