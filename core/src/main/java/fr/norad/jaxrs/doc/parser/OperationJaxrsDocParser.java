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
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.annotations.Summary;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class OperationJaxrsDocParser implements OperationParser {

    @Override
    public void parse(ApiDefinition api, OperationDefinition operation, Method method) {

        Summary summary = AnnotationUtil.findAnnotation(method, Summary.class);
        operation.setSummary(summary != null ? summary.value().trim() : null);

        Outdated outdated = AnnotationUtil.findAnnotation(method, Outdated.class);
        if (outdated != null) {
            operation.setDeprecated(true);
            operation.setDeprecatedCause(outdated.cause());
            operation.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }

        Description description = AnnotationUtil.findAnnotation(method, Description.class);
        operation.setDescription(description != null ? description.value().trim() : null);
    }

}
