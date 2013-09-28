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
package fr.norad.jaxrs.doc.parser.jaxrs;

import java.util.regex.Pattern;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ModelDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ModelParser {

    protected final DocConfig config;

    public ModelParser(DocConfig config) {
        this.config = config;
    }

    public void parse(ProjectDefinition project, Class<?> modelClass) {
        ModelDefinition model = new ModelDefinition();
        model.setModelClass(modelClass);

        Deprecated deprecated = AnnotationUtil.findAnnotation(modelClass, Deprecated.class);
        model.setDeprecated(deprecated != null ? true : null);

        Outdated outdated = AnnotationUtil.findAnnotation(modelClass, Outdated.class);
        if (outdated != null) {
            model.setDeprecated(true);
            model.setDeprecatedCause(outdated.cause());
            model.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }
        project.getModels().put(model.getModelClass().getName(), model);
    }

    protected boolean isIgnoreModel(Class<?> modelClass) {
        for (Pattern pattern : config.getIgnoreModelPatterns()) {
            if (pattern.matcher(modelClass.getName()).matches()) {
                return true;
            }
        }
        return false;
    }

}
