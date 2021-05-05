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

import com.hmware.android.koko.KDefinitionParameters
import com.hmware.android.koko.KScope


internal interface KokoServiceLocator {
    fun <T> findFactory(type: Class<T>, key: Any? = null) : (KScope.(KDefinitionParameters)->T)?
    fun <T> registerFactory (type: Class<out T>, factory: KScope.(KDefinitionParameters)->T, key: Any? = null)

    fun <T> add(type: Class<T>, obj: T, forScope: Any, key: Any? = null)

    fun <T> get(type: Class<T>, scope: Any?, key: Any? = null) : T
    fun <T> optional(type: Class<T>, scope: Any?, key: Any? = null) : T?

    fun clearScope(scope: Any)
}