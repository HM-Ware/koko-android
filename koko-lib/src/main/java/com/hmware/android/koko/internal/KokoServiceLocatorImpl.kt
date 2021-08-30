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
import com.hmware.android.koko.api.KLogger
import java.lang.ref.WeakReference

internal class KokoServiceLocatorImpl : KokoServiceLocator {

    private val factoryList = mutableSetOf<KokoBeanDefinition<*>>()
    private val objectList = mutableListOf<BeanRecord<*>>()

    override fun <T> findBeanDefinition(type: Class<T>, key: Any?): KokoBeanDefinition<T>? {
        val matcher : (KokoBeanDefinition<*>)->Boolean = if (key == null) {
            { type.isAssignableFrom(it.type) }
        } else {
            { type.isAssignableFrom(it.type) && it.qualifier == key }
        }

        @Suppress("UNCHECKED_CAST")
        return factoryList.firstOrNull(matcher) as? KokoBeanDefinition<T>?
    }

    override fun <T> registerBeanDefinition(definition: KokoBeanDefinition<T>) {
        factoryList.add(definition)
    }

    override fun <T> add(
        type: Class<out T>,
        obj: T,
        forScope: KScope,
        beanDefinition: KokoBeanDefinition<T>
    ) {
        objectList.add(
            BeanRecord(
                clazz = type,
                obj = obj,
                scope = WeakReference(forScope),
                beanDefinition = beanDefinition
            )
        )
    }

    override fun <T> get(type: Class<T>, scope: KScope?, key: Any?): T  = optional(type, scope, key)!!

    override fun <T> optional(type: Class<T>, scope: Any?, key: Any?): T? {

        val conditions = listOfNotNull<(BeanRecord<*>)->Boolean> (
                { type.isAssignableFrom(it.clazz) },
                takeIf { scope != null }?.let { { it.beanDefinition.overrideScope != null || it.scope == scope } },
                takeIf { key != null }?.let { { it.beanDefinition.qualifier == key } }
        )

        val matcher: (BeanRecord<*>) -> Boolean = { conditions.all { condition -> condition.invoke(it) } }
        val matchingResult = objectList.filter(matcher)

        if (matchingResult.size > 1) {
            KokoLogger.log(
                    KLogger.Level.ERROR,
                    "More than one matching object." +
                            "\t\tType [${type.simpleName}]\n" +
                            "\t\tIn Scope [${scope?.javaClass?.simpleName}]\n" +
                            "\t\tWith qualifier [$key]"

            )
        }

        @Suppress("UNCHECKED_CAST")
        return matchingResult.firstOrNull()?.obj as? T
    }

    override fun clearScope(scope: KScope) {
        var removedObjectCount = 0

        objectList.removeAll {
            if (it.scope.get() == scope) {
                removedObjectCount ++
                true
            }else {
                false
            }
        }

        KokoLogger.log(
            level = KLogger.Level.INFO,
            message = "Clearing objects for scope[${scope::class.java.name}] removed object count [$removedObjectCount]"
        )
    }

    override fun transferObjectsScope(oldScope: KScope, newScope: KScope) {
        objectList
            .asSequence()
            .filter { it.scope.get() == oldScope }
            .forEach { it.scope = WeakReference(newScope) }
    }

    override fun reset() {
        factoryList.clear()
        objectList.clear()
    }

    private class BeanRecord<T>(
            val clazz: Class<out T>,
            val obj: T,
            var scope: WeakReference<KScope>,
            val beanDefinition: KokoBeanDefinition<T>
    )
}