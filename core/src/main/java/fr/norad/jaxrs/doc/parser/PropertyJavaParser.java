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
import java.util.Map;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;
import fr.norad.jaxrs.doc.utils.ReflectionUtil;

public class PropertyJavaParser implements PropertyParser {

    @Override
    public void parse(PropertyDefinition property, PropertyAccessor accessor) {
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

        Deprecated deprecated = AnnotationUtil.findAnnotation(method, Deprecated.class);
        if (deprecated != null) {
            property.setDeprecated(true);
        }
    }

    private void processTypeFromField(PropertyDefinition property, Field field) {
        Class<?> propertyClass = field.getType();
        if (Map.class.isAssignableFrom(propertyClass)) {
            property.setMapKeyClass(ReflectionUtil.getGenericFieldTypeFromPosition(field, 0));
            property.setPropertyClass(ReflectionUtil.getGenericFieldTypeFromPosition(field, 1));
            property.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(propertyClass)) {
            property.setPropertyClass(ReflectionUtil.getGenericFieldTypeFromPosition(field, 0));
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
            property.setMapKeyClass(ReflectionUtil.getGenericParamTypeForPosition(method, 0, 0));
            property.setPropertyClass(ReflectionUtil.getGenericParamTypeForPosition(method, 0, 1));
            property.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(propertyClass)) {
            property.setPropertyClass(ReflectionUtil.getSingleGenericReturnType(method));
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
            property.setMapKeyClass(ReflectionUtil.getGenericReturnTypeForPosition(method, 0));
            property.setPropertyClass(ReflectionUtil.getGenericReturnTypeForPosition(method, 1));
            property.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(propertyClass)) {
            property.setPropertyClass(ReflectionUtil.getSingleGenericReturnType(method));
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

    @Override
    public void isPropertyToIgnore(PropertyAccessor accessor) {
        // TODO Auto-generated method stub

    }

}
