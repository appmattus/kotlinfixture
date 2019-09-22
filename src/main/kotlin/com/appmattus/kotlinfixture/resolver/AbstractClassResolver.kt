package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Classes
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class AbstractClassResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if ((obj as? KClass<*>)?.isAbstract == true) {
            val classInfo = Classes.classGraph.getClassInfo(obj.java.name)

            val classes = if (classInfo.isInterface) classInfo.classesImplementing else classInfo.subclasses

            classes.shuffled().forEach { subclass ->
                val result = resolver.resolve(subclass.loadClass().kotlin, resolver)
                if (result != Unresolved) {
                    return result
                }
            }
        }

        return Unresolved
    }
}
