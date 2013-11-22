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
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import lombok.Getter;

public class PropertyProcessor {
    private final JaxrsDocProcessorFactory factory;
    @Getter
    private ParserHolder<PropertyParser> parsers;

    public PropertyProcessor(JaxrsDocProcessorFactory factory, ParserHolder<PropertyParser> parsers) {
        this.factory = factory;
        this.parsers = parsers;
    }

    public PropertyDefinition process(ProjectDefinition project, PropertyAccessor accessor) {
        PropertyDefinition property = new PropertyDefinition();
        for (PropertyParser parser : parsers.get()) {
            parser.parse(project.getLocalizations(), property, accessor);
            factory.getModelProcessor().process(project, property.getPropertyClass());
            factory.getModelProcessor().process(project, property.getMapKeyClass());
        }
        return property;
    }
}
