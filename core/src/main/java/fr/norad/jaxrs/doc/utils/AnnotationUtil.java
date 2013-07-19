/**
 *
 *     Copyright (C) Awired.net
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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

public class AnnotationUtil {

    private static final Map<Class<?>, Boolean> annotatedInterfaceCache = new WeakHashMap<Class<?>, Boolean>();

    public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType,
            Class<?> classToFind) {
        if (hasAnnotation(annotationType, classToFind)) {
            return classToFind;
        }
        Class clazz = classToFind;
        while (clazz != null && (clazz.getSuperclass() != null || clazz.getInterfaces().length > 0)) {
            if (hasAnnotation(annotationType, clazz.getSuperclass())) {
                return clazz.getSuperclass();
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                for (Class declaringInterface : interfaces) {
                    if (hasAnnotation(annotationType, declaringInterface)) {
                        return declaringInterface;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private static boolean hasAnnotation(Class annotationType, Class clazz) {
        return (clazz != null && getAnnotation(clazz, annotationType) != null);
        //        return (clazz != null && clazz.getAnnotation(annotationType) != null);
    }

    //Code copied over from Spring
    public static <A extends Annotation> A findAnnotation(Class clazz, Class<A> annotationType) {
        A annotation = getAnnotation(clazz, annotationType);
        Class<?> cl = clazz;
        if (annotation == null) {
            annotation = searchOnInterfaces(clazz, annotationType, cl.getInterfaces());
        }
        while (annotation == null) {
            cl = cl.getSuperclass();
            if (cl == null || cl == Object.class) {
                break;
            }
            annotation = getAnnotation(cl, annotationType);
            if (annotation == null) {
                annotation = searchOnInterfaces(clazz, annotationType, cl.getInterfaces());
            }
        }
        return annotation;
    }

    public static <A extends Annotation> A findParameterAnnotation(Method method, int position,
            Class<A> annotationType) {
        if (position < 0 || position > method.getParameterAnnotations().length - 1) {
            throw new IllegalStateException("Param position " + position + " overflow for method : " + method);
        }

        A annotation = getParameterAnnotation(method, annotationType, position);
        Class<?> cl = method.getDeclaringClass();
        if (annotation == null) {
            annotation = searchParamAnnotationOnInterfaces(method, annotationType, cl.getInterfaces(), position);
        }
        while (annotation == null) {
            cl = cl.getSuperclass();
            if (cl == null || cl == Object.class) {
                break;
            }
            try {
                Method equivalentMethod = cl.getDeclaredMethod(method.getName(), method.getParameterTypes());
                annotation = getParameterAnnotation(equivalentMethod, annotationType, position);
                if (annotation == null) {
                    annotation = searchParamAnnotationOnInterfaces(method, annotationType, cl.getInterfaces(),
                            position);
                }
            } catch (NoSuchMethodException ex) {
                // We're done...
            }
        }
        return annotation;

    }

    //Code copied over from Spring
    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
        A annotation = getAnnotation(method, annotationType);
        Class<?> cl = method.getDeclaringClass();
        if (annotation == null) {
            annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
        }
        while (annotation == null) {
            cl = cl.getSuperclass();
            if (cl == null || cl == Object.class) {
                break;
            }
            try {
                Method equivalentMethod = cl.getDeclaredMethod(method.getName(), method.getParameterTypes());
                annotation = getAnnotation(equivalentMethod, annotationType);
                if (annotation == null) {
                    annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
                }
            } catch (NoSuchMethodException ex) {
                // We're done...
            }
        }
        return annotation;
    }

    private static <A extends Annotation> A searchParamAnnotationOnInterfaces(Method method, Class<A> annotationType,
            Class<?>[] ifcs, int position) {
        for (Class<?> iface : ifcs) {
            try {
                Method method2 = iface.getMethod(method.getName(), method.getParameterTypes());
                A parameterAnnotation = getParameterAnnotation(method2, annotationType, position);
                if (parameterAnnotation != null) {
                    return parameterAnnotation;
                }
            } catch (SecurityException | NoSuchMethodException e) {
                // nothing to do
            }
        }
        return null;
    }

    private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>[] ifcs) {
        A annotation = null;
        for (Class<?> iface : ifcs) {
            if (isInterfaceWithAnnotatedMethods(iface)) {
                try {
                    Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                    annotation = getAnnotation(equivalentMethod, annotationType);
                } catch (NoSuchMethodException ex) {
                    // Skip this interface - it doesn't have the method...
                }
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }

    private static <A extends Annotation> A searchOnInterfaces(Class clazz, Class<A> annotationType, Class<?>[] ifcs) {
        A annotation = null;
        for (Class<?> iface : ifcs) {
            annotation = getAnnotation(iface, annotationType);
            if (annotation != null) {
                break;
            }
        }
        return annotation;
    }

    private static boolean isInterfaceWithAnnotatedMethods(Class<?> iface) {
        synchronized (annotatedInterfaceCache) {
            Boolean flag = annotatedInterfaceCache.get(iface);
            if (flag != null) {
                return flag;
            }
            boolean found = false;
            for (Method ifcMethod : iface.getMethods()) {
                if (ifcMethod.getAnnotations().length > 0) {
                    found = true;
                    break;
                }
            }
            annotatedInterfaceCache.put(iface, found);
            return found;
        }
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement ae, Class<T> annotationType) {
        T ann = ae.getAnnotation(annotationType);
        if (ann == null) {
            for (Annotation metaAnn : ae.getAnnotations()) {
                ann = metaAnn.annotationType().getAnnotation(annotationType);
                if (ann != null) {
                    break;
                }
            }
        }
        return ann;
    }

    public static <T extends Annotation> T getParameterAnnotation(Method method, Class<T> annotationType, int position) {
        for (Annotation annotation : method.getParameterAnnotations()[position]) {
            if (annotation.annotationType().isAssignableFrom(annotationType)) {
                return (T) annotation;
            }
        }
        return null;
    }

}
