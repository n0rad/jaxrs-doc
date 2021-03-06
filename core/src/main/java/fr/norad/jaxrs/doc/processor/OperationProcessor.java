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
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.ParserHolder;
import fr.norad.jaxrs.doc.api.domain.ApiDefinition;
import fr.norad.jaxrs.doc.api.domain.ErrorOperationDefinition;
import fr.norad.jaxrs.doc.api.domain.OperationDefinition;
import fr.norad.jaxrs.doc.api.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.api.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import lombok.Getter;

public class OperationProcessor {
    private final JaxrsDocProcessorFactory factory;
    @Getter
    private ParserHolder<OperationParser> parsers;

    public OperationProcessor(JaxrsDocProcessorFactory factory, ParserHolder<OperationParser> parsers) {
        this.factory = factory;
        this.parsers = parsers;
    }

    public OperationDefinition process(ProjectDefinition project, ApiDefinition api, Method method) {
        OperationDefinition operation = new OperationDefinition();
        for (OperationParser operationParser : parsers.get()) {
            operationParser.parse(api, operation, method);

            factory.getModelProcessor().process(project, operation.getResponseClass());
            factory.getModelProcessor().process(project, operation.getResponseMapKeyClass());

        }

        if (operation.getErrors() != null) {
            for (ErrorOperationDefinition errorOperation : operation.getErrors()) {
                factory.getErrorProcessor().process(project, errorOperation);
            }
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
