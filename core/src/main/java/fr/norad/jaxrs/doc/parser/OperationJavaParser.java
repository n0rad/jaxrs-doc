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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtils;
import fr.norad.jaxrs.doc.utils.ReflectionUtils;

public class OperationJavaParser implements OperationParser {

    @Override
    public void parse(ApiDefinition api, OperationDefinition operation, Method method) {
        operation.setMethodName(method.getName());
        operation.setOperationClass(api.getApiClass());

        Deprecated deprecated = AnnotationUtils.findAnnotation(method, Deprecated.class);
        operation.setDeprecated(deprecated != null ? true : null);

        List<Class<?>> exceptions = ReflectionUtils.getExceptions(method);
        for (Class<?> exception : exceptions) {
            if (operation.getErrors() == null) {
                operation.setErrors(new ArrayList<ErrorDefinition>());
            }
            operation.getErrors().add(new ErrorDefinition(exception));
        }

        fillReturnPart(operation, method);
    }

    protected void fillReturnPart(OperationDefinition operation, Method method) {
        if (Map.class.isAssignableFrom(method.getReturnType())) {
            operation.setResponseMapKeyClass(ReflectionUtils.getGenericReturnTypeForPosition(method, 0));
            operation.setResponseClass(ReflectionUtils.getGenericReturnTypeForPosition(method, 1));
            operation.setResponseAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            operation.setResponseClass(ReflectionUtils.getSingleGenericReturnType(method));
            operation.setResponseAsList(true);
            return;
        }
        if (method.getReturnType().isArray()) {
            operation.setResponseClass(method.getReturnType().getComponentType());
            operation.setResponseAsList(true);
            return;
        }

        if (!method.getReturnType().equals(Void.TYPE)) {
            operation.setResponseClass(method.getReturnType());
        }
    }

}
