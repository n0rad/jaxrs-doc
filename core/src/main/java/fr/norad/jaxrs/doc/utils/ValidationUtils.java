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

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.MessageInterpolator;
import javax.validation.Payload;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import fr.norad.jaxrs.doc.api.domain.ConstraintDefinition;
import fr.norad.jaxrs.doc.api.domain.LocalizationDefinition;

public class ValidationUtils {
    private static final Map<Class<?>, BeanDescriptor> cache = new ConcurrentHashMap<>();
    private static final MessageInterpolator.Context emptyContext = new EmptyInterpolatorContext();
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static BeanDescriptor getBeanDescriptor(Class<?> clazz) {
        if (!cache.containsKey(clazz)) {
            BeanDescriptor beanDesc = factory.getValidator().getConstraintsForClass(clazz);
            cache.put(clazz, beanDesc);
        }
        return cache.get(clazz);
    }

    public static void fillMessage(Map<Locale, LocalizationDefinition> localeDefinitions, String key) {
        for (Locale locale : localeDefinitions.keySet()) {
            if (!localeDefinitions.get(locale).getMessages().containsKey(key)) {
                String message = factory.getMessageInterpolator().interpolate(key, emptyContext, locale);
                localeDefinitions.get(locale).getMessages().put(key, message);
            }
        }
    }

    public static void fillConstraint(Map<Locale, LocalizationDefinition> localeDefinitions,
                                      ConstraintDefinition constraint,
                                      ConstraintDescriptor<?> constraintFromValidator) {
        constraint.setConstraintClass(constraintFromValidator.getAnnotation().annotationType().getName());
        constraint.setReportAsSingle(constraintFromValidator.isReportAsSingleViolation());
        Map<String, Object> attributes = new HashMap<>(constraintFromValidator.getAttributes());
        if (((Class[]) attributes.get("groups")).length == 0) {
            attributes.remove("groups");
        }
        if (((Class[]) attributes.get("payload")).length == 0) {
            attributes.remove("payload");
        }
        constraint.setAttributes(attributes);
        Object messageKey = constraint.getAttributes().get("message");
        fillMessage(localeDefinitions, messageKey.toString());

        Set<ConstraintDescriptor<?>> composingConstraints = constraintFromValidator.getComposingConstraints();
        if (!composingConstraints.isEmpty()) {
            Set<ConstraintDefinition> constraints = new HashSet<>(composingConstraints.size());
            constraint.setComposingConstraints(constraints);
            for (ConstraintDescriptor<?> composingConstraintFromValidator : composingConstraints) {
                ConstraintDefinition composingConstraint = new ConstraintDefinition();
                constraints.add(composingConstraint);
                fillConstraint(localeDefinitions, composingConstraint, composingConstraintFromValidator);
            }
        }
    }

    private static class EmptyInterpolatorContext implements MessageInterpolator.Context {
        @Override
        public Object getValidatedValue() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> type) {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return new ConstraintDescriptor<Annotation>() {
                @Override
                public Annotation getAnnotation() {
                    return null;
                }

                @Override
                public String getMessageTemplate() {
                    return null;
                }

                @Override
                public Set<Class<?>> getGroups() {
                    return null;
                }

                @Override
                public Set<Class<? extends Payload>> getPayload() {
                    return null;
                }

                @Override
                public ConstraintTarget getValidationAppliesTo() {
                    return null;
                }

                @Override
                public List<Class<? extends ConstraintValidator<Annotation, ?>>> getConstraintValidatorClasses() {
                    return null;
                }

                @Override
                public Map<String, Object> getAttributes() {
                    return new HashMap<String, Object>();
                }

                @Override
                public Set<ConstraintDescriptor<?>> getComposingConstraints() {
                    return null;
                }

                @Override
                public boolean isReportAsSingleViolation() {
                    return false;
                }
            };
        }
    }

}
