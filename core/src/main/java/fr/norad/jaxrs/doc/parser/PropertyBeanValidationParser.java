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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.domain.ConstraintDefinition;
import fr.norad.jaxrs.doc.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import fr.norad.jaxrs.doc.utils.ValidationUtils;

public class PropertyBeanValidationParser implements PropertyParser {
    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, PropertyDefinition property,
                      PropertyAccessor accessor) {
        Set<ConstraintDescriptor> processedDescriptor = new HashSet<>();
        if (accessor.getField() != null) {
            processField(localeDefinitions, property, accessor.getField(), processedDescriptor);
        }
        if (accessor.getSetter() != null) {
            // setter cannot have constraints
        }
        if (accessor.getGetter() != null) {
            processMethod(localeDefinitions, property, accessor.getGetter(), processedDescriptor);
        }
    }

    private void processMethod(Map<Locale, LocalizationDefinition> localeDefinitions, PropertyDefinition property,
                               Method method, Set<ConstraintDescriptor> processedDescriptor) {
        BeanDescriptor beanDescriptor = ValidationUtils.getBeanDescriptor(method.getDeclaringClass());
        MethodDescriptor methodDescriptor = beanDescriptor.getConstraintsForMethod(method.getName(),
                                                                                   method.getParameterTypes());
        if (methodDescriptor == null) {
            return;
        }
        ReturnValueDescriptor returnValueDescriptor = methodDescriptor.getReturnValueDescriptor();
        if (property.getValidationCascaded() == null) {
            property.setValidationCascaded(returnValueDescriptor.isCascaded() ? true : null);
        }
        for (ConstraintDescriptor constraint : returnValueDescriptor.getConstraintDescriptors()) {
            if (processedDescriptor.contains(constraint)) {
                continue;
            }
            if (property.getConstraints() == null) {
                property.setConstraints(new ArrayList<ConstraintDefinition>());
            }

            ConstraintDefinition constraintDefinition = new ConstraintDefinition();
            ValidationUtils.fillConstraint(localeDefinitions, constraintDefinition, constraint);
            property.getConstraints().add(constraintDefinition);
            processedDescriptor.add(constraint);
        }
    }

    private void processField(Map<Locale, LocalizationDefinition> localeDefinitions, PropertyDefinition property,
                              Field field, Set<ConstraintDescriptor> processedDescriptor) {
        BeanDescriptor beanDescriptor = ValidationUtils.getBeanDescriptor(field.getDeclaringClass());
        PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty(field.getName());
        if (propertyDescriptor == null) {
            return;
        }
        property.setValidationCascaded(propertyDescriptor.isCascaded() ? true : null);
        for (ConstraintDescriptor constraint : propertyDescriptor.getConstraintDescriptors()) {
            if (processedDescriptor.contains(constraint)) {
                continue;
            }
            if (property.getConstraints() == null) {
                property.setConstraints(new ArrayList<ConstraintDefinition>());
            }
            ConstraintDefinition constraintDefinition = new ConstraintDefinition();
            ValidationUtils.fillConstraint(localeDefinitions, constraintDefinition, constraint);
            property.getConstraints().add(constraintDefinition);
            processedDescriptor.add(constraint);
        }
    }

}
