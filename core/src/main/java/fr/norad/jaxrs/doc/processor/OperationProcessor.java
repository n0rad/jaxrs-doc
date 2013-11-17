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
package fr.norad.jaxrs.doc.processor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import lombok.Getter;

public class OperationProcessor {
    private final JaxrsDocProcessorFactory factory;
    @Getter
    private final Set<OperationParser> operationParsers = new LinkedHashSet<>();

    public OperationProcessor(JaxrsDocProcessorFactory factory, Collection<OperationParser> operationParsers) {
        this.factory = factory;
        this.operationParsers.addAll(operationParsers);
    }

    public OperationDefinition process(ProjectDefinition project, ApiDefinition api, Method method) {
        OperationDefinition operation = new OperationDefinition();
        for (OperationParser operationParser : operationParsers) {
            operationParser.parse(api, operation, method);

            factory.getModelProcessor().process(project, operation.getResponseClass());
            factory.getModelProcessor().process(project, operation.getResponseMapKeyClass());
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ParameterDefinition parameter = factory.getParameterProcessor().process(project, operation, method, i);
            if (operation.getParameters() == null) {
                operation.setParameters(new ArrayList<ParameterDefinition>());
            }
            operation.getParameters().add(parameter);
        }
        return operation;
    }
}
