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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.domain.ModelDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;
import lombok.Getter;

public class ModelProcessor {
    private final JaxrsDocProcessorFactory factory;
    @Getter
    private final Set<ModelParser> parsers = new LinkedHashSet<>();

    public ModelProcessor(JaxrsDocProcessorFactory factory, Collection<ModelParser> parsers) {
        this.factory = factory;
        this.parsers.addAll(parsers);
    }

    public void process(ProjectDefinition project, Class<?> modelClass) {
        if (isModelToIgnore(modelClass)) {
            return;
        }
        if (project.getModels().containsKey(modelClass.getName())) {
            return; // already processed
        }

        ModelDefinition model = new ModelDefinition(modelClass);
        project.getModels().put(model.getModelClass().getName(), model);

        for (ModelParser parser : parsers) {
            parser.parse(project.getLocalizations(), model, modelClass);
            List<PropertyAccessor> propertyAccessors = parser.findProperties(modelClass);
            if (propertyAccessors == null) {
                continue;
            }

            for (PropertyAccessor propertyAccessor : propertyAccessors) {
                PropertyDefinition property = factory.getPropertyProcessor().process(project, propertyAccessor);
                model.getProperties().put(propertyAccessor.getName(), property);
            }
        }

    }

    public boolean isModelToIgnore(Class<?> modelClass) {
        if (modelClass == null) {
            return true;
        }
        for (ModelParser parser : parsers) {
            if (parser.isModelToIgnore(modelClass)) {
                return true;
            }
        }
        return false;
    }

}
