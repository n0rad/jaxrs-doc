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
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class PropertyParser {

    private final DocConfig config;

    public PropertyParser(DocConfig config) {
        this.config = config;
    }

    public PropertyDefinition parse(ProjectDefinition project, Field field, Method getter, Method setter) {
        PropertyDefinition property = new PropertyDefinition();

        if (field != null) {
            property.setPropertyClass(field.getType());
        }
        if (getter != null) {
            property.setPropertyClass(getter.getReturnType());
        }

        property.setAsList(property.getPropertyClass().isArray() ? true : null);

        processGetterSetter(property, getter);
        processGetterSetter(property, setter);

        if (field != null) {
            Description desc = field.getAnnotation(Description.class);
            if (property.getDescription() == null) {
                property.setDescription(desc == null ? null : desc.value());
            }
        }

        //        property.setAsList(asList)

        //        if (model.getProperties() == null) {
        //            model.setProperties(new HashMap<String, ModelDefinition>());
        //        }
        //        Class<?> propertyType = property.getPropertyType();
        //        if (!model.getProperties().containsKey(propertyType)) {
        //            parse(project, propertyType);
        //        }
        //        model.getProperties().put(property.getName(), null);

        return property;
    }

    private void processGetterSetter(PropertyDefinition property, Method method) {
        if (method == null) {
            return;
        }
        Description desc = AnnotationUtil.findAnnotation(method, Description.class);
        if (property.getDescription() == null) {
            property.setDescription(desc == null ? null : desc.value());
        }

    }

}
