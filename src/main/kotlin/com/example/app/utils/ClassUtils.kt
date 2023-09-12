package com.example.app.utils

import org.springframework.web.method.HandlerMethod
import java.lang.reflect.Field
import java.lang.reflect.Method

object ClassUtils {

    fun <T> getFieldValue(obj: Any, fieldName: String, targetFieldType: Class<T>): T {
        // Start with the object's class
        var currentClass: Class<*>? = obj.javaClass

        // Loop until we reach the Object class
        while (currentClass != null) {
            try {
                val field: Field = currentClass.getDeclaredField(fieldName)
                field.isAccessible = true // Allow access to private fields
                val value: Any? = field.get(obj)

                if (targetFieldType.isInstance(value)) {
                    return targetFieldType.cast(value)!!
                } else {
                    throw IllegalArgumentException("Field '$fieldName' is not of the expected type.")
                }
            } catch (e: NoSuchFieldException) {
                // Field not found in the current class, move to the superclass
                currentClass = currentClass.superclass
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }
        }

        throw IllegalArgumentException("Field '$fieldName' not found in class hierarchy.")
    }

    private fun getMethodSig(method: Method): String {
        return method.name + "-" + getMethodTypeString(method)
    }

    fun getMethodSig(method: HandlerMethod): String {
        return getMethodSig(method.method)
    }

    private fun getMethodTypeString(declaredMethod: Method): String {
        if (declaredMethod.parameterCount == 0) {
            return ""
        }
        val sb = StringBuilder()
        val parameterTypes: Array<Class<*>> = declaredMethod.parameterTypes
        for (paramType in parameterTypes) {
            sb.append(paramType.typeName + "-")
        }

        val substring = sb.substring(0, sb.length - 1)

        return substring
    }
}
