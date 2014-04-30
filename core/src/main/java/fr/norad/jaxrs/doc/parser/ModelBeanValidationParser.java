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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.api.domain.ConstraintDefinition;
import fr.norad.jaxrs.doc.api.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.api.domain.ModelDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;
import fr.norad.jaxrs.doc.utils.ValidationUtils;

public class ModelBeanValidationParser implements ModelParser {
    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, ModelDefinition model,
                      Class<?> modelClass) {
        BeanDescriptor beanDescriptor = ValidationUtils.getBeanDescriptor(modelClass);
        Set<ConstraintDescriptor<?>> beanConstraints = beanDescriptor.getConstraintDescriptors();
        for (ConstraintDescriptor constraint : beanConstraints) {
            if (model.getConstraints() == null) {
                model.setConstraints(new ArrayList<ConstraintDefinition>());
            }
            ConstraintDefinition constraintDefinition = new ConstraintDefinition();
            ValidationUtils.fillConstraint(localeDefinitions, constraintDefinition, constraint);
            model.getConstraints().add(constraintDefinition);
        }
    }

    @Override
    public boolean isModelToIgnore(Class<?> modelClass) {
        return false;
    }

    @Override
    public List<PropertyAccessor> findProperties(Class<?> modelClass) {
        return null;
    }
}
