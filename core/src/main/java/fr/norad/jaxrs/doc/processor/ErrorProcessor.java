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

import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.ParserHolder;
import fr.norad.jaxrs.doc.api.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.api.domain.ErrorOperationDefinition;
import fr.norad.jaxrs.doc.api.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ErrorParser;
import lombok.Getter;

public class ErrorProcessor {

    private final JaxrsDocProcessorFactory factory;
    @Getter
    private ParserHolder<ErrorParser> parsers;

    public ErrorProcessor(JaxrsDocProcessorFactory factory, ParserHolder<ErrorParser> parsers) {
        this.factory = factory;
        this.parsers = parsers;
    }

    public void process(ProjectDefinition project, ErrorOperationDefinition errorOperation) {
        ErrorDefinition error = new ErrorDefinition(errorOperation.getErrorClass());
        project.getErrors().put(error.getErrorClass(), error);
        for (ErrorParser parser : parsers.get()) {
            parser.parse(error, errorOperation.getErrorClass());
        }
    }

}
