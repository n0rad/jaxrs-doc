/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.doc.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    public static Class<?> getSingleGenericParamType(Method method, int paramPosition) {
        return getGenericParamTypeForPosition(method, paramPosition, 0);
    }

    public static Class<?> getGenericParamTypeForPosition(Method method, int paramPosition, int genericPosition) {
        try {
            Type genericParamType = method.getGenericParameterTypes()[paramPosition];
            ParameterizedType type = (ParameterizedType) genericParamType;
            return (Class<?>) type.getActualTypeArguments()[genericPosition];
        } catch (Exception e) {
            return null;
        }
    }

    public static Class<?> getGenericReturnTypeForPosition(Method method, int position) {
        try {
            Type genericReturnType = method.getGenericReturnType();
            ParameterizedType type = (ParameterizedType) genericReturnType;
            return (Class<?>) type.getActualTypeArguments()[position];
        } catch (Exception e) {
            return null;
        }
    }

    public static Class<?> getSingleGenericReturnType(Method method) {
        return getGenericReturnTypeForPosition(method, 0);
    }

    /**
     * determines which interface declared a given method on a class
     */
    public static Class<?> getDeclaringInterface(final Method method) {
        for (Class<?> anInterface : method.getDeclaringClass().getInterfaces()) {
            final Method[] interfaceMethods = anInterface.getMethods();
            for (Method interfaceMethod : interfaceMethods) {
                if (interfaceMethod.getName().equals(method.getName())
                        && interfaceMethod.getReturnType().equals(method.getReturnType())) {
                    final Class<?>[] iMethodParams = interfaceMethod.getParameterTypes();
                    final Class<?>[] methodParams = method.getParameterTypes();

                    boolean equal = true;
                    if (iMethodParams.length == methodParams.length) {
                        for (int i = 0; i < methodParams.length; i++) {
                            final Class<?> methodParam = methodParams[i];
                            final Class<?> iMethodParam = iMethodParams[i];

                            if (!methodParam.equals(iMethodParam)) {
                                equal = false;
                                break;
                            }
                        }
                    }

                    if (equal) {
                        return interfaceMethod.getDeclaringClass();
                    }

                }

            }
        }
        return null;
    }

    public static List<Class<?>> getExceptions(Method method) {
        List<Class<?>> exceptions = new ArrayList<>();
        Class<?> declaringClass = method.getDeclaringClass();
        do {
            for (Class<?> interfaceClass : declaringClass.getInterfaces()) {
                try {
                    Method interfaceMethod = interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                    for (Class<?> exceptionClass : interfaceMethod.getExceptionTypes()) {
                        if (!exceptions.contains(exceptionClass)) {
                            exceptions.add(exceptionClass);
                        }
                    }
                } catch (NoSuchMethodException | SecurityException e) {
                }
            }
            try {
                Method interfaceMethod = declaringClass.getMethod(method.getName(), method.getParameterTypes());
                for (Class<?> exceptionClass : interfaceMethod.getExceptionTypes()) {
                    if (!exceptions.contains(exceptionClass)) {
                        exceptions.add(exceptionClass);
                    }
                }
            } catch (NoSuchMethodException | SecurityException e) {
            }
        } while ((declaringClass = declaringClass.getSuperclass()) != null);
        return exceptions;
    }
}
