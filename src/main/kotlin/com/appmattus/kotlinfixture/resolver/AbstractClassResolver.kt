package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Classes
import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class AbstractClassResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        if ((obj as? KClass<*>)?.isAbstract == true) {
            val classInfo = Classes.classGraph.getClassInfo(obj.java.name)

            val classes = if (classInfo.isInterface) classInfo.classesImplementing else classInfo.subclasses

            classes.shuffled().forEach { subclass ->
                val result = context.resolve(subclass.loadClass().kotlin)
                if (result != Unresolved) {
                    return result
                }
            }
        }

        return Unresolved
    }
}
