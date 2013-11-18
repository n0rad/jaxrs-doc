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
import java.util.Set;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.annotations.Summary;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;
import fr.norad.jaxrs.doc.utils.DocUtils;

public class ApiJaxrsDocParser implements ApiParser {

    @Override
    public void parse(ApiDefinition api, Class<?> apiClass) {
        Outdated outdated = AnnotationUtils.findAnnotation(apiClass, Outdated.class);
        if (outdated != null) {
            api.setDeprecated(true);
            api.setDeprecatedCause(outdated.cause());
            api.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }

        Summary summary = AnnotationUtils.findAnnotation(apiClass, Summary.class);
        api.setSummary(summary != null ? summary.value().trim() : null);

        Description description = AnnotationUtils.findAnnotation(apiClass, Description.class);
        api.setDescription(description != null ? DocUtils.getDescription(description) : null);
    }

    @Override
    public Set<Method> findOperations(Class<?> apiClass) {
        return null;
    }

}
