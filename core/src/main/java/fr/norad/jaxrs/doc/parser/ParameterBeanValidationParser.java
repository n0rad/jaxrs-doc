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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import fr.norad.jaxrs.doc.api.domain.ConstraintDefinition;
import fr.norad.jaxrs.doc.api.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.api.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;
import fr.norad.jaxrs.doc.utils.ValidationUtils;

public class ParameterBeanValidationParser implements ParameterParser {
    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, ParameterDefinition parameter,
                      Method method, int position) {
        BeanDescriptor beanDesc = ValidationUtils.getBeanDescriptor(method.getDeclaringClass());
        MethodDescriptor constraints = beanDesc.getConstraintsForMethod(method.getName(), method.getParameterTypes());
        if (constraints == null) {
            return;
        }
        ParameterDescriptor parameterDescriptor = constraints.getParameterDescriptors().get(position);
        parameter.setValidationCascaded(parameterDescriptor.isCascaded() ? true : null);
        for (ConstraintDescriptor constraint : parameterDescriptor.getConstraintDescriptors()) {
            if (parameter.getConstraints() == null) {
                parameter.setConstraints(new ArrayList<ConstraintDefinition>());
            }
            ConstraintDefinition constraintDefinition = new ConstraintDefinition();
            ValidationUtils.fillConstraint(localeDefinitions, constraintDefinition, constraint);
            parameter.getConstraints().add(constraintDefinition);
        }
    }
}
