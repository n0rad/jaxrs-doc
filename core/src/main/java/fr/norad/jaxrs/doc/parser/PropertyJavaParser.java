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
package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.core.lang.reflect.ReflectionUtils;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;

public class PropertyJavaParser implements PropertyParser {

    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, PropertyDefinition property,
                      PropertyAccessor accessor) {
        if (accessor.getField() != null) {
            processTypeFromField(property, accessor.getField());
        }
        if (accessor.getSetter() != null) {
            processTypeFromSetter(property, accessor.getSetter());
        }
        if (accessor.getGetter() != null) {
            processTypeFromGetter(property, accessor.getGetter());
        }

        processGetterSetter(property, accessor.getGetter());
        processGetterSetter(property, accessor.getSetter());

        if (accessor.getField() != null) {
            Deprecated deprecated = accessor.getField().getAnnotation(Deprecated.class);
            if (deprecated != null) {
                property.setDeprecated(true);
            }
        }
    }

    private void processGetterSetter(PropertyDefinition property, Method method) {
        if (method == null) {
            return;
        }

        Deprecated deprecated = AnnotationUtils.findAnnotation(method, Deprecated.class);
        if (deprecated != null) {
            property.setDeprecated(true);
        }
    }

    private void processTypeFromField(PropertyDefinition property, Field field) {
        Class<?> propertyClass = field.getType();
        if (Map.class.isAssignableFrom(propertyClass)) {
            property.setMapKeyClass(ReflectionUtils.getGenericFieldTypeFromPosition(field, 0));
            property.setPropertyClass(ReflectionUtils.getGenericFieldTypeFromPosition(field, 1));
            property.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(propertyClass)) {
            property.setPropertyClass(ReflectionUtils.getGenericFieldTypeFromPosition(field, 0));
            property.setAsList(true);
            return;
        }
        if (propertyClass.isArray()) {
            property.setPropertyClass(propertyClass.getComponentType());
            property.setAsList(true);
            return;
        }
        property.setPropertyClass(propertyClass);
    }

    private void processTypeFromSetter(PropertyDefinition property, Method method) {
        Class<?> propertyClass = method.getParameterTypes()[0];
        if (Map.class.isAssignableFrom(propertyClass)) {
            property.setMapKeyClass(ReflectionUtils.getGenericParamTypeForPosition(method, 0, 0));
            property.setPropertyClass(ReflectionUtils.getGenericParamTypeForPosition(method, 0, 1));
            property.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(propertyClass)) {
            property.setPropertyClass(ReflectionUtils.getSingleGenericReturnType(method));
            property.setAsList(true);
            return;
        }
        if (propertyClass.isArray()) {
            property.setPropertyClass(propertyClass.getComponentType());
            property.setAsList(true);
            return;
        }
        property.setPropertyClass(propertyClass);
    }

    private void processTypeFromGetter(PropertyDefinition property, Method method) {
        Class<?> propertyClass = method.getReturnType();
        if (Map.class.isAssignableFrom(propertyClass)) {
            property.setMapKeyClass(ReflectionUtils.getGenericReturnTypeForPosition(method, 0));
            property.setPropertyClass(ReflectionUtils.getGenericReturnTypeForPosition(method, 1));
            property.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(propertyClass)) {
            property.setPropertyClass(ReflectionUtils.getSingleGenericReturnType(method));
            property.setAsList(true);
            return;
        }
        if (propertyClass.isArray()) {
            property.setPropertyClass(propertyClass.getComponentType());
            property.setAsList(true);
            return;
        }
        property.setPropertyClass(propertyClass);
    }

}
