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
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;
import fr.norad.jaxrs.doc.utils.ReflectionUtil;

public class PropertyJavaParser implements PropertyParser {

    @Override
    public void parse(PropertyDefinition property, Field field, Method getter, Method setter) {
        if (field != null) {
            processType(property, field.getType());
        }
        if (setter != null) {
            processType(property, setter.getParameterTypes()[0]);
        }
        if (getter != null) {
            processType(property, getter.getReturnType());
        }

        processGetterSetter(property, getter);
        processGetterSetter(property, setter);

        if (field != null) {
            Deprecated deprecated = field.getAnnotation(Deprecated.class);
            property.setDeprecated(deprecated != null ? true : null);

        }

    }

    private void processGetterSetter(PropertyDefinition property, Method method) {
        if (method == null) {
            return;
        }

        Deprecated deprecated = AnnotationUtil.findAnnotation(method, Deprecated.class);
        property.setDeprecated(deprecated != null ? true : null);

    }

    private void processType(PropertyDefinition property, Class<?> propertyClass) {
        if (Map.class.isAssignableFrom(propertyClass)) {
            property.setResponseMapKeyClass(ReflectionUtil.getGenericReturnTypeForPosition(method, 0));
            property.setResponseClass(ReflectionUtil.getGenericReturnTypeForPosition(method, 1));
            property.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(propertyClass)) {
            property.setResponseClass(ReflectionUtil.getSingleGenericReturnType(method));
            property.setAsList(true);
            return;
        }
        if (propertyClass.isArray()) {
            property.setPropertyClass(propertyClass.getComponentType());
            property.setAsList(true);
            return;
        }
    }

}
