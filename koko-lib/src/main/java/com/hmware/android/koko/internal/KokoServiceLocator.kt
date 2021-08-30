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

import com.hmware.android.koko.KScope


internal interface KokoServiceLocator {
    fun <T> findBeanDefinition(type: Class<T>, key: Any? = null) : KokoBeanDefinition<T>?
    fun <T> registerBeanDefinition (definition: KokoBeanDefinition<T>)

    fun <T> add(type: Class<out T>, obj: T, forScope: KScope, beanDefinition: KokoBeanDefinition<T>)

    fun <T> get(type: Class<T>, scope: KScope?, key: Any? = null) : T
    fun <T> optional(type: Class<T>, scope: KScope?, key: Any? = null) : T?

    fun clearScope(scope: KScope)
    fun transferObjectsScope(oldScope: KScope, newScope: KScope)
    fun reset()
}