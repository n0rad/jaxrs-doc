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
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.ParserHolder;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;
import lombok.Getter;

public class ParameterProcessor {
    private final JaxrsDocProcessorFactory factory;
    @Getter
    private ParserHolder<ParameterParser> parsers;

    public ParameterProcessor(JaxrsDocProcessorFactory factory, ParserHolder<ParameterParser> parsers) {
        this.factory = factory;
        this.parsers = parsers;
    }

    public ParameterDefinition process(ProjectDefinition project, OperationDefinition operation, Method method,
                                       int position) {
        ParameterDefinition parameter = new ParameterDefinition();
        for (ParameterParser parser : parsers.get()) {
            parser.parse(project.getLocalizations(), parameter, method, position);
            factory.getModelProcessor().process(project, parameter.getParamClass());
            factory.getModelProcessor().process(project, parameter.getMapKeyClass());
        }
        return parameter;
    }

}
