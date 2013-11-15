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

import java.util.List;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ModelDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ModelJaxrsDocParser implements ModelParser {

    @Override
    public void parse(ModelDefinition model, Class<?> modelClass) {
        Outdated outdated = AnnotationUtil.findAnnotation(modelClass, Outdated.class);
        if (outdated != null) {
            model.setDeprecated(true);
            model.setDeprecatedCause(outdated.cause());
            model.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }

        Description description = AnnotationUtil.findAnnotation(modelClass, Description.class);
        model.setDescription(description != null ? description.value() : null);

    }

    @Override
    public boolean isModelToIgnore(Class<?> modelClass) {
        return false;
    }

    @Override
    public List<PropertyAccessor> findProperties(Class<?> modelClass) {
        // TODO Auto-generated method stub
        return null;
    }

}
